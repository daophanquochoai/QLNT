package com.CNPM.QLNT.security;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.model.Manager;
import com.CNPM.QLNT.model.UserAuth;
import com.CNPM.QLNT.repository.CustomerRepository;
import com.CNPM.QLNT.repository.ManagerRepo;
import com.CNPM.QLNT.repository.UserAuthRepo;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class JwtAuthenticationController {

    private JwtTokenService tokenService;
    private CustomerRepository customerRepository;
    private AuthenticationManager authenticationManager;
    private UserAuthRepo userAuthRepo;
    private ManagerRepo managerRepo;



    @Autowired
    public JwtAuthenticationController(JwtTokenService tokenService,
                                       AuthenticationManager authenticationManager,
                                       CustomerRepository customerRepository,
                                       UserAuthRepo userAuthRepo,
                                       ManagerRepo managerRepo) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.userAuthRepo = userAuthRepo;
        this.managerRepo = managerRepo;
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
        Optional<UserAuth> ua = userAuthRepo.findByUsername(jwtTokenRequest.username());
        if( ua.isEmpty()) throw new ResourceNotFoundException("Lỗi lấy thông tin người dùng");
        UserAuth u = ua.get();
        if( u.getRole().equals("ADMIN") || u.getRole().equals("MANAGER")){
            Manager m = managerRepo.getInfoManager(u.getId());
            return ResponseEntity.ok(new JwtTokenResponse(token,m));
        }
        else{
            Customers c = customerRepository.getInfoCustomerr(u.getId());
            return ResponseEntity.ok(new JwtTokenResponse(token, c));
        }
    }
}


