package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.HomeCategory;
import com.CNPM.QLNT.model.Room;
import com.CNPM.QLNT.repository.RoomResitory;
import com.CNPM.QLNT.response.RoomRes;
import com.CNPM.QLNT.services.Inter.IHomeCategory;
import com.CNPM.QLNT.services.Inter.IRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService implements IRoomService {

    private final RoomResitory roomRepository;
    private final IHomeCategory iHomeCategory;

    @Override
    public List<RoomRes> allRoom() {
        List<Room> list = roomRepository.findAll();
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
    public Optional<Room> getRoom(Integer room_id){
        return Optional.of(roomRepository.findById(room_id).get());
    }

    @Override
    public void addRoom(RoomRes roomRes) {
        try{
            HomeCategory homeCategory = iHomeCategory.getHomeCategory(roomRes.getHome_category_name());
            Room room = new Room();
            room.setId(roomRes.getRoom_id());
            room.setLimit(roomRes.getLimit());
            room.setPrice(roomRes.getPrice());
            room.setHomeCategoryId(homeCategory);
            room.setStatus(true);
            log.info("{}",room);
            roomRepository.save(room);
        }
        catch (Exception ex){
            throw new ResourceNotFoundException("Khong tim thay loai hoac du lieu phong sai");
        }
    }

    @Override
    public void updateRoom(int id, RoomRes roomRes) {
        try{
            Room R = getRoom(id).get();
            HomeCategory homeCategory = iHomeCategory.getHomeCategory(roomRes.getHome_category_name());
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
        Room Room = getRoom(id).get();
        Room.setHomeCategoryId(null);
        roomRepository.delete(Room);
    }

    @Override
    public List<RoomRes> getAllRoomByStatus(boolean status) {
        List<Room> listRoom = roomRepository.getRoomByStatus(status);
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

    @Override
    public List<RoomRes> getAllRoomByLimit(int type) {
        List<Room> l = new ArrayList<>();
        if(type == 1){
            l = roomRepository.getRoomByTrong();
        }else if( type == 2){
            l = roomRepository.getRoomByThueTrong();
        }else{
            l = roomRepository.getRoomByDay();
        }
        List<RoomRes> list = l.stream().map(
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
        return list;
    }

    @Override
    public List<RoomRes> getRoomForBill() {
        List<Room> list = roomRepository.getRoomByStatus(true);
        list = list.stream().filter( r->!r.getCustomers().isEmpty() &&
                !r.getBill().stream().anyMatch( b->b.getBeginDate().getMonth().getValue() == LocalDate.now().getMonth().getValue()-1
                                            && b.getBeginDate().getYear() == LocalDate.now().getYear()
                ))
                        .collect(Collectors.toList());
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
}