package com.whiteboard.dao;

import com.whiteboard.pojo.ClockInConfig;
import java.util.List;

public interface ClockInConfigMapper {
    int deleteByPrimaryKey(Integer clockId);

    int insert(ClockInConfig record);

    ClockInConfig selectByPrimaryKey(Integer clockId);

    List<ClockInConfig> selectAll();

    int updateByPrimaryKey(ClockInConfig record);

    int updateState(Integer clockId, Integer state);

    List<ClockInConfig> findByTeamId(Integer teamId);
}