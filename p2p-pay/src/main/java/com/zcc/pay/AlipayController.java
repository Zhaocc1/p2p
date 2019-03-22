package com.zcc.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.zcc.AlipayConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @description:
 * @author:zcc
 * @data:2019/3/2 0002
 */
@Controller
public class AlipayController {


    //调用支付宝的支付接口
    @RequestMapping(value = "/api/alipay")
    public String alipay(HttpServletRequest request, Model model,
                         @RequestParam(value = "out_trade_no",required = true) String out_trade_no,
                         @RequestParam(value = "body",required = true) String body,
                         @RequestParam(value = "total_amount",required = true) Double total_amount,
                         @RequestParam(value = "subject",required = true) String subject){

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key,
                "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);

        //创建API对应的request
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        //在公共参数中设置回跳和通知地址
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+out_trade_no+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+total_amount+"," +
                "    \"subject\":\""+subject+"\"," +
                "    \"body\":\""+body+"\"" +
                "  }");//填充业务参数

        String form="";
        try {
            //调用SDK生成表单
            form = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        System.out.println(form);
        model.addAttribute("form",form);

        return "payToAlipay";

    }

    //接收支付成功后返回的同步返回参数
    @RequestMapping(value = "/api/alipayBack")
    public String alipayBack(HttpServletRequest request,Model model) throws UnsupportedEncodingException, AlipayApiException {

        /* *
         * 功能：支付宝服务器同步通知页面
         * 日期：2017-03-30
         *
         */

        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();

        Map<String,String[]> requestParams = request.getParameterMap();

        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            System.out.println(name);
            System.out.println(valueStr);
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);

        //——请在这里编写您的程序（以下代码仅作参考）——
        if(signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

           model.addAttribute("params",params);
           model.addAttribute("signVerified",signVerified);


        }else {
            model.addAttribute("signVerified",signVerified);

        }

        model.addAttribute("pay_p2p_return_url","http://localhost:8080/p2p/loan/alipayBack");

        return "payToP2p";

    }

    @RequestMapping(value = "/api/alipayQuery")
    @ResponseBody
    public String alipayQuery(HttpServletRequest request,
                              @RequestParam(value = "out_trade_no",required = true) String out_trade_no) throws UnsupportedEncodingException, AlipayApiException {

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();


        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"}");

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();

        return result;
    }

}
