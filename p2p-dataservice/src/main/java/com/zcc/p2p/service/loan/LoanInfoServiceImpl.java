package com.zcc.p2p.service.loan;

import com.zcc.p2p.common.constant.Constant;
import com.zcc.p2p.mapper.loan.LoanInfoMapper;
import com.zcc.p2p.model.loan.LoanInfo;
import com.zcc.p2p.model.vo.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author:zcc
 * @data:2019/2/22 0022
 */

@Service(value = "loanInfoServiceImpl")
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Double queryHistoricalAverageRate() {

        //Double historicalAverageRate =  loanInfoMapper.getHistoricalAverageRate();
        Double historicalAverageRate = (Double) redisTemplate.opsForValue().get(Constant.HISTORICAL_AVERTAGE_RATE);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        if (historicalAverageRate == null) {

            historicalAverageRate =  loanInfoMapper.getHistoricalAverageRate();
            redisTemplate.opsForValue().set(Constant.HISTORICAL_AVERTAGE_RATE,historicalAverageRate,15, TimeUnit.MINUTES);

        }

        return historicalAverageRate;
    }

    @Override
    public List<LoanInfo> queryLoanInfoListByType(Map<String, Object> paramMap) {

        List<LoanInfo> loanInfoList = loanInfoMapper.getLoanInfoListByPage(paramMap);

        return loanInfoList;
    }

    @Override
    public PaginationVo<LoanInfo> queryLoanInfoListByPage(Map<String, Object> paramMap) {

        PaginationVo<LoanInfo> loanInfoPaginationVo = new PaginationVo<>();

        List<LoanInfo> loanInfoList = loanInfoMapper.getLoanInfoListByPage(paramMap);
        Long totalRows = loanInfoMapper.getTotalRows(paramMap);

        loanInfoPaginationVo.setDataList(loanInfoList);
        loanInfoPaginationVo.setTotalRows(totalRows);

        return loanInfoPaginationVo;
    }

    @Override
    public LoanInfo queryLoanInfoById(Integer id) {
        return loanInfoMapper.selectLoanInfoById(id);
    }






}
