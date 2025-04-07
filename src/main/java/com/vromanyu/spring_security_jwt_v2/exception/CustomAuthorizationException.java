package com.vromanyu.spring_security_jwt_v2.exception;

import java.util.Date;

public record CustomAuthorizationException(int code, String message, String path, Date timestamp) {}
