package com.whiteboard.controller;

import com.whiteboard.common.Const;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.exception.WhiteBoardException;
import com.whiteboard.service.IMessageService;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.TeamMessageVO;
import com.whiteboard.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/")
public class MessageController {
    @Autowired
    IMessageService messageService;

    @RequestMapping(value = "message/team/create")
    public ServerResponse create(TeamMessageVO teamMessageVO, HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse;
        try {
            serverResponse = messageService.createLogic(teamMessageVO, userInfo);
        }catch (WhiteBoardException e) {
            serverResponse = ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    e.getMsg());
        }
        return serverResponse;
    }

    @RequestMapping(value = "message/team/unread")
    public ServerResponse unread(Integer type, HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse;
        try {
            serverResponse = messageService.unreadLogic(type, userInfo.getUid());
        }catch (WhiteBoardException e) {
            serverResponse = ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    e.getMsg());
        }
        return serverResponse;
    }
}
