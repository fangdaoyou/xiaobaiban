package com.whiteboard.controller;

import com.whiteboard.common.Const;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.exception.WhiteBoardException;
import com.whiteboard.pojo.Team;
import com.whiteboard.pojo.User;
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
        ServerResponse serverResponse = teamService.createLogic(team, userInfo);
        if (serverResponse.isSucess()){//更新session用户信息
            session.setAttribute(Const.CURRENT_USER, userInfo);
        }
        return serverResponse;
    }

    @RequestMapping(value = "team/search")
    public ServerResponse search(String keyword,
                                 @RequestParam(value = "pageNum", defaultValue ="1") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                 String orderBy){
        ServerResponse serverResponse = teamService.searchLogic(keyword, pageNum, pageSize, orderBy);
        return serverResponse;
    }

    @RequestMapping(value = "team/join")
    public ServerResponse add(Integer uid, Integer role, HttpSession session){
        UserVO userInfo = (UserVO)session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = teamService.addLogic(uid, userInfo.getTeamId(), role);
        return serverResponse;
    }

    @RequestMapping(value = "team/info")
    public ServerResponse info(Integer teamId){
        ServerResponse serverResponse = teamService.infoLogic(teamId);
        return serverResponse;
    }

    @RequestMapping(value = "team/mates")
    public ServerResponse mates(Integer teamId){
        ServerResponse serverResponse = teamService.matesLogic(teamId);
        return serverResponse;
    }

    @RequestMapping(value = "team/disband")
    public ServerResponse disband(HttpSession session){
        UserVO opUser = (UserVO) session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse;
        try {
            serverResponse = teamService.disbandLogic(opUser);
        }catch (WhiteBoardException e){
            serverResponse = ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    e.getMsg());
        }

        if (serverResponse.isSucess()){//更新session用户信息
            session.setAttribute(Const.CURRENT_USER, serverResponse.getData());
        }
        return serverResponse;
    }

    @RequestMapping(value = "team/quit")
    public ServerResponse quit(Integer uid, HttpSession session){

        UserVO opUser = (UserVO) session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = teamService.quitLogic(opUser, uid);
        if (serverResponse.isSucess()){//更新session用户信息
            session.setAttribute(Const.CURRENT_USER, opUser);
        }
        return serverResponse;
    }

    @RequestMapping(value = "team/set_role")
    public ServerResponse setRole(Integer uid, Byte role, HttpSession session){
        UserVO opUser = (UserVO) session.getAttribute(Const.CURRENT_USER);
        ServerResponse serverResponse = teamService.setRoleLogic(uid, role, opUser);
        return serverResponse;
    }
}
