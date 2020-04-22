package com.whiteboard.service;

import com.whiteboard.pojo.Team;
import com.whiteboard.utils.ServerResponse;

public interface ITeamService {
    public ServerResponse createLogic(Team team);

    /**
     * 查询团队
     * @param keyword
     * @return
     */
    public ServerResponse searchLogic(String keyword, Integer pageNum, Integer pageSize, String orderBy);
}
