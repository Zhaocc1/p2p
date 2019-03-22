package com.zcc.p2p.web;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.http.HttpClientUtils;
import com.zcc.p2p.common.constant.Constant;
import com.zcc.p2p.model.loan.BidInfo;
import com.zcc.p2p.model.loan.IncomeRecord;
import com.zcc.p2p.model.loan.LoanInfo;
import com.zcc.p2p.model.loan.RechargeRecord;
import com.zcc.p2p.model.user.FinanceAccount;
import com.zcc.p2p.model.user.User;
import com.zcc.p2p.service.loan.BidInfoService;
import com.zcc.p2p.service.loan.LoanInfoService;
import com.zcc.p2p.service.tool.RedisService;
import com.zcc.p2p.service.user.FinanceAccountService;
import com.zcc.p2p.service.user.IncomeRecordService;
import com.zcc.p2p.service.user.RechargeRecordService;
import com.zcc.p2p.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @description:
 * @author:zcc
 * @data:2019/2/25 0025
 */

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private BidInfoService bidInfoService;

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RechargeRecordService rechargeRecordService;

    @Autowired
    private IncomeRecordService incomeRecordService;


    /**
     * 根绝手机号查用户（验证手机号是否重复）
     * @param request
     * @param phone
     * @return json:{"message":"OK"/"手机号重复，请更换"}
     * 返回字符串中文乱码问题：字符串转换和对象转换用的是两个转换器
     * 第一种：在@RequestMapping中添加produces="text/html;charset=UTF-8"
     * 第二种：在MVC 配置中加入，spring版本必需为3.1或以上版本才可以下配置
     * <mvc:annotation-driven>
     *         <mvc:message-converters register-defaults="true">
     *             <bean class="org.springframework.http.converter.StringHttpMessageConverter">
     *               <constructor-arg value="UTF-8" />
     *             </bean>
     *         </mvc:message-converters>
     *     </mvc:annotation-driven>
     */
    @RequestMapping(value = "/user/phoneCheck",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String phoneCheck(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "phone",required = true) String phone){


        //response.setCharacterEncoding("utf-8");
        User user = userService.queryUserByPhone(phone);

        return null == user ? "OK" : "手机号重复，请重新输入";


    }

    /**
     * 验证图片验证码
     * @param request
     * @param captcha
     * @return true或false
     */
    @RequestMapping(value = "/user/captchaCheck")
    @ResponseBody
    public boolean captchaCheck(HttpServletRequest request,
                               @RequestParam(value = "captcha",required = true) String captcha){

        String actualCaptcha = (String) request.getSession().getAttribute(Constant.CAPTCHA);

        return StringUtils.equalsIgnoreCase(captcha,actualCaptcha);

    }

    /**
     * 注册
     * @param request
     * @param phone
     * @param loginPassword
     * @return
     */
    @RequestMapping(value = "/user/register")
    @ResponseBody
    public Map<String,Object> register(HttpServletRequest request,
                                       @RequestParam(value = "phone",required = true) String phone,
                                       @RequestParam(value = "loginPassword",required = true) String loginPassword){

        Map<String,Object> map = new HashMap<>();


        //注册功能：1.增加用户 2.增加该用户的账户
        boolean flag = userService.register(phone,loginPassword);

        if (flag){

            request.getSession().setAttribute(Constant.SESSION_USER,userService.queryUserByPhone(phone));
            map.put("message","OK");

        }else{

            map.put("message","注册失败");

        }


        return map;

    }

    //查账户
    @RequestMapping(value = "/user/myFinanceAccount")
    @ResponseBody
    public FinanceAccount myFinanceAccount(HttpServletRequest request){

        User user= (User) request.getSession().getAttribute("user");

        FinanceAccount financeAccount = financeAccountService.queryFinanceAccount(user.getId());

        return financeAccount;

    }

    /**
     * 真实姓名和身份证验证
     */
    @RequestMapping(value = "/user/verifyRealName")
    @ResponseBody
    public String verifyRealName(HttpServletRequest request,
                                 @RequestParam(value = "realName" ,required = true) String realName,
                                 @RequestParam(value = "idCard",required = true) String idCard) throws Exception {

        System.out.println("进入实名验证方法");
        String message = "";

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("appkey","975949ae2a36adec5734e0625f08e700");
        paramMap.put("cardNo",idCard);
        paramMap.put("realName",realName);

        //调用京东万象接口认证真实姓名和身份证号是否匹配,返回值String
        //String JsonString = HttpClientUtils.doPost("https://way.jd.com/youhuoBeijing/test",paramMap);

        String JsonString = "{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 1305,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": {\n" +
                "        \"error_code\": 0,\n" +
                "        \"reason\": \"成功\",\n" +
                "        \"result\": {\n" +
                "            \"realname\": \"乐天磊\",\n" +
                "            \"idcard\": \"350721197702134399\",\n" +
                "            \"isok\": true\n" +
                "        }\n" +
                "    }\n" +
                "}";

        //将JSONString转换为JSON对象，取出isok的值
        JSONObject jsonObject = (JSONObject) JSONObject.parse(JsonString);

        //获取通信标识，10000为通信成功
        String code = jsonObject.getString("code");
        if (StringUtils.equals("10000",code)){

            //"isok": true则更新用户（姓名，身份证号），
            boolean isok = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");
            if (isok){

                User user = (User) request.getSession().getAttribute("user");
                User updateUser = new User();
                updateUser.setId(user.getId());
                updateUser.setName(realName);
                updateUser.setIdCard(idCard);
                int count = userService.modifyUserById(updateUser);
                if (count == 1){
                    message = "OK";

                    //更新session中的user
                    user.setName(realName);
                    user.setIdCard(idCard);
                    request.getSession().setAttribute("user",user);
                }else{
                    message = "实名认证异常";
                }

            }else{
                message = "身份证号和真实姓名不符";
            }

        }else{

            message = "通信失败";

        }

        return message;

    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @RequestMapping(value = "/loan/logout")
    public String logout(HttpServletRequest request){

        request.getSession().invalidate();

        return "redirect:/index";

    }

    /**
     * 初始化登录页面：获取三个参数
     * @return
     */
    @RequestMapping(value = "/loan/initLoginPage")
    @ResponseBody
    public Map<String,Object> initLoginPage(){

        Map<String,Object> map = new HashMap<>();

        Long userCount = userService.queryUserCount();
        Double historicalAverageRate = loanInfoService.queryHistoricalAverageRate();
        Double bidMoney = bidInfoService.queryBidMoney();

        map.put("userCount",userCount);
        map.put("historicalAverageRate",historicalAverageRate);
        map.put("bidMoney",bidMoney);

        return map;

    }

    /**
     * 获取短信验证码
     * @param request
     * @param phone
     * @return
     */
    @RequestMapping(value = "/loan/messageCode")
    @ResponseBody
    public Map<String,Object> messageCode(HttpServletRequest request,
                              @RequestParam(value = "phone",required = true) String phone) throws Exception {

        Map<String,Object> retMap = new HashMap<>();
        //errorMessage  messageCode
        //975949ae2a36adec5734e0625f08e700


        String messageCode = this.getRandomCode(6);
        String content = "【凯信通】您的验证码是："+messageCode;
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("appkey","975949ae2a36adec5734e0625f08e700");
        paramMap.put("mobile",phone);
        paramMap.put("content",content);

        /**
         * 返回结果的格式
         * {
         *  "code":"10000",
         *  "charge":false,
         *  "remain":0,
         *  "msg":"查询成功",
         *  "result":"<?xml version=\"1.0\" encoding=\"utf-8\" ?>
         *                  <returnsms>\n <returnstatus>Success</returnstatus>\n <message>ok</message>\n <remainpoint>-654079</remainpoint>\n <taskID>81924964</taskID>\n <successCounts>1</successCounts></returnsms>"}
         */
        //String jsonString = HttpClientUtils.doGet("https://way.jd.com/kaixintong/kaixintong",paramMap);
        String jsonString = "{\"code\":\"10000\",\"charge\":false,\"remain\":0,\"msg\":\"查询成功\",\"result\":\"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-654079</remainpoint>\\n <taskID>81924964</taskID>\\n <successCounts>1</successCounts></returnsms>\"}";

        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String code = jsonObject.getString("code");
        if (StringUtils.equals("10000",code)){

            //通信成功获取returnstatus标签中的内容
            String resultXml = jsonObject.getString("result");

            Document document = DocumentHelper.parseText(resultXml);
            Node node = document.selectSingleNode("//returnstatus");
            String returnstatus = node.getText();

            if (StringUtils.equals("Success",returnstatus)){

                //将验证码放入Redis中，保存时间为60秒
                redisService.set(phone,messageCode);

                retMap.put("errorMessage","OK");
                retMap.put("messageCode",messageCode);

            }else{

                retMap.put("errorMessage","发送验证码失败");

            }

        }else{

            retMap.put("errorMessage","通信失败");

        }

        return retMap;
    }

    /**
     * 用户登录
     * @param request
     * @param phone
     * @param loginPassword
     * @param messageCode
     * @return
     */
    @RequestMapping(value = "/user/login")
    @ResponseBody
    public Map<String,Object> login(HttpServletRequest request,
                                    @RequestParam(value = "phone",required = true) String phone,
                                    @RequestParam(value = "loginPassword",required = true) String loginPassword,
                                    @RequestParam(value = "messageCode",required = true) String messageCode){

        Map<String,Object> map = new HashMap<>();

        //验证短信验证码
        String redisMessageCode = (String) redisService.get(phone);
        if (StringUtils.equals(redisMessageCode,messageCode)){

            //用户登录：1.根据用户名和密码查用户2.更新最近登陆时间
            User user = userService.login(phone,loginPassword);
            if (null!=user){

                map.put("errorMessage","OK");
                //用户放进session
                request.getSession().setAttribute("user",user);

            }else{

                map.put("errorMessage","账号或密码错误");

            }

        }else{

            map.put("errorMessage","验证码有误");

        }

        return map;

    }

    //进入用户中心
    @RequestMapping(value = "/loan/myCenter")
    public String myCenter(HttpServletRequest request,Model model){

        User user = (User) request.getSession().getAttribute("user");
        //获取用户可投资金额

        FinanceAccount financeAccount = financeAccountService.queryFinanceAccount(user.getId());
        model.addAttribute("financeAccount",financeAccount);

        //根据UID获取最近投资列表（产品，投资金额，投资时间）：相当于分页
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("uid",user.getId());
        paramMap.put("currentPage",0);
        paramMap.put("pageSize",5);

        List<BidInfo> bidInfoList = bidInfoService.queryBidInfoListByUid(paramMap);

        //根据UID获取最近充值列表
        List<RechargeRecord> rechargeRecordList = rechargeRecordService.queryRechargeRecordListByUid(paramMap);

        //根据UID获取最近收益列表
        List<IncomeRecord> incomeRecordList = incomeRecordService.queryIncomeRecordListByUid(paramMap);

        model.addAttribute("bidInfoList",bidInfoList);
        model.addAttribute("rechargeRecordList",rechargeRecordList);
        model.addAttribute("incomeRecordList",incomeRecordList);

        return "myCenter";
    }


    //生成6位随机验证码
    private String getRandomCode(int count) {

        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for(int i=1;i<=count;i++){

            stringBuffer.append(random.nextInt(10));

        }

        return stringBuffer.toString();

    }

}
