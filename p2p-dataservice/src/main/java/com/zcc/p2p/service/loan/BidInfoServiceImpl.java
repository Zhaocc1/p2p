package com.zcc.p2p.service.loan;

import com.zcc.p2p.common.constant.Constant;
import com.zcc.p2p.mapper.loan.BidInfoMapper;
import com.zcc.p2p.mapper.loan.LoanInfoMapper;
import com.zcc.p2p.mapper.user.FinanceAccountMapper;
import com.zcc.p2p.model.loan.BidInfo;
import com.zcc.p2p.model.loan.LoanInfo;
import com.zcc.p2p.model.user.FinanceAccount;
import com.zcc.p2p.model.vo.BidInfoTop;
import com.zcc.p2p.model.vo.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.text.Bidi;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author:zcc
 * @data:2019/2/23 0023
 */

@Service(value = "bidInfoServiceImpl")
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Override
    public Double queryBidMoney() {

        Double bidMoney = (Double) redisTemplate.opsForValue().get(Constant.BID_MONEY);
        if (bidMoney == null) {

            bidMoney = bidInfoMapper.getBidMoney();
            redisTemplate.opsForValue().set(Constant.BID_MONEY,bidMoney,15, TimeUnit.MINUTES);

        }

        return bidMoney;
    }

    @Override
    public List<BidInfo> queryBidInfosByLoanId(Integer id) {
        return bidInfoMapper.selectBidInfosByLoanId(id);
    }

    /**
     * 根据用户id查询前5条投资记录
     * @param paramMap
     * @return
     */
    @Override
    public List<BidInfo> queryBidInfoListByUid(Map<String, Object> paramMap) {
        return bidInfoMapper.selectBidInfoListByPage(paramMap);
    }

    /**
     * 根据用户id分页查询
     * @param paramMap
     * @return
     */
    @Override
    public PaginationVo<BidInfo> queryBidInfoListByPage(Map<String, Object> paramMap) {

        PaginationVo<BidInfo> bidInfoPaginationVo = new PaginationVo<>();
        List<BidInfo> bidInfoList = bidInfoMapper.selectBidInfoListByPage(paramMap);
        Long totalRows = bidInfoMapper.selectBidInfoCountByUid(paramMap);
        bidInfoPaginationVo.setDataList(bidInfoList);
        bidInfoPaginationVo.setTotalRows(totalRows);

        return bidInfoPaginationVo;
    }

    /**
     * 用户投资功能
     * @param paramMap
     * @return
     */
    @Override
    public boolean invest(Map<String, Object> paramMap) {

        boolean flag = false;

        Double bidMoney = (Double) paramMap.get("bidMoney");
        String phone = (String) paramMap.get("phone");
        Integer uid = (Integer) paramMap.get("uid");
        Integer loanId = (Integer) paramMap.get("loanId");

        //获取到投资的产品
        LoanInfo loanInfo =  loanInfoMapper.selectByPrimaryKey(loanId);
        Integer version = loanInfo.getVersion();
        paramMap.put("version",version);

        //更新financeAccount表中的的availableMoney
        int finCount = financeAccountMapper.updateAvailableMoneyByUid(paramMap);
        if (finCount > 0 ){

             //更新loanInfo的leftMoney，判断是否满标，满标则更新满标标识
            int loanCount = loanInfoMapper.updateLeftProductMoneyById(paramMap);
            if (loanCount>0) {

                //增加一条投资记录
                BidInfo bidInfo = new BidInfo();
                bidInfo.setBidMoney(bidMoney);
                bidInfo.setBidStatus(1);
                bidInfo.setBidTime(new Date());
                bidInfo.setLoanId(loanId);
                bidInfo.setUid(uid);
                int bidCount = bidInfoMapper.insert(bidInfo);
                if (bidCount>0){

                    flag = true;

                    //再次查询产品剩余可投资金额
                    Double leftProductMoney = loanInfoMapper.selectLoanInfoById(loanId).getLeftProductMoney();
                    if (leftProductMoney == 0){

                        LoanInfo updateLoanInfo = new LoanInfo();
                        updateLoanInfo.setId(loanId);
                        updateLoanInfo.setProductStatus(1);
                        updateLoanInfo.setProductFullTime(new Date());
                        int updateLoanCount = loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);
                        if (updateLoanCount > 0){

                            flag = false;

                        }

                    }

                    //添加投资记录后，按手机号（即用户）累加投资金额保存到Redis中，生成投资排行榜
                    redisTemplate.opsForZSet().incrementScore(Constant.INVEST_TOP,phone,bidMoney);

                }

            }

        }

        return flag;
    }

    /**
     * 从Redis中查询投资排行榜
     * @return
     */
    @Override
    public List<BidInfoTop> queryBidUserTop() {

        List<BidInfoTop> bidInfoTopList = new LinkedList<>();

        //倒序从Redis中取出前10名
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(Constant.INVEST_TOP, 0, 9);

        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();

        while (iterator.hasNext()){

            ZSetOperations.TypedTuple<Object> next = iterator.next();
            String phone = (String) next.getValue();
            Double score = next.getScore();

            BidInfoTop bidInfoTop = new BidInfoTop();
            bidInfoTop.setPhone(phone);
            bidInfoTop.setScore(score);

            bidInfoTopList.add(bidInfoTop);

        }

        return bidInfoTopList;
    }


}
