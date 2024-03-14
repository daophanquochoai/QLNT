package com.CNPM.QLNT.security;

import com.CNPM.QLNT.response.InfoLogin;

public record JwtTokenResponse(String token, InfoLogin infoLogin) {}


