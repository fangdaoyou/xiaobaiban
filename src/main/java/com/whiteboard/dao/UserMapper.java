package com.whiteboard.dao;

import com.whiteboard.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(User record);

    User selectByPrimaryKey(Integer uid);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    Integer findByUsername(@Param("username") String username);

    Integer findByEmail(@Param("email") String email);

    Integer findByPhone(@Param("phone") String phone);

    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    List<User> findByKeywords(@Param("keyword") String keyword);

    List<User> findByTeamId(@Param("team_id") Integer teamId);

    List<User> findByTaskId(@Param("task_id") Integer taskId);
}