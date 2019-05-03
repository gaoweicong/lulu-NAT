package com.glwlc.nat.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.TRACE;

/**
 * @Author: Gavin
 * @Date: 2019-04-25 10:19
 */
@Controller
public class TestController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/test", method = {GET, HEAD, POST, PUT,
            PATCH, DELETE, OPTIONS, TRACE})
    public ModelAndView test(){

        System.out.println(request.getServerName());

        return null;
    }

}
