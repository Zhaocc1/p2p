package com.zcc.p2p.service.loan;

import com.zcc.p2p.model.loan.LoanInfo;
import com.zcc.p2p.model.vo.PaginationVo;

import java.util.List;
import java.util.Map;


public interface LoanInfoService {

    /**
     * 获取历史年化平均收益率
     */
    Double queryHistoricalAverageRate();

    /**
     * 查询Loan列表
     * @param paramMap
     * @return
     */
    List<LoanInfo> queryLoanInfoListByType(Map<String, Object> paramMap);

    /**
     * 查询更多Loan信息
     * @param paramMap
     * @return
     */
    PaginationVo<LoanInfo> queryLoanInfoListByPage(Map<String, Object> paramMap);

    /**
     * 根据id值查单条loanInfo
     * @param id
     * @return
     */
    LoanInfo queryLoanInfoById(Integer id);


}
