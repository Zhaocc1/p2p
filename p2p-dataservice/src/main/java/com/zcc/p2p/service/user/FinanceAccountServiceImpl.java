package com.zcc.p2p.service.user;

import com.zcc.p2p.mapper.user.FinanceAccountMapper;
import com.zcc.p2p.model.user.FinanceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author:zcc
 * @data:2019/2/25 0025
 */

@Service(value = "financeAccountServiceImpl")
public class FinanceAccountServiceImpl implements FinanceAccountService {

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public FinanceAccount queryFinanceAccount(Integer uId) {

        FinanceAccount financeAccount =  financeAccountMapper.selectFinanceAccountByUid(uId);

        return financeAccount;
    }
}
