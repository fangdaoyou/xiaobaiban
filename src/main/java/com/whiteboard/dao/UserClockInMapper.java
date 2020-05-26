package com.whiteboard.dao;

import com.whiteboard.pojo.UserClockIn;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserClockInMapper {
    int deleteByPrimaryKey(@Param("clockId") Integer clockId, @Param("uid") Integer uid);

    int insert(UserClockIn record);

    UserClockIn selectByPrimaryKey(@Param("clockId") Integer clockId, @Param("uid") Integer uid);

    List<UserClockIn> selectAll();

    int updateByPrimaryKey(UserClockIn record);
}