package com.CNPM.QLNT.response;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class RoomRes {
    private int roomId;
    private int limit;
    private String roomTypeName;
    private BigDecimal price;
    private Boolean status;
}
