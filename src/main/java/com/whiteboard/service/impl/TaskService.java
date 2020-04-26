package com.whiteboard.service.impl;

import com.whiteboard.common.Const;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.dao.TaskMapper;
import com.whiteboard.pojo.Task;
import com.whiteboard.service.ITaskService;
import com.whiteboard.utils.DateUtil;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.TaskVO;
import com.whiteboard.vo.UserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskService implements ITaskService {
    @Autowired
    TaskMapper taskMapper;

    @Override
    public ServerResponse createLogic(TaskVO taskVO, UserVO userInfo) {
        //参数校验
        if (taskVO == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        if (userInfo.getTeamId() == Const.DEFAULT_TEAM_ID){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }
        if (userInfo.getRole() == Const.ROLE_USER){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }
        String topic = taskVO.getTopic();
        String description = taskVO.getDescription();
        String ddlTime = taskVO.getDdlTime();
        Integer priority = taskVO.getPriority();

        if (StringUtils.isBlank(topic)){
            return ServerResponse.createServerResponseByFail(ResponseCode.TOPIC_EMPTY.getCode(),
                    ResponseCode.TOPIC_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(description)){
            return ServerResponse.createServerResponseByFail(ResponseCode.DESCRIPTION_EMPTY.getCode(),
                    ResponseCode.DESCRIPTION_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(ddlTime)){
            return ServerResponse.createServerResponseByFail(ResponseCode.DDL_EMPTY.getCode(),
                    ResponseCode.DDL_EMPTY.getMsg());
        }
        if (priority == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIORITY_EMPTY.getCode(),
                    ResponseCode.PRIORITY_EMPTY.getMsg());
        }
        taskVO.setSponsorId(userInfo.getUid());
        taskVO.setTeamId(userInfo.getTeamId());
        //设置task的值
        Task task = taskVO2Task(taskVO);
        //插入
        Integer result = taskMapper.insert(task);
        if (result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    ResponseCode.UPDATE_ERROR.getMsg());
        }
        task = taskMapper.selectByPrimaryKey(task.getTaskId());
        if (task == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    ResponseCode.UPDATE_ERROR.getMsg());
        }
        taskVO = task2TaskVO(task);
        return ServerResponse.createServerResponseBySucess(taskVO);
    }

    public Task taskVO2Task(TaskVO taskVO){
        Task task = new Task();
        task.setDdlTime(DateUtil.string2Date(taskVO.getDdlTime()));
        task.setTopic(taskVO.getTopic());
        task.setState(taskVO.getState());
        task.setDescription(taskVO.getDescription());
        task.setSponsorId(taskVO.getSponsorId());
        task.setTeamId(taskVO.getTeamId());
        task.setState(Const.TASK_UNFINISHED);
        task.setPriority(taskVO.getPriority());
        return task;
    }

    public TaskVO task2TaskVO(Task task){
        TaskVO taskVO = new TaskVO();

        taskVO.setCreateTime(DateUtil.date2String(task.getCreateTime()));
        taskVO.setFinishTime(DateUtil.date2String(task.getFinishTime()));
        taskVO.setDdlTime(DateUtil.date2String(task.getDdlTime()));
        taskVO.setDescription(task.getDescription());
        taskVO.setSponsorId(task.getSponsorId());
        taskVO.setPriority(task.getPriority());
        taskVO.setTeamId(task.getTeamId());
        taskVO.setTaskId(task.getTaskId());
        taskVO.setTopic(task.getTopic());
        taskVO.setState(task.getState());

        return taskVO;
    }
}
