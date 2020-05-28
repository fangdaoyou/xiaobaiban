package com.whiteboard.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whiteboard.common.Const;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.dao.ClockInConfigMapper;
import com.whiteboard.dao.TeamMapper;
import com.whiteboard.dao.UserClockInMapper;
import com.whiteboard.dao.UserMapper;
import com.whiteboard.exception.WhiteBoardException;
import com.whiteboard.pojo.*;
import com.whiteboard.service.IClockInService;
import com.whiteboard.utils.DateUtil;
import com.whiteboard.utils.DistanceCalculate;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.ClockInConfigVO;
import com.whiteboard.vo.TaskVO;
import com.whiteboard.vo.UserClockInVO;
import com.whiteboard.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClockInService implements IClockInService {
    @Autowired
    ClockInConfigMapper clockInConfigMapper;
    @Autowired
    UserClockInMapper userClockInMapper;
    @Autowired
    TeamMapper teamMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public ServerResponse createLogic(ClockInConfigVO clockInConfigVO, UserVO userInfo) {
        Integer teamId = userInfo.getTeamId();
        if (teamId == Const.DEFAULT_TEAM_ID || userInfo.getRole() != Const.ROLE_ADMIN){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }

        if (clockInConfigVO.getStartTime() == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.START_TIME_EMPTY.getCode(),
                    ResponseCode.START_TIME_EMPTY.getMsg());
        }

        if (clockInConfigVO.getDdlTime() == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.DDL_EMPTY.getCode(),
                    ResponseCode.DDL_EMPTY.getMsg());
        }

        if (clockInConfigVO.getArea() == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.AREA_EMPTY.getCode(),
                    ResponseCode.AREA_EMPTY.getMsg());
        }
        if (clockInConfigVO.getLatitude() == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.LATITUDE_EMPTY.getCode(),
                    ResponseCode.LATITUDE_EMPTY.getMsg());
        }
        if (clockInConfigVO.getLongitude() == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.LONGITUDE_EMPTY.getCode(),
                    ResponseCode.LONGITUDE_EMPTY.getMsg());
        }

        clockInConfigVO.setTeamId(userInfo.getTeamId());
        clockInConfigVO.setState(Const.ClockInNotStart);
        clockInConfigVO.setMemberCount(0);
        if (clockInConfigVO.getLatitude() == null || clockInConfigVO.getLongitude() == null){
            clockInConfigVO.setLatitude(Const.DEFAULT_LATITUDE);
            clockInConfigVO.setLongitude(Const.DEFAULT_LONGITUDE);
        }else {
            clockInConfigVO.setLatitude(clockInConfigVO.getLongitude());
            clockInConfigVO.setLongitude(clockInConfigVO.getLatitude());
        }

        ClockInConfig clockInConfig = ConfigVOToConfig(clockInConfigVO);
        Integer result = clockInConfigMapper.insert(clockInConfig);
        if (result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    ResponseCode.UPDATE_ERROR.getMsg());
        }
        return ServerResponse.createServerResponseBySucess();
    }

    @Override
    public ServerResponse updateLogic(ClockInConfigVO clockInConfigVO, UserVO userInfo) {
        Integer teamId = userInfo.getTeamId();
        if (teamId == Const.DEFAULT_TEAM_ID || userInfo.getRole() != Const.ROLE_ADMIN){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }

        Integer clockId = clockInConfigVO.getClockId();
        ClockInConfig newClockInConfig = ConfigVOToConfig(clockInConfigVO);
        newClockInConfig.setClockId(clockId);
        Integer result = clockInConfigMapper.updateByPrimaryKey(newClockInConfig);
        if (result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    ResponseCode.UPDATE_ERROR.getMsg());
        }
        ClockInConfig clockInConfig = clockInConfigMapper.selectByPrimaryKey(clockId);
        clockInConfigVO = ConfigToConfigVO(clockInConfig);
        return ServerResponse.createServerResponseBySucess(clockInConfigVO);
    }

    @Override
    @Transactional(rollbackFor = {WhiteBoardException.class})
    public ServerResponse clockInLogic(UserClockInVO userClockInVO, UserVO userInfo) {
        if (userClockInVO == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        Integer clockId = userClockInVO.getClockId();
        String Latitude = userClockInVO.getLatitude();
        String Longitude = userClockInVO.getLongitude();
        if (clockId == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.CLOCK_ID_EMPTY.getCode(),
                    ResponseCode.CLOCK_ID_EMPTY.getMsg());
        }
        if (Latitude == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.LATITUDE_EMPTY.getCode(),
                    ResponseCode.LATITUDE_EMPTY.getMsg());
        }
        if (Longitude == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.LONGITUDE_EMPTY.getCode(),
                    ResponseCode.LONGITUDE_EMPTY.getMsg());
        }

        ClockInConfig clockInConfig = clockInConfigMapper.selectByPrimaryKey(clockId);
        if (clockInConfig == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.CLOCK_NOT_EXISTS.getCode(),
                    ResponseCode.CLOCK_NOT_EXISTS.getMsg());
        }

        Integer teamId = userInfo.getTeamId();
        if (teamId != clockInConfig.getTeamId()){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }

        userClockInVO.setUid(userInfo.getUid());
        UserClockIn userClockIn = ClockInVOToClockIn(userClockInVO);
        Date now = DateUtil.getTime();
        Date ddlTime = clockInConfig.getDdlTime();
        Date startTime = clockInConfig.getStartTime();

        //重复签到
        UserClockIn oldUserClockIn = userClockInMapper.selectByPrimaryKey(userClockIn.getClockId(), userClockIn.getUid());
        if (oldUserClockIn != null){
            return ServerResponse.createServerResponseByFail(ResponseCode.CLOCK_IN_EXISTS.getCode(),
                    ResponseCode.CLOCK_IN_EXISTS.getMsg());
        }
        //未开始
        if (now.before(startTime)){
            return ServerResponse.createServerResponseByFail(ResponseCode.CLOCK_IN_NOT_START.getCode(),
                    ResponseCode.CLOCK_IN_NOT_START.getMsg());
        }
        //超时
        if (now.after(ddlTime)){
            if (clockInConfig.getState() != Const.ClockInFinished){
                int result = clockInConfigMapper.updateState(clockId, Const.ClockInFinished);
                if (result == 0){
                    throw new WhiteBoardException("更新失败");
                }
            }
            return ServerResponse.createServerResponseByFail(ResponseCode.CLOCK_IN_TIME_OUT.getCode(),
                    ResponseCode.CLOCK_IN_TIME_OUT.getMsg());
        }
        //时间验证完毕
        if (clockInConfig.getState() == Const.ClockInNotStart){
            //更新状态
            int result = clockInConfigMapper.updateState(clockId, Const.ClockInStart);
            if (result == 0){
                throw new WhiteBoardException("更新失败");
            }
        }

        //范围判断
        BigDecimal myLatitude = userClockIn.getLatitude();
        BigDecimal myLongitude = userClockIn.getLongitude();
        BigDecimal aimLatitude = clockInConfig.getLongitude();
        BigDecimal aimLongitude = clockInConfig.getLongitude();
        BigDecimal aimArea = new BigDecimal(clockInConfig.getArea()+10);
        BigDecimal myArea = DistanceCalculate.getShortestDistance(myLatitude, myLongitude, aimLatitude, aimLongitude);
        if (myArea.compareTo(aimArea) > 0) {
            return ServerResponse.createServerResponseByFail(ResponseCode.DISTANCE_ERROR.getCode(),
                    ResponseCode.DISTANCE_ERROR.getMsg());
        }


        //插入记录
        ClockInConfig newClockInConfig = new ClockInConfig();
        newClockInConfig.setClockId(clockId);
        newClockInConfig.setMemberCount(clockInConfig.getMemberCount()+1);
        Integer result = clockInConfigMapper.updateByPrimaryKey(newClockInConfig);
        if (result == 0){
            throw new WhiteBoardException("更新失败");
        }
        result = userClockInMapper.insert(userClockIn);
        if (result == 0){
            throw new WhiteBoardException("更新失败");
        }
        return ServerResponse.createServerResponseBySucess();
    }

    @Override
    public ServerResponse listTeamLogic(UserVO userInfo, Integer pageNum, Integer pageSize, String orderBy) {
        Integer teamId = userInfo.getTeamId();
        if (teamId == Const.DEFAULT_TEAM_ID || userInfo.getRole() != Const.ROLE_ADMIN){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }
        Team team = teamMapper.selectByPrimaryKey(teamId);
        if (team == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TEAM_NOT_EXISTS.getCode(),
                    ResponseCode.TEAM_NOT_EXISTS.getMsg());
        }

        List<ClockInConfig> clockInConfigList = clockInConfigMapper.findByTeamId(teamId);
        List<ClockInConfigVO> clockInConfigVOList = new ArrayList<>();
        for (ClockInConfig clockInConfig:clockInConfigList) {
            ClockInConfigVO clockInConfigVO = ConfigToConfigVO(clockInConfig);
            clockInConfigVO.setNotMemberCount(team.getMemberCount()-clockInConfig.getMemberCount());
            clockInConfigVOList.add(clockInConfigVO);
        }
        PageHelper.startPage(pageNum, pageSize);
        PageInfo pageInfo = new PageInfo(clockInConfigVOList);
        pageInfo.setList(clockInConfigVOList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }

    @Override
    public ServerResponse haveClockInLogic(Integer clockId, UserVO userInfo, Integer pageNum, Integer pageSize, String orderBy) {
        if (clockId == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.CLOCK_ID_EMPTY.getCode(),
                    ResponseCode.CLOCK_ID_EMPTY.getMsg());
        }
        if (userInfo.getRole() != Const.ROLE_ADMIN){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }

        ClockInConfig clockInConfig = clockInConfigMapper.selectByPrimaryKey(clockId);
        if (clockInConfig == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.CLOCK_NOT_EXISTS.getCode(),
                    ResponseCode.CLOCK_NOT_EXISTS.getMsg());
        }
        if (clockInConfig.getTeamId() != userInfo.getTeamId()){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }

        List<User> userList = userMapper.findClockIn(clockId);
        List<UserVO> userVOList = new ArrayList<>();
        for (User user:userList) {
            UserVO userVO = UserService.convert(user);
            userVOList.add(userVO);
        }

        return ServerResponse.createServerResponseBySucess(userVOList);
    }

    @Override
    public ServerResponse haveNotClockInLogic(Integer clockId, UserVO userInfo, Integer pageNum, Integer pageSize, String orderBy) {
        if (clockId == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.CLOCK_ID_EMPTY.getCode(),
                    ResponseCode.CLOCK_ID_EMPTY.getMsg());
        }
        if (userInfo.getRole() != Const.ROLE_ADMIN){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }

        ClockInConfig clockInConfig = clockInConfigMapper.selectByPrimaryKey(clockId);
        if (clockInConfig == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.CLOCK_NOT_EXISTS.getCode(),
                    ResponseCode.CLOCK_NOT_EXISTS.getMsg());
        }
        if (clockInConfig.getTeamId() != userInfo.getTeamId()){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIVILEGE_ERROR.getCode(),
                    ResponseCode.PRIVILEGE_ERROR.getMsg());
        }

        List<User> userList = userMapper.findNotClockIn(clockId, userInfo.getTeamId());
        List<UserVO> userVOList = new ArrayList<>();
        for (User user:userList) {
            UserVO userVO = UserService.convert(user);
            userVOList.add(userVO);
        }

        return ServerResponse.createServerResponseBySucess(userVOList);
    }

    public ClockInConfigVO ConfigToConfigVO (ClockInConfig clockInConfig){
        ClockInConfigVO clockInConfigVO = new ClockInConfigVO();

        clockInConfigVO.setLongitude(clockInConfig.getLongitude().toString());
        clockInConfigVO.setLatitude(clockInConfig.getLatitude().toString());
        clockInConfigVO.setStartTime(DateUtil.date2String(clockInConfig.getStartTime()));
        clockInConfigVO.setDdlTime(DateUtil.date2String(clockInConfig.getDdlTime()));
        clockInConfigVO.setCreateTime(DateUtil.date2String(clockInConfig.getCreateTime()));
        clockInConfigVO.setUpdateTime(DateUtil.date2String(clockInConfig.getUpdateTime()));
        clockInConfigVO.setTeamId(clockInConfig.getTeamId());
        clockInConfigVO.setState(clockInConfig.getState());
        clockInConfigVO.setArea(clockInConfig.getArea());
        clockInConfigVO.setClockId(clockInConfig.getClockId());
        clockInConfigVO.setMemberCount(clockInConfig.getMemberCount());
        return clockInConfigVO;
    }

    public ClockInConfig ConfigVOToConfig (ClockInConfigVO clockInConfigVO){
        ClockInConfig clockInConfig = new ClockInConfig();

        if (clockInConfigVO.getLongitude() != null) clockInConfig.setLongitude(new BigDecimal(clockInConfigVO.getLongitude()));
        if (clockInConfigVO.getLatitude() != null) clockInConfig.setLatitude(new BigDecimal(clockInConfigVO.getLatitude()));

        clockInConfig.setCreateTime(DateUtil.string2Date(clockInConfigVO.getCreateTime()));
        clockInConfig.setUpdateTime(DateUtil.string2Date(clockInConfigVO.getUpdateTime()));
        clockInConfig.setStartTime(DateUtil.string2Date(clockInConfigVO.getStartTime()));
        clockInConfig.setDdlTime(DateUtil.string2Date(clockInConfigVO.getDdlTime()));

        clockInConfig.setTeamId(clockInConfigVO.getTeamId());
        clockInConfig.setState(clockInConfigVO.getState());
        clockInConfig.setArea(clockInConfigVO.getArea());

        clockInConfig.setClockId(clockInConfigVO.getClockId());
        clockInConfig.setMemberCount(clockInConfigVO.getMemberCount());
        return clockInConfig;
    }

    public UserClockIn ClockInVOToClockIn (UserClockInVO UserClockInVO){
        UserClockIn userClockIn = new UserClockIn();

        userClockIn.setLongitude(new BigDecimal(UserClockInVO.getLongitude()));
        userClockIn.setLatitude(new BigDecimal(UserClockInVO.getLatitude()));
        userClockIn.setUpdateTime(DateUtil.string2Date(UserClockInVO.getUpdateTime()));
        userClockIn.setClockId(UserClockInVO.getClockId());
        userClockIn.setUid(UserClockInVO.getUid());

        return userClockIn;
    }


}
