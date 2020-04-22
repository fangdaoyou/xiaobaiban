package com.whiteboard.controller;

import com.whiteboard.common.Const;
import com.whiteboard.pojo.Team;
import com.whiteboard.service.ITeamService;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping(value = "/portal/")
public class TeamController {

    @Autowired
    ITeamService teamService;

    @RequestMapping(value = "team/create")
    public ServerResponse register(Team team, HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        return  teamService.createLogic(team, userInfo.getUid());
    }

    @RequestMapping(value = "team/search")
    public ServerResponse search(String keyword,
                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                 String orderBy){
        ServerResponse serverResponse = teamService.searchLogic(keyword, pageNum, pageSize, orderBy);
        return serverResponse;
    }

    @RequestMapping(value = "team/add")
    public ServerResponse add(Integer uid, Integer teamId, Integer role){
        ServerResponse serverResponse = teamService.addLogic(uid, teamId, role);
        return serverResponse;
    }
}
