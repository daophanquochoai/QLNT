package com.CNPM.QLNT.response;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InfoContract {
    private String firstName;
    private String lastName;
    private String CCCD;
    private LocalDate date_of_birth;
    private Boolean sex;
    private String infoAddress;
    private String phoneNumber;
    private String email;
    private int numberRoom;
    private LocalDate conDate;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Boolean status;
}
