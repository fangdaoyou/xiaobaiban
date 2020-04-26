package com.whiteboard.controller;

import com.whiteboard.common.Const;
import com.whiteboard.pojo.Task;
import com.whiteboard.service.ITaskService;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.TaskVO;
import com.whiteboard.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/")
public class TaskController {
    @Autowired
    ITaskService taskService;

    @RequestMapping(value = "task/create")
    public ServerResponse create(TaskVO taskVO, HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = taskService.createLogic(taskVO, userInfo);

        return serverResponse;
    }
}
