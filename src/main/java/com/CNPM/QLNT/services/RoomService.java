package com.CNPM.QLNT.services;

import com.CNPM.QLNT.model.room;
import com.CNPM.QLNT.repository.RoomResitory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService{

    private final RoomResitory roomRepository;

    @Override
    public List<room> allRoom() {
        return roomRepository.findAll();
    }
}
