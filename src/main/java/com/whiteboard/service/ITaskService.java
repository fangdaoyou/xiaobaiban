package com.whiteboard.service;

import com.whiteboard.pojo.Task;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.TaskVO;
import com.whiteboard.vo.UserVO;

public interface ITaskService {
    public ServerResponse createLogic(TaskVO taskVO, String workerIds, UserVO userInfo);

    public ServerResponse listForTeamLogic(Integer teamId, Integer pageNum, Integer pageSize, String orderBy);

    public ServerResponse listForIndividualLogic(Integer uid, Integer pageNum, Integer pageSize, String orderBy);

    public ServerResponse detailLogic(Integer taskId);

    public ServerResponse adminTaskLogic(UserVO userInfo, Integer pageNum, Integer pageSize, String orderBy);

    public ServerResponse updateLogic(TaskVO taskVO, String workerIds, UserVO userInfo);

    public ServerResponse deleteLogic(Integer taskId, UserVO userInfo);

}
