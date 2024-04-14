package com.CNPM.QLNT.response;

import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.model.Room;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class History {
    private LocalDateTime StayDay;
    private LocalDateTime MoveDay;
    private Customers customers;
    private Room OldRoom;
    private Room NewRoom;
}
