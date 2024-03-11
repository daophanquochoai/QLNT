package com.CNPM.QLNT.response;

import com.CNPM.QLNT.model.room;
import com.CNPM.QLNT.model.user_auth;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Info_user {
    private int id;
    private String first_name;
    private String last_name;
    private String CCCD;
    private Date date_of_birth;
    private Boolean sex;
    private String info_address;
    private String phone_number;
    private String email;
    private int Room;
    private String taikhoan;
    private String matkhau;

}
