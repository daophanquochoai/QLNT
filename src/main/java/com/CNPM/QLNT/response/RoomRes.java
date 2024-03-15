package com.CNPM.QLNT.response;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class RoomRes {
    private int room_id;
    private int limit;
    private String home_category_name;
    private BigDecimal price;
    private Boolean status;
}
