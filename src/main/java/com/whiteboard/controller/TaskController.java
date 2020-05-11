package com.whiteboard.controller;

import com.whiteboard.common.Const;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.exception.WhiteBoardException;
import com.whiteboard.service.ITaskService;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.TaskVO;
import com.whiteboard.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/")
public class TaskController {
    @Autowired
    ITaskService taskService;

    @RequestMapping(value = "task/create")
    public ServerResponse create(TaskVO taskVO, String workerIds, HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse;
        try {
            serverResponse = taskService.createLogic(taskVO, workerIds,userInfo);
        }catch (WhiteBoardException e){
            serverResponse = ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    e.getMsg());
        }


        return serverResponse;
    }

    @RequestMapping(value = "task/list/team")
    public ServerResponse listForTeam(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                      String orderBy,
                                      HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = taskService.listForTeamLogic(userInfo.getTeamId(), pageNum, pageSize, orderBy);

        return serverResponse;
    }

    @RequestMapping(value = "task/list/individual")
    public ServerResponse listForIndividual(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            String orderBy,
                                            HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = taskService.listForIndividualLogic(userInfo.getUid(), pageNum, pageSize, orderBy);
        return serverResponse;
    }

    @RequestMapping(value = "task/detail")
    public ServerResponse detail(Integer taskId){
        ServerResponse serverResponse = taskService.detailLogic(taskId);
        return serverResponse;
    }

    @RequestMapping(value = "task/admin_task_list")
    public ServerResponse adminTask(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                    String orderBy,
                                    HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = taskService.adminTaskLogic(userInfo, pageNum, pageSize, orderBy);
        return serverResponse;
    }

    @RequestMapping(value = "task/update")
    public ServerResponse update(TaskVO taskVO, String workerIds, HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse;
        try {
            serverResponse = taskService.updateLogic(taskVO, workerIds, userInfo);
        }catch (WhiteBoardException e){
            serverResponse = ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    e.getMsg());
        }
        return serverResponse;
    }

    @RequestMapping(value = "task/delete")
    public ServerResponse delete(Integer taskId, HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = taskService.deleteLogic(taskId, userInfo);
        return serverResponse;
    }
}
