package com.zcc.p2p.service.loan;

import com.zcc.p2p.model.loan.BidInfo;
import com.zcc.p2p.model.vo.BidInfoTop;
import com.zcc.p2p.model.vo.PaginationVo;

import java.util.List;
import java.util.Map;

public interface BidInfoService {
    Double queryBidMoney();

    List<BidInfo> queryBidInfosByLoanId(Integer id);

    List<BidInfo> queryBidInfoListByUid(Map<String, Object> paramMap);

    PaginationVo<BidInfo> queryBidInfoListByPage(Map<String, Object> paramMap);

    boolean invest(Map<String, Object> paramMap);

    List<BidInfoTop> queryBidUserTop();
}
