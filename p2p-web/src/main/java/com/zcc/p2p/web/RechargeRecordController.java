package com.zcc.p2p.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.http.HttpClientUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.zcc.p2p.common.utils.DateUtil;
import com.zcc.p2p.model.loan.RechargeRecord;
import com.zcc.p2p.model.user.User;
import com.zcc.p2p.service.tool.RedisService;
import com.zcc.p2p.service.user.RechargeRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author:zcc
 * @data:2019/3/2 0002
 */

@Controller
public class RechargeRecordController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RechargeRecordService rechargeRecordService;


    //支付宝支付
    @RequestMapping(value = "/loan/toAlipayRecharge")
    public String toAlipayRecharge(HttpServletRequest request, Model model,
                                   @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) {


        //生成充值记录 id uid recharge_no recharge_status recharge_money recharge_time recharge_desc
        RechargeRecord rechargeRecord = new RechargeRecord();

        User user = (User) request.getSession().getAttribute("user");
        if (null != user) {
            rechargeRecord.setUid(user.getId());
        }

        //订单号recharge_no=时间戳+Redis全局唯一数字
        String rechargeNo = DateUtil.getTimeStamp() + redisService.getOnlyNumber();
        rechargeRecord.setRechargeNo(rechargeNo);

        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeDesc("支付宝订单");
        //待支付订单，状态为0
        rechargeRecord.setRechargeStatus("0");
        rechargeRecord.setRechargeMoney(rechargeMoney);

        //生成充值记录增加到数据库
        int addCount = rechargeRecordService.addRechargeRecord(rechargeRecord);

        if (addCount > 0) {

            //给pay工程传递参数：out_trade_no、product_code、total_amount、subject
            model.addAttribute("p2p_pay_alipay_url", "http://localhost:9090/pay/api/alipay");
            model.addAttribute("out_trade_no", rechargeNo);
            model.addAttribute("body", "支付宝充值");
            model.addAttribute("total_amount", rechargeMoney);
            model.addAttribute("subject", "充值");

        } else {

            model.addAttribute("trade_msg", "系统繁忙，请稍后重试...");
            return "toRechargeBack";

        }

        return "p2pToAlipay";

    }

    //接收pay工程传回的同步请求参数
    @RequestMapping(value = "/loan/alipayBack")
    public String alipayBack(HttpServletRequest request, Model model,
                             @RequestParam(value = "signVerified", required = true) boolean signVerified,
                             @RequestParam(value = "out_trade_no", required = true) String out_trade_no,
                             @RequestParam(value = "total_amount", required = true) Double total_amount) throws Exception {

        //signVerified为true验签成功
        if (signVerified) {

            //调用pay工程的查询订单接口
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("out_trade_no", out_trade_no);

            String jsonString = HttpClientUtils.doPost("http://localhost:9090/pay/api/alipayQuery", paramMap);
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            JSONObject alipayResponse = jsonObject.getJSONObject("alipay_trade_query_response");
            String code = alipayResponse.getString("code");
            if (StringUtils.equals("10000", code)) {

                //接口调用成功
                String trade_status = alipayResponse.getString("trade_status");

                switch (trade_status) {

                    //充值成功：1.更新充值订单的状态码为1  2.给用户充值（充值订单号，用户id，充值金额）
                    case "TRADE_SUCCESS":

                        User user = (User) request.getSession().getAttribute("user");
                        paramMap.put("uid",user.getId());
                        paramMap.put("total_amount",total_amount);
                        boolean flag = rechargeRecordService.recharge(paramMap);
                        if (!flag ){

                            model.addAttribute("trade_msg", "充值失败，请稍后重试...");
                            return "toRechargeBack";

                        }
                        break;

                    //交易关闭：1.更新充值订单的状态码为2
                    case "TRADE_CLOSED":

                        RechargeRecord rechargeRecord = new RechargeRecord();
                        rechargeRecord.setRechargeNo(out_trade_no);
                        rechargeRecord.setRechargeStatus("2");
                        rechargeRecordService.modifyRechargeStatusByTradeNo(rechargeRecord);

                        model.addAttribute("trade_msg", "订单关闭，如有需要，请重新充值...");
                        return "toRechargeBack";

                    //充值失败
                    default:

                        model.addAttribute("trade_msg", "充值失败，请稍后重试...");
                        return "toRechargeBack";

                }

            } else {

                model.addAttribute("query_msg", "查询订单失败");
                return "toRechargeBack";

            }

        }

        return "forward:myCenter";

    }


    //微信支付
    @RequestMapping(value = "/loan/toWxpayRecharge")
    public String toWxpayRecharge(HttpServletRequest request,Model model,
                                  @RequestParam(value = "rechargeMoney",required = true)Double rechargeMoney) {


        //生成充值记录
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setRechargeMoney(rechargeMoney);

        User user = (User) request.getSession().getAttribute("user");
        rechargeRecord.setUid(user.getId());
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeStatus("0");
        String rechargeNo = DateUtil.getTimeStamp()+redisService.getOnlyNumber();
        rechargeRecord.setRechargeDesc("微信充值");
        rechargeRecord.setRechargeNo(rechargeNo);

        int addCount = rechargeRecordService.addRechargeRecord(rechargeRecord);
        if (addCount <= 0 ){

            model.addAttribute("trade_msg","系统忙，你个丑八怪请稍后重试");
            return "toRechargeBack";

        }

        //给showQRCode页面传递参数
        String rechargeTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rechargeRecord.getRechargeTime());

        model.addAttribute("rechargeNo",rechargeNo);
        model.addAttribute("rechargeMoney",rechargeMoney);
        model.addAttribute("rechargeTime",rechargeTime);

        return "showQRCode";

    }


    //调用pay工程接口，传递参数；通信成功后，生成二维码
    @RequestMapping(value = "/loan/generateQRCode")
    public void generateQRCode(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(value = "out_trade_no" ,required = true) String out_trade_no,
                                 @RequestParam(value = "total_fee",required = true) Double total_fee) throws Exception {

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("out_trade_no",out_trade_no);
        paramMap.put("total_fee",total_fee);

        //调用pay工程的api/wxpay
        String jsonString = HttpClientUtils.doPost("http://localhost:9090/pay/api/wxpay", paramMap);


        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String return_code = jsonObject.getString("return_code");
        if (StringUtils.equals(return_code,"SUCCESS")){

            //通信成功，获取业务处理标识
            String result_code = jsonObject.getString("result_code");

            if (StringUtils.equals(result_code,"SUCCESS")){

                //获取微信用于生成二维码的连接
                String code_url = jsonObject.getString("code_url");

                //生成二维码
                BitMatrix bitMatrix = new MultiFormatWriter().encode(code_url,BarcodeFormat.QR_CODE,200,200);

                OutputStream outputStream = response.getOutputStream();

                MatrixToImageWriter.writeToStream(bitMatrix,"png",outputStream);

                outputStream.flush();
                outputStream.close();

            }

        }

    }

}
