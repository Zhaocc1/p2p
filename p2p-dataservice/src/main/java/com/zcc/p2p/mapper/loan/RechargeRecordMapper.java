package com.zcc.p2p.mapper.loan;


import com.zcc.p2p.model.loan.RechargeRecord;

import java.util.List;
import java.util.Map;

public interface RechargeRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int insert(RechargeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int insertSelective(RechargeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    RechargeRecord selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int updateByPrimaryKeySelective(RechargeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_recharge_record
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int updateByPrimaryKey(RechargeRecord record);

    //更新充值订单状态
    int updateRechargeStatusByNo(RechargeRecord rechargeRecord);

    //根据UID获取最近充值记录
    List<RechargeRecord> selectRechargeRecordListByUid(Map<String, Object> paramMap);

    //获取分页需要的totalRows
    Long selectRechargeCountByUid(Map<String, Object> paramMap);
}