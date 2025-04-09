package com.vromanyu.spring_security_jwt.exception;

import java.util.Date;

public record CustomAuthenticationException(int code, String message, String path, Date timestamp) {
}
