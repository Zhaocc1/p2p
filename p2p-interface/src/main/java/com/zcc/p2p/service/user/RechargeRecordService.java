package com.zcc.p2p.service.user;

import com.zcc.p2p.model.loan.RechargeRecord;
import com.zcc.p2p.model.vo.PaginationVo;

import java.util.List;
import java.util.Map;

public interface RechargeRecordService {

    //List<RechargeRecord> queryRechargeRecordListByUid(Map<String, Object> paramMap);

    int addRechargeRecord(RechargeRecord rechargeRecord);

    boolean recharge(Map<String, Object> paramMap);

    int modifyRechargeStatusByTradeNo(RechargeRecord rechargeRecord);

    List<RechargeRecord> queryRechargeRecordListByUid(Map<String, Object> paramMap);

    PaginationVo<RechargeRecord> queryRechargeRecordListByPage(Map<String, Object> paramMap);

}
