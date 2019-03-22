package com.zcc.p2p.web;

import com.zcc.p2p.common.constant.Constant;
import com.zcc.p2p.model.loan.LoanInfo;
import com.zcc.p2p.service.loan.BidInfoService;
import com.zcc.p2p.service.loan.LoanInfoService;
import com.zcc.p2p.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:页面加载Controller
 * @author:zcc
 * @data:2019/2/22 0022
 */

@Controller
public class IndexController {


    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidInfoService bidInfoService;

    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, Model model){

        //获取历史年化收益率
        Double historicalAverageRate = loanInfoService.queryHistoricalAverageRate();
        model.addAttribute(Constant.HISTORICAL_AVERTAGE_RATE,historicalAverageRate);


        //获取用户人数
        Long userCount = userService.queryUserCount();
        model.addAttribute(Constant.USER_COUNT,userCount);

        //获取总投资额
       Double bidMoney = bidInfoService.queryBidMoney();
       model.addAttribute(Constant.BID_MONEY,bidMoney);


        //获取产品
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("currentPage",0);

        //新手宝
        paramMap.put("productType",Constant.PRODUCT_TYPE_X);
        paramMap.put("pageSize",1);

        List<LoanInfo> xLoanInfoList = loanInfoService.queryLoanInfoListByType(paramMap);
        model.addAttribute("xLoanInfoList",xLoanInfoList);

        //优选产品
        paramMap.put("productType",Constant.PRODUCT_TYPE_U);
        paramMap.put("pageSize",4);

        List<LoanInfo> uLoanInfoList = loanInfoService.queryLoanInfoListByType(paramMap);
        model.addAttribute("uLoanInfoList",uLoanInfoList);


        //散标产品
        paramMap.put("productType",Constant.PRODUCT_TYPE_S);
        paramMap.put("pageSize",8);

        List<LoanInfo> sLoanInfoList = loanInfoService.queryLoanInfoListByType(paramMap);
        model.addAttribute("sLoanInfoList",sLoanInfoList);


        return "index";

    }


}
