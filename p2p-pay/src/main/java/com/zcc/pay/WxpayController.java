package com.zcc.pay;

import com.bjpowernode.http.HttpClientUtils;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:微信支付
 * @author:zcc
 * @data:2019/3/4 0004
 */

@Controller
public class WxpayController {

    //pay工程调用微信支付接口
    @RequestMapping(value = "/api/wxpay")
    @ResponseBody
    public Map<String, String> wxpay(HttpServletRequest request,
                                     @RequestParam(value = "out_trade_no",required = true ) String out_trade_no,
                                     @RequestParam(value = "total_fee",required = true) Double total_fee) throws Exception {

        //给微信支付接口传递参数
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appid","wx8a3fcf509313fd74");
        paramMap.put("mch_id","1361137902");
        paramMap.put("nonce_str",WXPayUtil.generateNonceStr());
        paramMap.put("body","微信支付");
        paramMap.put("out_trade_no",out_trade_no);

        //multiply是total_fee转换为以分为单位的数
        BigDecimal bigDecimal = new BigDecimal(total_fee);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        int intValue = multiply.intValue();

        paramMap.put("total_fee",String.valueOf(intValue));
        paramMap.put("spbill_create_ip","127.0.0.1");

        //需要公网地址，因此不能返回异步通知
        paramMap.put("notify_url","http:localhost:9090/pay/api/wxpayNotify");
        paramMap.put("trade_type","NATIVE");
        paramMap.put("sign",WXPayUtil.generateSignature(paramMap,"367151c5fd0d50f1e34a68a802d6bbca"));


        String paramXml = WXPayUtil.mapToXml(paramMap);
        String responseXml = HttpClientUtils.doPostByXml("https://api.mch.weixin.qq.com/pay/unifiedorder",paramXml);
        Map<String, String> responseMap = WXPayUtil.xmlToMap(responseXml);

        return responseMap;
    }

}
