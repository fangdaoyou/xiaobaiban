package com.whiteboard.dao;

import com.whiteboard.pojo.ReadTeamMessage;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReadTeamMessageMapper {
    int deleteByPrimaryKey(@Param("messageId") Integer messageId, @Param("uid") Integer uid);

    int insert(ReadTeamMessage record);

    ReadTeamMessage selectByPrimaryKey(@Param("messageId") Integer messageId, @Param("uid") Integer uid);

    List<ReadTeamMessage> selectAll();

    int updateByPrimaryKey(ReadTeamMessage record);
}