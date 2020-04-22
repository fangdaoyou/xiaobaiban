package com.whiteboard.service;

import com.whiteboard.pojo.Team;
import com.whiteboard.utils.ServerResponse;
import org.springframework.web.servlet.mvc.ServletWrappingController;

public interface ITeamService {
    public ServerResponse createLogic(Team team, Integer uid);

    /**
     * 查询团队
     * @param keyword
     * @return
     */
    public ServerResponse searchLogic(String keyword, Integer pageNum, Integer pageSize, String orderBy);

    public ServerResponse addLogic(Integer uid, Integer teamId, Integer role);
}
