package com.zcc.p2p.mapper.user;


import com.zcc.p2p.model.user.FinanceAccount;

import java.util.Map;

public interface FinanceAccountMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_finance_account
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_finance_account
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int insert(FinanceAccount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_finance_account
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int insertSelective(FinanceAccount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_finance_account
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    FinanceAccount selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_finance_account
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int updateByPrimaryKeySelective(FinanceAccount record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table u_finance_account
     *
     * @mbggenerated Fri Feb 22 12:32:49 CST 2019
     */
    int updateByPrimaryKey(FinanceAccount record);

    //根据UID查financeAccount
    FinanceAccount selectFinanceAccountByUid(Integer uId);

    //投资后更新账户可用金额
    int updateAvailableMoneyByUid(Map<String, Object> paramMap);

    //收益返还后更新用户的账户余额
    int updateAvailableMoneyByIncome(Map<String, Object> paramMap);

    //给用户的账户充值
    int updateAvailableMoneyByRecharge(Map<String, Object> paramMap);
}