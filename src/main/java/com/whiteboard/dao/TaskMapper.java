package com.whiteboard.dao;

import com.whiteboard.pojo.Task;
import com.whiteboard.pojo.UserForTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskMapper {
    int deleteByPrimaryKey(Integer taskId);

    int insert(Task record);

    Task selectByPrimaryKey(Integer taskId);

    List<Task> selectAll();

    int updateByPrimaryKey(Task record);

    List<Task> findByTeamId(Integer teamId);

    List<Task> findByWorkerId(Integer uid);

    List<Task> findBySponsorId(Integer uid);
}