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

    /**
     * 加入团队
     * @param uid
     * @param teamId
     * @param role
     * @return
     */
    public ServerResponse addLogic(Integer uid, Integer teamId, Integer role);

    /**
     * 团队基本信息
     * @param teamId
     * @return
     */
    public ServerResponse infoLogic(Integer teamId);

    /**
     * 团队成员信息
     * @param teamId
     * @return
     */
    public ServerResponse matesLogic(Integer teamId);

    /**
     * 团队解散
     * @param teamId
     * @return
     */
    public ServerResponse disbandLogic(Integer teamId);
}
