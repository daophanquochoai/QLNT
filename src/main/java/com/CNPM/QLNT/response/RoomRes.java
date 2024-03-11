package com.CNPM.QLNT.response;

import com.CNPM.QLNT.model.bill;
import com.CNPM.QLNT.model.home_category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
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
