package com.whiteboard.service;

import com.whiteboard.pojo.UserClockIn;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.ClockInConfigVO;
import com.whiteboard.vo.UserClockInVO;
import com.whiteboard.vo.UserVO;

public interface IClockInService {
    public ServerResponse createLogic(ClockInConfigVO clockInConfigVO, UserVO userInfo);
    public ServerResponse updateLogic(ClockInConfigVO clockInConfigVO, UserVO userInfo);
    public ServerResponse clockInLogic(UserClockInVO userClockInVO, UserVO userInfo);
    public ServerResponse listTeamLogic(UserVO userInfo, Integer pageNum, Integer pageSize, String orderBy);
    public ServerResponse haveClockInLogic(Integer clockId, UserVO userInfo, Integer pageNum, Integer pageSize, String orderBy);
    public ServerResponse haveNotClockInLogic(Integer clockId, UserVO userInfo, Integer pageNum, Integer pageSize, String orderBy);
}
