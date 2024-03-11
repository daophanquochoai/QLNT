package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.home_category;
import com.CNPM.QLNT.model.room;
import com.CNPM.QLNT.repository.RoomResitory;
import com.CNPM.QLNT.response.RoomRes;
import com.CNPM.QLNT.services.Inter.IHomeCategory;
import com.CNPM.QLNT.services.Inter.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomResitory roomRepository;
    private final IHomeCategory iHomeCategory;

    @Override
    public List<RoomRes> allRoom() {
        List<room> list = roomRepository.findAll();
        List<RoomRes> l = list.stream().map(
                r ->
                {
                    RoomRes rm = new RoomRes(r.getId(),
                            r.getLimit(),
                            r.getHomeCategoryId().getHome_category_name(),
                            r.getPrice(),
                            r.getStatus());
                    return rm;
                }
        ).collect(Collectors.toList());
        return l;
    }

    @Override
    public Optional<room> getRoom(Integer room_id){
        return Optional.of(roomRepository.findById(room_id).get());
    }

    @Override
    public void addRoom(RoomRes roomRes) {
        try{
            home_category homeCategory = iHomeCategory.getHomeCategory(roomRes.getHome_category_name());
            room Room = new room();
            Room.setLimit(roomRes.getLimit());
            Room.setStatus(false);
            Room.setPrice(roomRes.getPrice());
            Room.setHomeCategoryId(homeCategory);
            Room.setStatus(roomRes.getStatus());
            roomRepository.save(Room);
        }
        catch (Exception ex){
            throw new ResourceNotFoundException("Khong tim thay loai hoac du lieu phong sai");
        }
    }

    @Override
    public void updateRoom(int id, RoomRes roomRes) {
        try{
            room R = getRoom(id).get();
            home_category homeCategory = iHomeCategory.getHomeCategory(roomRes.getHome_category_name());
            R.setLimit(roomRes.getLimit());
            R.setStatus(false);
            R.setPrice(roomRes.getPrice());
            R.setHomeCategoryId(homeCategory);
            R.setStatus(roomRes.getStatus());
            roomRepository.save(R);
        }
        catch (Exception ex){
            throw new ResourceNotFoundException("Du lieu sua doi loi can xem lai");
        }
    }

    @Override
    public void deleteRoom(int id) throws Exception {
        room Room = getRoom(id).get();
        Room.setHomeCategoryId(null);
        roomRepository.delete(Room);
    }

    @Override
    public List<RoomRes> getAllRoomByStatus(boolean status) {
        List<room> listRoom = roomRepository.getRoomByStatus(status);
        List<RoomRes> l = listRoom.stream().map(
                r ->
                {
                    RoomRes rm = new RoomRes(r.getId(),
                            r.getLimit(),
                            r.getHomeCategoryId().getHome_category_name(),
                            r.getPrice(),
                            r.getStatus());
                    return rm;
                }
        ).collect(Collectors.toList());
        return l;
    }
}
