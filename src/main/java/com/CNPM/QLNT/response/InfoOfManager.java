package com.CNPM.QLNT.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InfoOfManager {
    private String firstName;
    private String lastName;
    private String CCCD;
    private LocalDate date_of_birth;
    private Boolean sex;
    private String infoAddress;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private Boolean active;
}
