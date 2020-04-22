package com.whiteboard.service;

import com.whiteboard.pojo.User;
import com.whiteboard.utils.ServerResponse;

public interface IUserService {
    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public ServerResponse loginLogic(String username, String password);

    /**
     * 注册
     * @param user
     * @return
     */
    public ServerResponse registerLogic(User user);

    /**
     * 修改用户信息
     */
    public ServerResponse updateLogic(User user);

    /**
     * 查询用户
     * @param keyword
     * @return
     */
    public ServerResponse searchLogic(String keyword, Integer pageNum, Integer pageSize, String orderBy);
}
