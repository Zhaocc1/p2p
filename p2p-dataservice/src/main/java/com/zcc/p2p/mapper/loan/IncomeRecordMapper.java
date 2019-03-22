package com.zcc.p2p.mapper.loan;


import com.zcc.p2p.model.loan.IncomeRecord;

import java.util.List;
import java.util.Map;

public interface IncomeRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int insert(IncomeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int insertSelective(IncomeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    IncomeRecord selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int updateByPrimaryKeySelective(IncomeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table b_income_record
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int updateByPrimaryKey(IncomeRecord record);

    //根据收益记录状态值（0表示可返还）和返还时间（当前）查询可返还的记录
    List<IncomeRecord> selectIncomeRecordsByStatusAndBackTime();

    List<IncomeRecord> selectIncomeRecordListByUid(Map<String, Object> paramMap);

    Long selectIncomeRecordCountByUid(Map<String, Object> paramMap);
}