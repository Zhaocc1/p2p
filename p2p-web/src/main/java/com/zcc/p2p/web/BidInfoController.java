package com.zcc.p2p.web;

import com.zcc.p2p.model.user.User;
import com.zcc.p2p.service.loan.BidInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:投资相关
 * @author:zcc
 * @data:2019/2/28 0028
 */
@Controller
public class BidInfoController {

    @Autowired
    private BidInfoService bidInfoService;

    /**
     * 用户投资
     * @param request
     * @param bidMoney
     * @param loanId
     * @return 投资是否成功标识："OK"/其他
     */
    @RequestMapping(value = "/loan/invest")
    @ResponseBody
    public String invest(HttpServletRequest request,
                         @RequestParam(value = "bidMoney",required = true) Double bidMoney,
                         @RequestParam(value = "loanId",required = true) Integer loanId){

        System.out.println("进入invest方法");
        String message = "";
        Map<String,Object> paramMap = new HashMap<>();

        User user = (User) request.getSession().getAttribute("user");
        paramMap.put("bidMoney",bidMoney);
        paramMap.put("loanId",loanId);
        paramMap.put("uid",user.getId());
        //phone为实现投资排行榜功能准备的参数
        paramMap.put("phone",user.getPhone());


        //增加一条投资记录
        //更新loanInfo的leftMoney，判断是否满标，满标则更新满标标识
        //更新用户的availableMoney
        boolean flag = bidInfoService.invest(paramMap);
        System.out.println("000000000");


        if (flag){

            message = "OK";

        }

        return message;

    }

}
