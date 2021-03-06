package com.whiteboard.dao;

import com.whiteboard.pojo.Team;
import com.whiteboard.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeamMapper {
    int deleteByPrimaryKey(Integer teamId);

    int insert(Team record);

    Team selectByPrimaryKey(Integer teamId);

    List<Team> selectAll();

    int updateByPrimaryKey(Team record);

    Integer findByTeamName(@Param("team_name") String TeamName);

    List<Team> findByKeywords(@Param("keyword") String keyword);
}