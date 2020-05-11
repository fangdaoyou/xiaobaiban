package com.whiteboard.dao;

import com.whiteboard.pojo.UserForTask;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserForTaskMapper {
    int deleteByPrimaryKey(@Param("taskId") Integer taskId, @Param("uid") Integer uid);

    int deleteByTaskId(@Param("taskId") Integer taskId);

    int insert(UserForTask record);

    List<UserForTask> selectAll();

    List<UserForTask> findByUid(Integer uid);

    int InsertBatch(@Param("recordList") List<UserForTask> record);

}