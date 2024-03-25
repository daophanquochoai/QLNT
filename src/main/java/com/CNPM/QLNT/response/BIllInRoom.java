package com.CNPM.QLNT.response;

import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class BIllInRoom {
    private int numberBill;
    private LocalDate day_Begin;
    private LocalDate day_End;
    private int number_E_Begin;
    private int number_E_End;
    private int number_W_Begin;
    private int number_W_End;
    private int other_Price;
    private String ghi_Chu;
    private BigInteger thanh_Tien;
    private boolean dong_tien;
    private int roomId;
}
