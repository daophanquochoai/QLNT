package com.CNPM.QLNT.controller;

import com.CNPM.QLNT.model.room;
import com.CNPM.QLNT.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final RoomService roomService;

    @GetMapping("/getAllRoom")
    public ResponseEntity<List<room>> getAllRoom() throws SQLException{
        return ResponseEntity.ok(roomService.allRoom());
    }
}
