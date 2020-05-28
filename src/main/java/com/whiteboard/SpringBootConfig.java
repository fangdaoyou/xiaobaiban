package com.whiteboard;

import com.whiteboard.inteceptors.PortalLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootConfiguration
public class SpringBootConfig implements WebMvcConfigurer {

    @Autowired
    PortalLogin portalLogin;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excluedUrl = new ArrayList<>();
        excluedUrl.add("/portal/user/login");
        excluedUrl.add("/portal/user/register");
        List<String> interceptedUrl = new ArrayList<>();
        interceptedUrl.add("/portal/user/**");
        interceptedUrl.add("/portal/team/**");
        interceptedUrl.add("/portal/task/**");
        interceptedUrl.add("/portal/clock_in/**");
        interceptedUrl.add("/portal/message/**");

        registry.addInterceptor(portalLogin).addPathPatterns(interceptedUrl)
                .excludePathPatterns(excluedUrl);
    }
}
