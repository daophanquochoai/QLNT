package com.CNPM.QLNT.response;

import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.model.Room;
import lombok.Data;

import java.time.LocalDate;

@Data
public class History {
    private LocalDate StayDay;
    private LocalDate MoveDay;
    private Customers customers;
    private Room OldRoom;
    private Room NewRoom;
}
