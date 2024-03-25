package com.CNPM.QLNT.security;

import com.CNPM.QLNT.response.InfoLogin;
import com.CNPM.QLNT.services.Inter.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAKey;

@RestController
@Slf4j
public class JwtAuthenticationController {
    
    private final JwtTokenService tokenService;
    private ICustomerService iCustomerService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public JwtAuthenticationController(JwtTokenService tokenService,
                                       AuthenticationManager authenticationManager, ICustomerService iCustomerService) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.iCustomerService = iCustomerService;

    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(
            @RequestBody JwtTokenRequest jwtTokenRequest) {
        var authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        jwtTokenRequest.username(),
                        jwtTokenRequest.password());
        var authentication =
                authenticationManager.authenticate(authenticationToken);

        var token = tokenService.generateToken(authentication);
        InfoLogin info = iCustomerService.getLogin(jwtTokenRequest.username());
        return ResponseEntity.ok(new JwtTokenResponse(token, info));
    }
}


