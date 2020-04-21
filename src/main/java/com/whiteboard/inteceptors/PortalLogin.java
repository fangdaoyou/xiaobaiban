package com.whiteboard.inteceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whiteboard.common.Const;
import com.whiteboard.common.ResponseCode;
import com.whiteboard.utils.ServerResponse;
import com.whiteboard.vo.UserVO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

@Component
public class PortalLogin implements HandlerInterceptor {

    //请求到达Controller前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserVO userVO = (UserVO) session.getAttribute(Const.CURRENT_USER);
        if (userVO != null){
            return true;
        }

        try{
            response.reset();
            response.addHeader("Content-Type","application/json;charset=utf-8");
            PrintWriter printWriter = response.getWriter();
            ServerResponse serverResponse = ServerResponse.createServerResponseByFail(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getMsg());
            ObjectMapper objectMapper = new ObjectMapper();
            String info = objectMapper.writeValueAsString(serverResponse);
            printWriter.write(info);
            printWriter.flush();
            printWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }

    //请求处理完后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    //客户端接受响应后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
