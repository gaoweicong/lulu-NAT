package com.glwlc.nat.server.config;

import com.glwlc.nat.server.service.NatConnectHandler;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @Author: Gavin
 * @Date: 2019-04-24 15:42
 */
@WebFilter(filterName = "natFilter")
@Configuration
    public class NatFilter implements Filter {

    private static final String SERVER_NAME_PATTERN = "\\w+.glwlc.top";

    @Resource
    private NatConnectHandler natConnectHandler;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String serverName = request.getServerName();

        if (!Pattern.matches(SERVER_NAME_PATTERN, serverName))
            filterChain.doFilter(request, response);
        else {
            String secondDomainName=serverName.substring(0, serverName.indexOf("."));

            if (secondDomainName.equalsIgnoreCase("www"))
                filterChain.doFilter(request, response);
            else {
                byte[] payload = natConnectHandler.handlerNat((HttpServletRequest) request, secondDomainName);
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write(payload);
                // TODO
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {

    }
}
