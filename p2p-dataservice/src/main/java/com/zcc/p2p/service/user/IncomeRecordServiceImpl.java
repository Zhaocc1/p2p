package com.zcc.p2p.service.user;

import com.zcc.p2p.common.constant.Constant;
import com.zcc.p2p.common.utils.DateUtil;
import com.zcc.p2p.mapper.loan.BidInfoMapper;
import com.zcc.p2p.mapper.loan.IncomeRecordMapper;
import com.zcc.p2p.mapper.loan.LoanInfoMapper;
import com.zcc.p2p.mapper.user.FinanceAccountMapper;
import com.zcc.p2p.model.loan.BidInfo;
import com.zcc.p2p.model.loan.IncomeRecord;
import com.zcc.p2p.model.loan.LoanInfo;
import com.zcc.p2p.model.vo.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author:zcc
 * @data:2019/3/1 0001
 */
@Service(value = "incomeRecordServiceImpl")
public class IncomeRecordServiceImpl implements IncomeRecordService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private IncomeRecordMapper incomeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;


    @Override
    public void generateIncomePlan() {

        //获取已满标产品list（product_status为1）
        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoListByProductStatus(1);

        //遍历循环满标产品list，得到每一个满标产品
        for(LoanInfo loanInfo : loanInfoList){

            //查询每一个满标产品下的投资记录list
            List<BidInfo> bidInfoList = bidInfoMapper.selectBidInfoListByLoanId(loanInfo.getId());

            //遍历投资记录list，为每一条投资记录生成一条投资收益记录
            for(BidInfo bidInfo:bidInfoList){

                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setBidId(bidInfo.getId());
                incomeRecord.setBidMoney(bidInfo.getBidMoney());
                incomeRecord.setLoanId(loanInfo.getId());
                incomeRecord.setUid(bidInfo.getUid());
                //状态为0表示收益未返还
                incomeRecord.setIncomeStatus("0");

                //1.收益时间：满标时间+投资周期
                Date incomeDate = null;

                //2.收益金额：利息
                Double incomeMoney = null;

                //按产品类型计算收益时间和收益金额，新手宝：天
                if (Constant.PRODUCT_TYPE_X == loanInfo.getProductType()){

                    incomeDate = DateUtil.getDateAfterDays(loanInfo.getProductFullTime(),loanInfo.getCycle());
                    incomeMoney = bidInfo.getBidMoney() * (loanInfo.getRate() / 100 /365) * loanInfo.getCycle();

                    //按产品类型计算收益时间和收益金额：单位月
                }else{

                    incomeDate = DateUtil.getDateAfterMonths(loanInfo.getProductFullTime(),loanInfo.getCycle());
                    incomeMoney = bidInfo.getBidMoney() * (loanInfo.getRate() / 100 / 12) * loanInfo.getCycle();

                }

                //收益金额保留两位小数
                incomeMoney = Math.round(incomeMoney*Math.pow(10,2)) / Math.pow(10,2);

                incomeRecord.setIncomeMoney(incomeMoney);
                incomeRecord.setIncomeDate(incomeDate);

                incomeRecordMapper.insertSelective(incomeRecord);

            }

            //更新产品的product_status为2：已满标且生成受益计划
            LoanInfo updateLoanInfo = new LoanInfo();
            updateLoanInfo.setId(loanInfo.getId());
            updateLoanInfo.setProductStatus(2);
            loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);

        }

    }

    /**
     * 到期收益返还
     */
    @Override
    public void generateIncomeBack() {

        //查询收益状态为0（未返还）且收益时间为当前的incomeRecordList
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectIncomeRecordsByStatusAndBackTime();

        //遍历到期的收益记录
        for(IncomeRecord incomeRecord:incomeRecordList){

            //返还收益：计算账户金额（本金+bidMoney+income）
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("uid",incomeRecord.getUid());
            paramMap.put("bidMoney",incomeRecord.getBidMoney());
            paramMap.put("income",incomeRecord.getIncomeMoney());

            int count = financeAccountMapper.updateAvailableMoneyByIncome(paramMap);
            if (count>0){

                //更新收益状态为1（已返还）
                IncomeRecord updateIncomeRecord = new IncomeRecord();
                updateIncomeRecord.setId(incomeRecord.getId());
                updateIncomeRecord.setIncomeStatus("1");
                incomeRecordMapper.updateByPrimaryKeySelective(updateIncomeRecord);

            }

        }

    }

    //根据用户id分页查询incomeRecordList
    @Override
    public List<IncomeRecord> queryIncomeRecordListByUid(Map<String, Object> paramMap) {
        return incomeRecordMapper.selectIncomeRecordListByUid(paramMap);
    }

    //根据UID分页查询incomeRecord，返回VO
    @Override
    public PaginationVo<IncomeRecord> queryIncomeRecordListByPage(Map<String, Object> paramMap) {

        PaginationVo<IncomeRecord> incomeRecordPaginationVo = new PaginationVo<>();
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectIncomeRecordListByUid(paramMap);

        Long totalRows = incomeRecordMapper.selectIncomeRecordCountByUid(paramMap);

        incomeRecordPaginationVo.setTotalRows(totalRows);
        incomeRecordPaginationVo.setDataList(incomeRecordList);


        return incomeRecordPaginationVo;
    }
}
