package com.vromanyu.spring_security_jwt_v2.exception;

import java.time.LocalDate;
import java.util.Date;

public record CustomAuthenticationException(int code, String message, String path, Date timestamp) {}
