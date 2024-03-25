package com.CNPM.QLNT.response;

import com.CNPM.QLNT.model.Customers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private int id;
    private Customers senderId;
    private Customers receiverId;
    private String message;
    private LocalDateTime createdDatatime;
}
