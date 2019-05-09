package com.glwlc.nat.server.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Gavin
 * @Date: 2019-05-08 14:34
 */

public interface NatConnectHandler {

    byte[] handlerNat(HttpServletRequest request, String domainName);

}
