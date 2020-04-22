package com.whiteboard.controller;

import com.whiteboard.pojo.Team;
import com.whiteboard.service.ITeamService;
import com.whiteboard.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/portal/")
public class TeamController {

    @Autowired
    ITeamService teamService;

    @RequestMapping(value = "team/create")
    public ServerResponse register(Team team){
        return  teamService.createLogic(team);
    }

    @RequestMapping(value = "team/search")
    public ServerResponse search(String keyword,
                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                 String orderBy){
        ServerResponse serverResponse = teamService.searchLogic(keyword, pageNum, pageSize, orderBy);
        return serverResponse;
    }
}
