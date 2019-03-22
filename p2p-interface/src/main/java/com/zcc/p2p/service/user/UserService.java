package com.zcc.p2p.service.user;

import com.zcc.p2p.model.user.User;

public interface UserService {
    Long queryUserCount();

    User queryUserByPhone(String phone);

    boolean register(String phone, String loginPassword);

    int modifyUserById(User updateUser);


    User login(String phone, String loginPassword);
}
