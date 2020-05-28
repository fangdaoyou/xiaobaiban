package com.whiteboard.service.impl;

import com.whiteboard.common.Const;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.dao.ReadTeamMessageMapper;
import com.whiteboard.dao.TeamMapper;
import com.whiteboard.dao.TeamMessageMapper;
import com.whiteboard.dao.UserMapper;
import com.whiteboard.exception.WhiteBoardException;
import com.whiteboard.pojo.ReadTeamMessage;
import com.whiteboard.pojo.TeamMessage;
import com.whiteboard.pojo.User;
import com.whiteboard.service.IMessageService;
import com.whiteboard.utils.DateUtil;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.TeamMessageVO;
import com.whiteboard.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService implements IMessageService {
    @Autowired
    TeamMessageMapper teamMessageMapper;
    @Autowired
    ReadTeamMessageMapper readTeamMessageMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    @Transactional(rollbackFor = {WhiteBoardException.class})
    public ServerResponse createLogic(TeamMessageVO teamMessageVO, UserVO userVO) {
        Integer sendUid = userVO.getUid();
        Integer teamId = userVO.getTeamId();
        String description = teamMessageVO.getDescription();
        Integer type = teamMessageVO.getType();
        Integer priority = teamMessageVO.getPriority();
        if (description == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.DESCRIPTION_EMPTY.getCode(),
                    ResponseCode.DESCRIPTION_EMPTY.getMsg());
        }
        if (type == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.TYPE_EMPTY.getCode(),
                    ResponseCode.TYPE_EMPTY.getMsg());
        }
        if (type == Const.MESSAGE_COMPLICATED && priority == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PRIORITY_EMPTY.getCode(),
                    ResponseCode.PRIORITY_EMPTY.getMsg());
        }
        teamMessageVO.setSendUid(sendUid);
        teamMessageVO.setTeamId(teamId);

        TeamMessage teamMessage = VOToMessage(teamMessageVO);
        Integer result = teamMessageMapper.insert(teamMessage);
        if (result == 0){
            throw new WhiteBoardException("更新失败");
        }

        Integer messageId = teamMessage.getMessageId();
        List<User> userList = userMapper.findByTeamId(teamId);
        for (User user:userList) {
            ReadTeamMessage readTeamMessage = new ReadTeamMessage();
            readTeamMessage.setUid(user.getUid());
            readTeamMessage.setMessageId(messageId);
            readTeamMessage.setStatus(Const.NOT_READ);
            result = readTeamMessageMapper.insert(readTeamMessage);
            if (result == 0){
                throw new WhiteBoardException("更新失败");
            }
        }
        return ServerResponse.createServerResponseBySucess();
    }

    @Override
    public ServerResponse unreadLogic(Integer type, Integer uid) {
        if (type == null){
            ServerResponse.createServerResponseByFail(ResponseCode.TYPE_EMPTY.getCode(),
                    ResponseCode.TYPE_EMPTY.getMsg());
        }
        List<TeamMessage> teamMessageList = teamMessageMapper.findUnread(uid, type);
        List<TeamMessageVO> teamMessageVOList = new ArrayList<>();
        for (TeamMessage teamMessage: teamMessageList) {
            TeamMessageVO teamMessageVO = messageToVO(teamMessage);
            teamMessageVOList.add(teamMessageVO);
        }
        for (TeamMessage teamMessage: teamMessageList) {
            ReadTeamMessage readTeamMessage = new ReadTeamMessage();
            readTeamMessage.setStatus(Const.HAVE_READ);
            readTeamMessage.setUid(uid);
            readTeamMessage.setMessageId(teamMessage.getMessageId());
            readTeamMessageMapper.updateByPrimaryKey(readTeamMessage);
        }
        return ServerResponse.createServerResponseBySucess(teamMessageVOList);
    }

    public TeamMessage VOToMessage(TeamMessageVO teamMessageVO){
        TeamMessage teamMessage = new TeamMessage();
        teamMessage.setType(teamMessageVO.getType());
        teamMessage.setTeamId(teamMessageVO.getTeamId());
        teamMessage.setSendUid(teamMessageVO.getSendUid());
        teamMessage.setSendTime(DateUtil.string2Date(teamMessageVO.getSendTime()));
        teamMessage.setPriority(teamMessageVO.getPriority());
        teamMessage.setMessageId(teamMessageVO.getMessageId());
        teamMessage.setDescription(teamMessageVO.getDescription());
        return teamMessage;
    }
    public TeamMessageVO messageToVO(TeamMessage teamMessage){
        TeamMessageVO teamMessageVO = new TeamMessageVO();
        teamMessageVO.setType(teamMessage.getType());
        teamMessageVO.setTeamId(teamMessage.getTeamId());
        teamMessageVO.setSendUid(teamMessage.getSendUid());
        teamMessageVO.setSendTime(DateUtil.date2String(teamMessage.getSendTime()));
        teamMessageVO.setPriority(teamMessage.getPriority());
        teamMessageVO.setMessageId(teamMessage.getMessageId());
        teamMessageVO.setDescription(teamMessage.getDescription());
        return teamMessageVO;
    }
}
