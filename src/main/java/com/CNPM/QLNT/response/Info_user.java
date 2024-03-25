package com.CNPM.QLNT.response;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Info_user {
    private int id;
    private String first_name;
    private String last_name;
    private String CCCD;
    private LocalDate date_of_birth;
    private Boolean sex;
    private String info_address;
    private String phone_number;
    private String email;
    private int Room;
    private String taikhoan;
    private String matkhau;

}
