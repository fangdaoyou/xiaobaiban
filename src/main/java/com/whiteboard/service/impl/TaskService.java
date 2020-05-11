package com.whiteboard.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whiteboard.common.Const;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.dao.TaskMapper;
import com.whiteboard.dao.TeamMapper;
import com.whiteboard.dao.UserForTaskMapper;
import com.whiteboard.dao.UserMapper;
import com.whiteboard.exception.WhiteBoardException;
import com.whiteboard.pojo.Task;
import com.whiteboard.pojo.Team;
import com.whiteboard.pojo.User;
import com.whiteboard.pojo.UserForTask;
import com.whiteboard.service.ITaskService;
import com.whiteboard.utils.DateUtil;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.TaskVO;
import com.whiteboard.vo.UserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskService implements ITaskService {
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserForTaskMapper userForTaskMapper;
    @Autowired
    TeamMapper teamMapper;

    @Override
    @Transactional(rollbackFor = {WhiteBoardException.class})
    public ServerResponse createLogic(TaskVO taskVO, String workerIds, UserVO userInfo) {
        //参数校验
        if (taskVO == null || StringUtils.isBlank(workerIds)){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        if (userInfo.getTeamId() == Const.DEFAULT_TEAM_ID || userInfo.getRole() == Const.ROLE_USER){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }
        {
            String topic = taskVO.getTopic();
            String description = taskVO.getDescription();
            String ddlTime = taskVO.getDdlTime();
            Integer priority = taskVO.getPriority();

            if (StringUtils.isBlank(topic)) {
                return ServerResponse.createServerResponseByFail(ResponseCode.TOPIC_EMPTY.getCode(),
                        ResponseCode.TOPIC_EMPTY.getMsg());
            }
            if (StringUtils.isBlank(description)) {
                return ServerResponse.createServerResponseByFail(ResponseCode.DESCRIPTION_EMPTY.getCode(),
                        ResponseCode.DESCRIPTION_EMPTY.getMsg());
            }
            if (StringUtils.isBlank(ddlTime)) {
                return ServerResponse.createServerResponseByFail(ResponseCode.DDL_EMPTY.getCode(),
                        ResponseCode.DDL_EMPTY.getMsg());
            }
            if (priority == null) {
                return ServerResponse.createServerResponseByFail(ResponseCode.PRIORITY_EMPTY.getCode(),
                        ResponseCode.PRIORITY_EMPTY.getMsg());
            }
        }
        taskVO.setSponsorId(userInfo.getUid());
        taskVO.setTeamId(userInfo.getTeamId());
        taskVO.setState(Const.TASK_UNFINISHED);
        //设置task的值
        Task task = taskVO2Task(taskVO);
        //插入
        Integer result = taskMapper.insert(task);
        if (result == 0){
            throw new WhiteBoardException("任务创建失败");
        }
        task = taskMapper.selectByPrimaryKey(task.getTaskId());
        if (task == null){
            throw new WhiteBoardException("任务创建失败");
        }

        //分割字符串,查找工作人信息
        String[] ids = workerIds.split(",");
        List<UserVO> userVOList = new ArrayList<>();
        List<UserForTask> userForTaskList = new ArrayList<>();
        for (String each:ids) {
            Integer uid = Integer.parseInt(each);
            User user = userMapper.selectByPrimaryKey(uid);
            if (user == null){
                throw new WhiteBoardException("用户" + each + "不存在");
            }
            if (user.getTeamId() != userInfo.getTeamId()){
                throw new WhiteBoardException("用户不在同一团队");
            }
            UserVO userVO = UserService.convert(user);
            userVOList.add(userVO);

            UserForTask userForTask = new UserForTask();
            userForTask.setUid(uid);
            userForTask.setTaskId(task.getTaskId());
            userForTaskList.add(userForTask);
        }

        result = userForTaskMapper.InsertBatch(userForTaskList);
        if (result == 0){
            throw new WhiteBoardException("更新失败");
        }
        taskVO = task2TaskVO(task);
        taskVO.setWorkerList(userVOList);
        return ServerResponse.createServerResponseBySucess(taskVO);
    }

    @Override
    public ServerResponse listForTeamLogic(Integer teamId, Integer pageNum, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize);

        List<Task> taskList = taskMapper.findByTeamId(teamId);
        List<TaskVO> taskVOList = new ArrayList<>();
        for (Task task:taskList) {
            TaskVO taskVO = task2TaskVO(task);
            taskVOList.add(taskVO);
        }
        PageInfo pageInfo = new PageInfo(taskList);
        pageInfo.setList(taskVOList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }

    @Override
    public ServerResponse listForIndividualLogic(Integer uid, Integer pageNum, Integer pageSize, String orderBy) {
        PageHelper.startPage(pageNum, pageSize);
        List<Task> taskList = taskMapper.findByWorkerId(uid);
        List<TaskVO> taskVOList = new ArrayList<>();
        for (Task task:taskList) {
            TaskVO taskVO = task2TaskVO(task);
            taskVOList.add(taskVO);
        }
        PageInfo pageInfo = new PageInfo(taskList);
        pageInfo.setList(taskVOList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }

    @Override
    public ServerResponse detailLogic(Integer taskId) {
        if (taskId == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        Task task = taskMapper.selectByPrimaryKey(taskId);
        if (task == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TASK_NOT_EXISTS.getCode(),
                    ResponseCode.TASK_NOT_EXISTS.getMsg());
        }
        List<User> userList = userMapper.findByTaskId(taskId);
        List<UserVO> userVOList = new ArrayList<>();
        for (User user:userList) {
            UserVO userVO = UserService.convert(user);
            userVOList.add(userVO);
        }
        TaskVO taskVO = task2TaskVO(task);
        taskVO.setWorkerList(userVOList);
        return ServerResponse.createServerResponseBySucess(taskVO);
    }

    @Override
    public ServerResponse adminTaskLogic(UserVO userInfo, Integer pageNum, Integer pageSize, String orderBy) {
        if (userInfo.getRole() == Const.ROLE_USER || userInfo.getTeamId() == Const.DEFAULT_TEAM_ID){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Task> taskList = taskMapper.findBySponsorId(userInfo.getUid());
        List<TaskVO> taskVOList = new ArrayList<>();
        for (Task task:taskList) {
            TaskVO taskVO = task2TaskVO(task);
            taskVOList.add(taskVO);
        }
        PageInfo pageInfo = new PageInfo(taskList);
        pageInfo.setList(taskVOList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }

    @Override
    @Transactional(rollbackFor = {WhiteBoardException.class})
    public ServerResponse updateLogic(TaskVO taskVO, String workerIds, UserVO userInfo) {
        if (taskVO == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }

        Integer taskId = taskVO.getTaskId();
        Task oldTask = taskMapper.selectByPrimaryKey(taskId);
        if (oldTask == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TASK_NOT_EXISTS.getCode(),
                    ResponseCode.TASK_NOT_EXISTS.getMsg());
        }
        //权限验证
        Integer sponsorId = oldTask.getSponsorId();
        if (!sponsorId.equals(userInfo.getUid())){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }

        Task newTask = taskVO2Task(taskVO);

        //更新任务基本信息
        System.out.println(newTask);
        int result = taskMapper.updateByPrimaryKey(newTask);
        if (result == 0){
            throw new WhiteBoardException("基本信息更新失败");
        }
        newTask = taskMapper.selectByPrimaryKey(taskId);
        System.out.println(newTask);

        List<User> userList = userMapper.findByTaskId(taskId);
        List<UserVO> userVOList = new ArrayList<>();

        //不需要更新任务人员信息

        if (workerIds == null){

            for (User user:userList) {
                UserVO userVO = UserService.convert(user);
                userVOList.add(userVO);
            }
            taskVO = task2TaskVO(newTask);
            taskVO.setWorkerList(userVOList);
            return ServerResponse.createServerResponseBySucess(taskVO);
        }

        //需要更新任务人员信息
        //删除原纪录
        result = userForTaskMapper.deleteByTaskId(taskId);
        List<UserForTask> userForTaskList = new ArrayList<>();
        if (result == 0){
            throw new WhiteBoardException("人员更新失败");
        }

        //插入新纪录
        String[] ids = workerIds.split(",");
        for (String each:ids) {
            Integer uid = Integer.parseInt(each);
            User user = userMapper.selectByPrimaryKey(uid);
            if (user == null){
                throw new WhiteBoardException("用户" + each + "不存在");
            }
            if (user.getTeamId() != userInfo.getTeamId()){
                throw new WhiteBoardException("用户不在同一团队");
            }
            UserVO userVO = UserService.convert(user);
            userVOList.add(userVO);

            UserForTask userForTask = new UserForTask();
            userForTask.setUid(uid);
            userForTask.setTaskId(taskId);
            userForTaskList.add(userForTask);
        }
        result = userForTaskMapper.InsertBatch(userForTaskList);
        if (result == 0){
            throw new WhiteBoardException("更新失败");
        }
        taskVO = task2TaskVO(newTask);
        taskVO.setWorkerList(userVOList);

        return ServerResponse.createServerResponseBySucess(taskVO);
    }

    @Override
    @Transactional(rollbackFor = {WhiteBoardException.class})
    public ServerResponse deleteLogic(Integer taskId, UserVO userInfo) {
        if (taskId == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        Task task = taskMapper.selectByPrimaryKey(taskId);
        if (task == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TASK_NOT_EXISTS.getCode(),
                    ResponseCode.TASK_NOT_EXISTS.getMsg());
        }
        if (userInfo.getUid() != task.getSponsorId() || userInfo.getRole() != Const.ROLE_ADMIN || userInfo.getTeamId() != task.getTeamId()){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }

        int result = userForTaskMapper.deleteByTaskId(taskId);
        if (result == 0){
            throw new WhiteBoardException("删除失败");
        }
        result = taskMapper.deleteByPrimaryKey(taskId);
        if (result == 0){
            throw new WhiteBoardException("删除失败");
        }
        return ServerResponse.createServerResponseBySucess();
    }

    public Task taskVO2Task(TaskVO taskVO){
        Task task = new Task();

        task.setCreateTime(DateUtil.string2Date(taskVO.getCreateTime()));
        task.setFinishTime(DateUtil.string2Date(taskVO.getFinishTime()));
        task.setDdlTime(DateUtil.string2Date(taskVO.getDdlTime()));
        task.setDescription(taskVO.getDescription());
        task.setSponsorId(taskVO.getSponsorId());
        task.setPriority(taskVO.getPriority());
        task.setTeamId(taskVO.getTeamId());
        task.setTaskId(taskVO.getTaskId());
        task.setTopic(taskVO.getTopic());
        task.setState(taskVO.getState());

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
