package com.whiteboard.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.dao.TeamMapper;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService implements ITeamService {
    @Autowired
    TeamMapper teamMapper;
    @Override
    public ServerResponse createLogic(Team team) {
        //数据校验
        if (team == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        if (team.getTeamName() == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TEAM_NAME_EMPTY.getCode(),
                    ResponseCode.TEAM_NAME_EMPTY.getMsg());
        }
        if (team.getMemberCount() == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TEAM_MEMBER_COUNT_EMPTY.getCode(),
                    ResponseCode.TEAM_MEMBER_COUNT_EMPTY.getMsg());
        }
        //队名是否被占用
        String teamName = team.getTeamName();
        Integer count = teamMapper.findByTeamName(teamName);
        if (count > 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.TEAM_NAME_EXISTS.getCode(),
                    ResponseCode.TEAM_NAME_EXISTS.getMsg());
        }

        Integer result = teamMapper.insert(team);
        if (result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.REGISTER_ERROR.getCode(),
                    ResponseCode.REGISTER_ERROR.getMsg());
        }

        Team newTeam = teamMapper.selectByPrimaryKey(team.getTeamId());
        TeamVO teamVO = convert(newTeam);
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

    private TeamVO convert(Team team){
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
