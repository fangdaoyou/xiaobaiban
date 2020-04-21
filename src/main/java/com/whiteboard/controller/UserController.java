package com.whiteboard.controller;

import com.whiteboard.common.Const;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.pojo.User;
import com.whiteboard.service.IUserService;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/")
public class UserController {

    @Autowired
    IUserService userService;

    @RequestMapping(value = "user/login")
    public ServerResponse login(String username, String password, HttpSession session){
        ServerResponse serverResponse = userService.loginLogic(username, password);

        if (serverResponse.isSucess()){
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }

    @RequestMapping(value = "user/register")
    public ServerResponse register(User user){
        return  userService.registerLogic(user);
    }

    @RequestMapping(value = "user/update")
    public ServerResponse updateUser(User user, HttpSession session){

        //step1:判断用户是否登录
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
//        if (userInfo == null){
//            return ServerResponse.createServerResponseByFail(ResponseCode.NEED_LOGIN.getCode(),
//                    ResponseCode.NEED_LOGIN.getMsg());
//        }
        if (user == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        user.setUid(userInfo.getUid());

        ServerResponse serverResponse = userService.updateLogic(user);
        if (serverResponse.isSucess()){//更新session用户信息
            session.setAttribute(Const.CURRENT_USER, serverResponse.getData());
        }
        return serverResponse;
    }

}
