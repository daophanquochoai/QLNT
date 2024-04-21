package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.RoomType;

import java.util.List;

public interface IRoomType {
    void addRoomType(RoomType homeCate);
    List<RoomType> getAllRoomType();
    RoomType getRoomTypeByRoomTypeId(int roomTypeId);
}
