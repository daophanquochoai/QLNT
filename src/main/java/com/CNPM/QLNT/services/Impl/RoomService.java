package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.model.room;
import com.CNPM.QLNT.repository.RoomResitory;
import com.CNPM.QLNT.services.Inter.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomResitory roomRepository;

    @Override
    public List<room> allRoom() {
        return roomRepository.findAll();
    }

    @Override
    public Optional<room> getRoom(Integer room_id){
        return Optional.of(roomRepository.findById(room_id).get());
    }
}
