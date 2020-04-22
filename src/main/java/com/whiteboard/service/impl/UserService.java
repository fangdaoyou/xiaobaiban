package com.whiteboard.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whiteboard.common.Const;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.dao.UserMapper;
import com.whiteboard.pojo.User;
import com.whiteboard.service.IUserService;
import com.whiteboard.utils.DateUtil;
import com.whiteboard.utils.Md5Util;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.UserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    UserMapper userMapper;
    @Override
    public ServerResponse loginLogic(String username, String password) {

        //step1:用户名和密码不能为空
        if (StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_EMPTY.getCode(),
                    ResponseCode.USERNAME_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(password)){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_EMPTY.getCode(),
                    ResponseCode.PASSWORD_EMPTY.getMsg());
        }
        //step2:查看用户是否存在
        Integer count = userMapper.findByUsername(username);
        if (count == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EXISTS.getCode(),
                    ResponseCode.USERNAME_NOT_EXISTS.getMsg());
        }
        //step3:判断密码是否正确
        User user = userMapper.findByUsernameAndPassword(username, Md5Util.encode(password));
        if (user == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_ERROR.getCode(),
                    ResponseCode.PASSWORD_ERROR.getMsg());
        }
        //step4:成功返回
        return ServerResponse.createServerResponseBySucess(convert(user));
    }

    private UserVO convert(User user){
        UserVO userVO = new UserVO();
        userVO.setUid(user.getUid());
        userVO.setAvatar(user.getAvatar());
        userVO.setEmail(user.getEmail());
        userVO.setNickname(user.getNickname());
        userVO.setPhone(user.getPhone());
        userVO.setRegTime(DateUtil.date2String(user.getRegTime()));
        userVO.setUpdateTime(DateUtil.date2String(user.getUpdateTime()));
        userVO.setRole(user.getRole());
        userVO.setTeamId(user.getTeamId());
        userVO.setUsername(user.getUsername());
        return userVO;
    }

    @Override
    public ServerResponse registerLogic(User user) {
        //step1:数据校验
        if (user == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }

        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        String phone = user.getPhone();
        String nickname = user.getNickname();

        if (StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_EMPTY.getCode(),
                    ResponseCode.USERNAME_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(password)){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_EMPTY.getCode(),
                    ResponseCode.PASSWORD_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(email)){
            return ServerResponse.createServerResponseByFail(ResponseCode.EMAIL_EMPTY.getCode(),
                    ResponseCode.EMAIL_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(phone)){
            return ServerResponse.createServerResponseByFail(ResponseCode.PHONE_EMPTY.getCode(),
                    ResponseCode.PHONE_EMPTY.getMsg());
        }
        if (StringUtils.isBlank(nickname)){
            return ServerResponse.createServerResponseByFail(ResponseCode.NICKNAME_EMPTY.getCode(),
                    ResponseCode.NICKNAME_EMPTY.getMsg());
        }

        //step2:已存在参数校验
        Integer count;
        count = userMapper.findByUsername(username);
        if (count > 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_EXISTS.getCode(),
                    ResponseCode.USERNAME_EXISTS.getMsg());
        }
        count = userMapper.findByEmail(email);
        if (count > 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.EMAIL_EXISTS.getCode(),
                    ResponseCode.EMAIL_EXISTS.getMsg());
        }
        count = userMapper.findByPhone(phone);
        if (count > 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.PHONE_EXISTS.getCode(),
                    ResponseCode.PHONE_EXISTS.getMsg());
        }

        //step3:注册
        user.setPassword(Md5Util.encode(user.getPassword()));
        user.setRole((byte) Const.ROLE_USER);
        user.setTeamId(Const.DEFAULT_TEAM_ID);
        Integer result = userMapper.insert(user);

        if (result == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.REGISTER_ERROR.getCode(),
                    ResponseCode.REGISTER_ERROR.getMsg());
        }
        return ServerResponse.createServerResponseBySucess();
    }

    @Override
    public ServerResponse updateLogic(User user) {
        int count = userMapper.updateByPrimaryKey(user);
        if (count == 0){
            return ServerResponse.createServerResponseByFail(ResponseCode.UPDATE_ERROR.getCode(),
                    ResponseCode.UPDATE_ERROR.getMsg());
        }
        User newUser = userMapper.selectByPrimaryKey(user.getUid());
        UserVO userVO = convert(newUser);
        return ServerResponse.createServerResponseBySucess(userVO);
    }

    @Override
    public ServerResponse searchLogic(String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword)){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAM_EMPTY.getCode(),
                    ResponseCode.PARAM_EMPTY.getMsg());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.findByKeywords("%" + keyword + "%");
        List<UserVO> userVOList = new ArrayList<>();
        for (User user:userList) {
            userVOList.add(convert(user));
        }
        PageInfo pageInfo = new PageInfo(userList);
        pageInfo.setList(userVOList);
        return ServerResponse.createServerResponseBySucess(pageInfo);
    }
}
