package com.whiteboard.controller;

import com.whiteboard.common.Const;
import com.whiteboard.pojo.User;
import com.whiteboard.service.IUserService;
import com.whiteboard.utils.ServerResponse;
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

}
