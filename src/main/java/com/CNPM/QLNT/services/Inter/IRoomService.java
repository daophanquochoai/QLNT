package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.room;

import java.util.List;
import java.util.Optional;

public interface IRoomService {
    public List<room> allRoom();
    public Optional<room> getRoom(Integer room_id);
}
