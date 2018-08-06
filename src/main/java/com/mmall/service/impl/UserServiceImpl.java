package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        //校验用户是否存在
        int result = userMapper.checkUsername(username);
        if (result < 1) {
            return ServerResponse.createByErrorMsg("用戶不存在");
        }
        // todo 密码MD5加密
        String passwordMD5 = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectByLogin(username, passwordMD5);
        if (user == null) {
            return ServerResponse.createByErrorMsg("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccessMsgAndData("登录成功",user);
    }

    @Override
    public ServerResponse<User> register(User user){
        return null;
    }


    public ServerResponse<User> checkValid(String str,String type){
        return null;
    }

    public ServerResponse isAdmin(User user){
        if(user!=null &&user.getRole()== Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }


}
