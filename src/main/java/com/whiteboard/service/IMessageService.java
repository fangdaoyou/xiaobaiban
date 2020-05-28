package com.whiteboard.service;

import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.TeamMessageVO;
import com.whiteboard.vo.UserVO;

public interface IMessageService {
    ServerResponse createLogic(TeamMessageVO teamMessageVO, UserVO userVO);
    ServerResponse unreadLogic(Integer type, Integer uid);
}
