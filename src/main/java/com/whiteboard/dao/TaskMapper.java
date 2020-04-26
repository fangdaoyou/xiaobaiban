package com.whiteboard.dao;

import com.whiteboard.pojo.Task;
import java.util.List;

public interface TaskMapper {
    int deleteByPrimaryKey(Integer taskId);

    int insert(Task record);

    Task selectByPrimaryKey(Integer taskId);

    List<Task> selectAll();

    int updateByPrimaryKey(Task record);
}