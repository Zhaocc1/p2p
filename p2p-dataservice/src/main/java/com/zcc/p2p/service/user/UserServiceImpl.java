package com.zcc.p2p.service.user;

import com.zcc.p2p.common.constant.Constant;
import com.zcc.p2p.mapper.user.FinanceAccountMapper;
import com.zcc.p2p.mapper.user.UserMapper;
import com.zcc.p2p.model.user.FinanceAccount;
import com.zcc.p2p.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author:zcc
 * @data:2019/2/23 0023
 */

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    /**
     * 查询用户数量
     * @return
     */
    @Override
    public Long queryUserCount() {

        BoundValueOperations<String, Object> stringObjectBoundValueOperations = redisTemplate.boundValueOps(Constant.USER_COUNT);
        Long userCount = (Long) stringObjectBoundValueOperations.get();

        if (userCount == null) {

            userCount = userMapper.getUserCount();
            stringObjectBoundValueOperations.set(userCount,15, TimeUnit.MINUTES);


        }


        return userCount;
    }

    /**
     * 根据手机号查用户
     * @param phone
     * @return User
     */
    @Override
    public User queryUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    /**
     * 创建User和FinanceAccount
     * @param phone
     * @param loginPassword
     * @return
     */
    @Override
    public boolean register(String phone, String loginPassword) {

        boolean flag = true;

        //新建User
        User user = new User();
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());
        user.setLoginPassword(loginPassword);
        user.setPhone(phone);

        int insertUser = userMapper.insertSelective(user);
        if (insertUser <= 0){
            flag = false;
        }

        //新建FinanceAccount
        FinanceAccount financeAccount = new FinanceAccount();
        financeAccount.setUid(userMapper.selectUserByPhone(phone).getId());
        financeAccount.setAvailableMoney(888.0);
        int insertFin = financeAccountMapper.insertSelective(financeAccount);
        if (insertFin <= 0){

            flag = false;

        }

        return flag;
    }


    /**
     * 根据id更新用户
     * @param updateUser
     * @return
     */
    @Override
    public int modifyUserById(User updateUser) {

        return userMapper.updateByPrimaryKeySelective(updateUser);
    }

    /**
     * 用户登录
     * @param phone
     * @param loginPassword
     * @return
     */
    @Override
    public User login(String phone, String loginPassword) {

        User user = userMapper.selectUserByPhoneAndLoginPassword(phone,loginPassword);

        if (null!=user){

            User updateUser = new User();
            updateUser.setId(user.getId());
            updateUser.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(updateUser);

        }

        return user;
    }


}
