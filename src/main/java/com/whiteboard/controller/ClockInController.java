package com.whiteboard.controller;

import com.whiteboard.common.Const;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.exception.WhiteBoardException;
import com.whiteboard.pojo.UserClockIn;
import com.whiteboard.service.IClockInService;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.ClockInConfigVO;
import com.whiteboard.vo.UserClockInVO;
import com.whiteboard.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/")
public class ClockInController {
    @Autowired
    IClockInService clockInService;

    @RequestMapping(value = "clock_in/create")
    public ServerResponse create(ClockInConfigVO clockInConfigVO, HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = clockInService.createLogic(clockInConfigVO, userInfo);
        return serverResponse;
    }

    @RequestMapping(value = "clock_in/update")
    public ServerResponse update(ClockInConfigVO clockInConfigVO, HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = clockInService.updateLogic(clockInConfigVO, userInfo);
        return serverResponse;
    }

    @RequestMapping(value = "clock_in/clock_in")
    public ServerResponse clockIn(UserClockInVO userClockInVO, HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse;
        try {
            serverResponse = clockInService.clockInLogic(userClockInVO, userInfo);
        }catch (WhiteBoardException e) {
            serverResponse = ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    e.getMsg());
        }
        return serverResponse;
    }

    @RequestMapping(value = "clock_in/list/team")
    public ServerResponse listTeam(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                   String orderBy,
                                   HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = clockInService.listTeamLogic(userInfo, pageNum, pageSize, orderBy);
        return serverResponse;
    }

    @RequestMapping(value = "clock_in/have_clock_in")
    public ServerResponse haveClockIn(Integer clockId,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                      String orderBy,
                                      HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = clockInService.haveClockInLogic(clockId, userInfo, pageNum, pageSize, orderBy);
        return serverResponse;
    }

    @RequestMapping(value = "clock_in/have_not_clock_in")
    public ServerResponse haveNotClockIn(Integer clockId,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                      String orderBy,
                                      HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = clockInService.haveNotClockInLogic(clockId, userInfo, pageNum, pageSize, orderBy);
        return serverResponse;
    }
}
