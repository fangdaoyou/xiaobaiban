package com.whiteboard.service;

import com.whiteboard.pojo.Team;
import com.whiteboard.utils.ServerResponse;

public interface ITeamService {
    public ServerResponse createLogic(Team team);
}
