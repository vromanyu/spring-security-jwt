package com.vromanyu.spring_security_jwt_v2.dto;

import java.util.Date;

public record JwtDTO(String token, Date expiration, Date issuedAt) {}
