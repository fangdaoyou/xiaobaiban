package com.whiteboard.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whiteboard.common.Const;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.dao.TeamMapper;
import com.whiteboard.dao.UserMapper;
import com.whiteboard.exception.WhiteBoardException;
import com.whiteboard.pojo.Team;
import com.whiteboard.pojo.User;
import com.whiteboard.service.ITeamService;
import com.whiteboard.utils.DateUtil;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.TeamVO;
import com.whiteboard.vo.UserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService implements ITeamService {
    @Autowired
    TeamMapper teamMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public ServerResponse createLogic(Team team, UserVO userInfo) {
        //数据校验
        if (team == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        if (team.getTeamName() == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TEAM_NAME_EMPTY.getCode(),
                    ResponseCode.TEAM_NAME_EMPTY.getMsg());
        }
        //队名是否被占用
        String teamName = team.getTeamName();
        Integer count = teamMapper.findByTeamName(teamName);
        if (count > 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.TEAM_NAME_EXISTS.getCode(),
                    ResponseCode.TEAM_NAME_EXISTS.getMsg());
        }
        User user = userMapper.selectByPrimaryKey(userInfo.getUid());
        //用户是否已经在团队中
        if (user.getTeamId() != Const.DEFAULT_TEAM_ID){
            return ServerResponse.createServerResponseByFail(ResponseCode.USER_IS_IN_TEAM.getCode(),
                    ResponseCode.USER_IS_IN_TEAM.getMsg());
        }
        //插入team
        team.setMemberCount(1);
        Integer result = teamMapper.insert(team);
        if (result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.REGISTER_ERROR.getCode(),
                    ResponseCode.REGISTER_ERROR.getMsg());
        }
        Team newTeam = teamMapper.selectByPrimaryKey(team.getTeamId());
        TeamVO teamVO = convert(newTeam);
        //修改user的权限
        user.setTeamId(team.getTeamId());
        user.setRole((byte)Const.ROLE_ADMIN);
        userInfo.setTeamId(team.getTeamId());
        userInfo.setRole((byte)Const.ROLE_ADMIN);
        result = userMapper.updateByPrimaryKey(user);
        if (result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.REGISTER_ERROR.getCode(),
                    ResponseCode.REGISTER_ERROR.getMsg());
        }
        return ServerResponse.createServerResponseBySucess(teamVO);
    }

    @Override
    public ServerResponse searchLogic(String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword)){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Team> teamList = teamMapper.findByKeywords("%" + keyword + "%");
        List<TeamVO> teamVOList = new ArrayList<>();
        for (Team team:teamList) {
            teamVOList.add(convert(team));
        }
        PageInfo pageInfo = new PageInfo(teamList);
        pageInfo.setList(teamVOList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }

    @Override
    public ServerResponse addLogic(Integer uid, Integer teamId, Integer role) {
        if (uid == null || teamId == null || role == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        if (role != Const.ROLE_ADMIN && role != Const.ROLE_USER){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_ERROR.getCode(),
                    ResponseCode.PARAM_ERROR.getMsg());
        }
        Team team = teamMapper.selectByPrimaryKey(teamId);
        //团队是否存在
        if (team == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TEAM_NOT_EXISTS.getCode(),
                    ResponseCode.TEAM_NOT_EXISTS.getMsg());
        }
        User user = userMapper.selectByPrimaryKey(uid);
        //用户是否存在
        if (user == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EXISTS.getCode(),
                    ResponseCode.USERNAME_NOT_EXISTS.getMsg());
        }
        //用户是否已经在团队中
        if (user.getTeamId() != Const.DEFAULT_TEAM_ID){
            return ServerResponse.createServerResponseByFail(ResponseCode.USER_IS_IN_TEAM.getCode(),
                    ResponseCode.USER_IS_IN_TEAM.getMsg());
        }
        //用户加入
        user.setTeamId(teamId);
        int ro = role;
        user.setRole((byte) ro);
        int result = userMapper.updateByPrimaryKey(user);
        if (result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.JOIN_TEAM_ERROR.getCode(),
                    ResponseCode.JOIN_TEAM_ERROR.getMsg());
        }
        //团队人数+1
        team.setMemberCount(team.getMemberCount()+1);
        result = teamMapper.updateByPrimaryKey(team);
        if (result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    ResponseCode.UPDATE_ERROR.getMsg());
        }
        return ServerResponse.createServerResponseBySucess();
    }

    @Override
    public ServerResponse infoLogic(Integer teamId) {
        if (teamId == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        Team team = teamMapper.selectByPrimaryKey(teamId);
        if (team == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TEAM_NOT_EXISTS.getCode(),
                    ResponseCode.TEAM_NOT_EXISTS.getMsg());
        }

        TeamVO teamVO = convert(team);

        return ServerResponse.createServerResponseBySucess(teamVO);
    }

    @Override
    public ServerResponse matesLogic(Integer teamId) {
        if (teamId == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        Team team = teamMapper.selectByPrimaryKey(teamId);
        if (team == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TEAM_NOT_EXISTS.getCode(),
                    ResponseCode.TEAM_NOT_EXISTS.getMsg());
        }
        List<User> userList = userMapper.findByTeamId(teamId);
        List<UserVO> userVOList = new ArrayList<>();
        for (User user:userList) {
            userVOList.add(UserService.convert(user));
        }
        return ServerResponse.createServerResponseBySucess(userVOList);
    }

    @Override
    @Transactional(rollbackFor = {WhiteBoardException.class})
    public ServerResponse disbandLogic(UserVO opUser) {
        if (opUser.getTeamId() == Const.DEFAULT_TEAM_ID || opUser.getRole() != Const.ROLE_ADMIN){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }
        Integer teamId = opUser.getTeamId();
        Team team = teamMapper.selectByPrimaryKey(teamId);
        if (team == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TEAM_NOT_EXISTS.getCode(),
                    ResponseCode.TEAM_NOT_EXISTS.getMsg());
        }
        List<User> userList = userMapper.findByTeamId(teamId);
        for (User user:userList) {
            user.setRole((byte) Const.ROLE_USER);
            user.setTeamId(Const.DEFAULT_TEAM_ID);
            int result = userMapper.updateByPrimaryKey(user);
            if (result == 0){
                throw new WhiteBoardException("解散成员失败");
            }
        }
        opUser.setTeamId(0);
        opUser.setRole((byte) Const.ROLE_USER);
        Integer result = teamMapper.deleteByPrimaryKey(teamId);
        if (result == 0){
            throw new WhiteBoardException("解散团队失败");
        }
        return ServerResponse.createServerResponseBySucess(opUser);
    }

    @Override
    public ServerResponse quitLogic(UserVO opUser, Integer uid) {
        //参数校验
        if (uid == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        User user = userMapper.selectByPrimaryKey(uid);
        if (user == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EXISTS.getCode(),
                    ResponseCode.USERNAME_NOT_EXISTS.getMsg());
        }
        Team team = teamMapper.selectByPrimaryKey(opUser.getTeamId());
            //队伍不存在
        if (team == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TEAM_NOT_EXISTS.getCode(),
                    ResponseCode.TEAM_NOT_EXISTS.getMsg());
        }
        //权限校验
            //不在同一队伍
        if (opUser.getTeamId() != user.getTeamId()){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }
            //opUser权限不足
        if (opUser.getUid() != uid && opUser.getRole() != Const.ROLE_ADMIN){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }
        //退出团队
        user.setRole((byte) Const.ROLE_USER);
        user.setTeamId(0);
        Integer result = userMapper.updateByPrimaryKey(user);
        if (result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    ResponseCode.UPDATE_ERROR.getMsg());
        }
        //团队人数-1
        if (team.getMemberCount() == 1){
            result = teamMapper.deleteByPrimaryKey(team.getTeamId());
        }
        else {
            team.setMemberCount(team.getMemberCount()-1);
            result = teamMapper.updateByPrimaryKey(team);
        }
        if (result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    ResponseCode.UPDATE_ERROR.getMsg());
        }

        return ServerResponse.createServerResponseBySucess();
    }

    @Override
    public ServerResponse setRoleLogic(Integer uid, Byte role, UserVO opUser) {
        if (uid == null || role == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        User user = userMapper.selectByPrimaryKey(uid);
        if (user == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EXISTS.getCode(),
                    ResponseCode.USERNAME_NOT_EXISTS.getMsg());
        }
        //操作人无团队、操作人与被操作人不在同一团队、操作人不是管理员
        if (opUser.getTeamId() == Const.DEFAULT_TEAM_ID || user.getTeamId() != opUser.getTeamId() || opUser.getRole() != Const.ROLE_ADMIN){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }
        user.setRole(role);
        Integer result = userMapper.updateByPrimaryKey(user);
        if (result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    ResponseCode.UPDATE_ERROR.getMsg());
        }
        return ServerResponse.createServerResponseBySucess();
    }

    public static TeamVO convert(Team team){
        TeamVO teamVO = new TeamVO();
        teamVO.setTeamName(team.getTeamName());
        teamVO.setAvatar(team.getAvatar());
        teamVO.setRegTime(DateUtil.date2String(team.getRegTime()));
        teamVO.setUpdateTime(DateUtil.date2String(team.getUpdateTime()));
        teamVO.setTeamId(team.getTeamId());
        teamVO.setMemberCount(team.getMemberCount());
        return teamVO;
    }
}
