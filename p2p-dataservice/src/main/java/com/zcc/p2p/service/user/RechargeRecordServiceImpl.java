package com.zcc.p2p.service.user;

import com.zcc.p2p.mapper.loan.RechargeRecordMapper;
import com.zcc.p2p.mapper.user.FinanceAccountMapper;
import com.zcc.p2p.model.loan.RechargeRecord;
import com.zcc.p2p.model.vo.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:zcc
 * @data:2019/3/2 0002
 */
@Service(value = "rechargeRecordServiceImpl")
public class RechargeRecordServiceImpl implements RechargeRecordService {

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    //添加充值记录
    @Override
    public int addRechargeRecord(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.insert(rechargeRecord);
    }

    //给用户充值并更新充值订单状态
    @Override
    public boolean recharge(Map<String, Object> paramMap) {

        boolean flag = false;

        //1.给用户充值（充值订单号，用户id，充值金额）
        int avCount = financeAccountMapper.updateAvailableMoneyByRecharge(paramMap);
        if (avCount > 0) {

            //2.更新充值订单的状态码为1
            RechargeRecord rechargeRecord = new RechargeRecord();
            rechargeRecord.setRechargeNo((String) paramMap.get("out_trade_no"));
            rechargeRecord.setRechargeStatus("1");
            int staCount = rechargeRecordMapper.updateRechargeStatusByNo(rechargeRecord);
            if (staCount > 0){

                flag = true;
            }

        }


        return flag;
    }

    //更新订单状态
    @Override
    public int modifyRechargeStatusByTradeNo(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.updateRechargeStatusByNo(rechargeRecord);
    }

    //根据UID获取最近充值列表
    @Override
    public List<RechargeRecord> queryRechargeRecordListByUid(Map<String, Object> paramMap) {
        return rechargeRecordMapper.selectRechargeRecordListByUid(paramMap);
    }


    //根据UID分页获取最近充值列表
    @Override
    public PaginationVo<RechargeRecord> queryRechargeRecordListByPage(Map<String, Object> paramMap) {

        PaginationVo<RechargeRecord> rechargePaginationVo = new PaginationVo<>();

        //获取list
        List<RechargeRecord> rechargeRecordList = rechargeRecordMapper.selectRechargeRecordListByUid(paramMap);
        rechargePaginationVo.setDataList(rechargeRecordList);

        //获取总条数
        Long totalRows = rechargeRecordMapper.selectRechargeCountByUid(paramMap);
        rechargePaginationVo.setTotalRows(totalRows);

        return rechargePaginationVo;
    }
}
