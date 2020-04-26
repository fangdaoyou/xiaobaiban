package com.whiteboard.service;

import com.whiteboard.pojo.Task;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.TaskVO;
import com.whiteboard.vo.UserVO;

public interface ITaskService {
    public ServerResponse createLogic(TaskVO taskVO, UserVO userInfo);
}
