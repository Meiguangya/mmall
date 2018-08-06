package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import org.springframework.stereotype.Service;

/**
 * 用户服务层
 *
 */
public interface IUserService {

    public ServerResponse<User> login(String username,String password);

    public ServerResponse<User> register(User user);

    public ServerResponse isAdmin(User user);
}
