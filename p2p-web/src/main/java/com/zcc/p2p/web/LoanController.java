package com.zcc.p2p.web;

import com.zcc.p2p.model.loan.BidInfo;
import com.zcc.p2p.model.loan.IncomeRecord;
import com.zcc.p2p.model.loan.LoanInfo;
import com.zcc.p2p.model.loan.RechargeRecord;
import com.zcc.p2p.model.user.FinanceAccount;
import com.zcc.p2p.model.user.User;
import com.zcc.p2p.model.vo.BidInfoTop;
import com.zcc.p2p.model.vo.PaginationVo;
import com.zcc.p2p.service.loan.BidInfoService;
import com.zcc.p2p.service.loan.LoanInfoService;
import com.zcc.p2p.service.user.FinanceAccountService;
import com.zcc.p2p.service.user.IncomeRecordService;
import com.zcc.p2p.service.user.RechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:Loan
 * @author:zcc
 * @data:2019/2/23 0023
 */

@Controller
public class LoanController {

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private BidInfoService bidInfoService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private RechargeRecordService rechargeRecordService;

    @Autowired
    private IncomeRecordService incomeRecordService;


    /**
     * 分页展示产品
     * @param request
     * @param model
     * @param productType
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/loan/loan")
    public String loan(HttpServletRequest request, Model model,
                       @RequestParam(name = "ptype" ,required = false) Integer productType,
                       @RequestParam( name = "currentPage",required = false) Integer currentPage){
        System.out.print("进入loan方法");
        if (currentPage == null) {
            currentPage = 1;
        }

        Map<String,Object> paramMap = new HashMap<>();

        if (productType != null) {
            paramMap.put("productType",productType);
        }

        int pageSize = 9;
        paramMap.put("pageSize",pageSize);
        paramMap.put("currentPage",(currentPage-1)*pageSize);
        PaginationVo<LoanInfo> loanInfoVo= loanInfoService.queryLoanInfoListByPage(paramMap);

        Long totalRows = loanInfoVo.getTotalRows();
        List<LoanInfo> loanInfoList = loanInfoVo.getDataList();

        int temp = totalRows.intValue() % pageSize;
        int totalPage = totalRows.intValue() / pageSize;
        if (temp != 0){
            totalPage += 1;
        }


        model.addAttribute("loanInfoList",loanInfoList);
        model.addAttribute("totalRows",totalRows);
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("currentPage",currentPage);
        if (productType != null) {
            model.addAttribute("productType",productType);
        }

        //loan.jsp中的投资排行榜
        List<BidInfoTop> bidInfoTopList = bidInfoService.queryBidUserTop();
        model.addAttribute("bidInfoTopList",bidInfoTopList);

        return "loan/loan";
    }

    /**
     * 显示该产品下的投资记录
     * @param request
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "/loan/loanInfo")
    public String loanInfo(HttpServletRequest request,Model model,
                           @RequestParam(value = "id",required = true) Integer id){

        System.out.print("进入loanInfo方法");
        //获取loan详细信息
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(id);
        //获取该loan下的投资记录
        List<BidInfo> bidInfoList = bidInfoService.queryBidInfosByLoanId(id);

        //获取用户的账户
        User user = (User) request.getSession().getAttribute("user");
        if (null!=user){

            FinanceAccount financeAccount = financeAccountService.queryFinanceAccount(user.getId());
            model.addAttribute("financeAccount",financeAccount);

        }

        model.addAttribute("loanInfo",loanInfo);
        model.addAttribute("bidInfoList",bidInfoList);

        return "loan/loanInfo";
    }


    /**
     * 分页查询用户投资记录，每页10条
     * @param request
     * @param model
     * @param currentPage
     * @return
     */
    @RequestMapping(value="/loan/myInvest")
    public String myInvest(HttpServletRequest request, Model model,
                           @RequestParam (value = "currentPage" ,required = false) Integer currentPage){

        User user = (User) request.getSession().getAttribute("user");

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("uid",user.getId());
        if (null == currentPage){
            currentPage = 1;
        }

        int pageSize =10;
        paramMap.put("currentPage",(currentPage-1)*pageSize);
        paramMap.put("pageSize",pageSize);

        //查询分页数据
        PaginationVo<BidInfo> paginationVo = (PaginationVo<BidInfo>) bidInfoService.queryBidInfoListByPage(paramMap);

        //分页需要返回：bidInfoList、totalRows、currentPage、totalPage
        List<BidInfo> bidInfoList = paginationVo.getDataList();
        Long totalRows = paginationVo.getTotalRows();
        int totalPage = (int) (totalRows / 10);
        if (totalRows % 10 != 0 ){

            totalPage += 1;
        }

        model.addAttribute("bidInfoList",bidInfoList);
        model.addAttribute("totalRows",totalRows);
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("currentPage",currentPage);


        return "myInvest";

    }

    //分页展示用户的充值记录
    @RequestMapping(value = "/loan/myRecharge")
    public String myRecharge(HttpServletRequest request,Model model,
                             @RequestParam(value = "currentPage" ,required = false) Integer currentPage){

        Map<String,Object> paramMap = new HashMap<>();
        if (null == currentPage){
            currentPage = 1;
        }

        int pageSize = 10;
        paramMap.put("pageSize",pageSize);
        paramMap.put("currentPage",(currentPage-1)*pageSize);

        User user = (User) request.getSession().getAttribute("user");
        paramMap.put("uid",user.getId());

        PaginationVo<RechargeRecord> rechargePaginationVo = rechargeRecordService.queryRechargeRecordListByPage(paramMap);

        Long totalRows = rechargePaginationVo.getTotalRows();
        List<RechargeRecord> rechargeRecordList = rechargePaginationVo.getDataList();
        Long totalPage = totalRows / pageSize;
        if (totalRows % pageSize != 0){

            totalPage += 1;

        }

        model.addAttribute("totalRows",totalRows);
        model.addAttribute("rechargeRecordList",rechargeRecordList);
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("currentPage",currentPage);

        return "myRecharge";
    }


    @RequestMapping(value = "/loan/myIncome")
    public String myIncome(HttpServletRequest request,Model model,
                           @RequestParam(value = "currentPage",required = false)Integer currentPage){

        Map<String,Object> paramMap = new HashMap<>();
        if (null == currentPage){

            currentPage = 1;
        }
        int pageSize = 10;
        User user = (User) request.getSession().getAttribute("user");

        paramMap.put("uid",user.getId());
        paramMap.put("currentPage",(currentPage-1)*pageSize);
        paramMap.put("pageSize",pageSize);

        PaginationVo<IncomeRecord> incomeRecordPaginationVo = incomeRecordService.queryIncomeRecordListByPage(paramMap);

        Long totalRows = incomeRecordPaginationVo.getTotalRows();
        List<IncomeRecord> incomeRecordList = incomeRecordPaginationVo.getDataList();
        Long totalPage = totalRows / pageSize;

        if (totalRows%pageSize != 0){

            totalPage += 1;
        }

        model.addAttribute("incomeRecordList",incomeRecordList);
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("currentPage",currentPage);
        model.addAttribute("totalRows",totalRows);

        return "myIncome";

    }
}
