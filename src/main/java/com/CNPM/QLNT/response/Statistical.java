package com.CNPM.QLNT.response;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistical {
    private Long doanhThu;
    private int phongChuaDong;
    private int phongDaDong;
    private int daThueDay;
    private int daThueChuaDay;
    private int chuaThue;
}
