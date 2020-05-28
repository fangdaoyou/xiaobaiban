package com.whiteboard.dao;

import com.whiteboard.pojo.TeamMessage;
import java.util.List;

public interface TeamMessageMapper {
    int deleteByPrimaryKey(Integer messageId);

    int insert(TeamMessage record);

    TeamMessage selectByPrimaryKey(Integer messageId);

    List<TeamMessage> selectAll();

    int updateByPrimaryKey(TeamMessage record);

    List<TeamMessage> findUnread(Integer uid, Integer type);
}