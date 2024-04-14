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
    private LocalDate beginDate;
    private LocalDate endDate;
    private int electricNumberBegin;
    private int electricNumberEnd;
    private int waterNumberBegin;
    private int waterNumberEnd;
    private String ghiChu;
    private Long total;
    private boolean dong_tien;
    private int roomId;
}
