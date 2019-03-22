package com.zcc.p2p.service.user;

import com.zcc.p2p.model.loan.IncomeRecord;
import com.zcc.p2p.model.vo.PaginationVo;

import java.util.List;
import java.util.Map;

public interface IncomeRecordService {

   // List<IncomeRecord> queryIncomeRecordListByUid(Map<String, Object> paramMap);

    void generateIncomePlan();

    void generateIncomeBack();

    List<IncomeRecord> queryIncomeRecordListByUid(Map<String, Object> paramMap);

    PaginationVo<IncomeRecord> queryIncomeRecordListByPage(Map<String, Object> paramMap);
}
