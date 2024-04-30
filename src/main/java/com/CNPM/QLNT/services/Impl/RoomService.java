package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Room;
import com.CNPM.QLNT.repository.HistoryCustomerRepo;
import com.CNPM.QLNT.repository.RoomRepo;
import com.CNPM.QLNT.response.RoomRes;
import com.CNPM.QLNT.services.Inter.IRoomTypeService;
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
    private final RoomRepo roomRepo;
    private final IRoomTypeService iRoomTypeService;
    private final HistoryCustomerRepo historyCustomerRepo;

    @Override
    public List<RoomRes> getAllRoom() {
        List<Room> list = roomRepo.findAll();
        List<RoomRes> l = list.stream().map(
                r ->
                {
                    RoomRes rm = new RoomRes(r.getRoomId(),
                            r.getLimit(),
                            r.getRoomType().getRoomTypeId(),
                            r.getPrice());
                    return rm;
                }
        ).collect(Collectors.toList());
        return l;
    }

    @Override
    public Optional<Room> getRoomByRoomId(Integer roomId) {
        return Optional.of(roomRepo.findById(roomId).get());
    }

    @Override
    public void addRoom(RoomRes roomRes) {
        boolean isExisted = getAllRoom().stream()
                .anyMatch(r -> r.getRoomId() == roomRes.getRoomId());
        if(isExisted) throw new ResourceNotFoundException("Phòng đã tồn tại");
        try {
            Room room = new Room();
            room.setRoomId(roomRes.getRoomId());
            room.setLimit(roomRes.getLimit());
            room.setPrice(roomRes.getPrice());
            room.setRoomType(iRoomTypeService.getRoomTypeByRoomTypeId(roomRes.getRoomTypeId()));
            log.info("{}", room);
            roomRepo.save(room);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Dữ liệu phòng bị lỗi");
        }
    }

    @Override
    public void updateRoom(int id, RoomRes roomRes) {
        if (getRoomByRoomId(id).isEmpty() || id == 0) throw new ResourceNotFoundException("Phòng không tồn tại");
        Room r = getRoomByRoomId(id).get();
        if (roomRes.getRoomTypeId() != 0) {
            r.setRoomType(iRoomTypeService.getRoomTypeByRoomTypeId(roomRes.getRoomTypeId()));
        }
        if (roomRes.getLimit() != 0 && roomRes.getLimit() > 0) {
            if (historyCustomerRepo.getCustomersByRoom(id).size() > roomRes.getLimit()) {
                throw new ResourceNotFoundException("Sức chứa bé hơn số người ở hiện tại");
            }
            r.setLimit(roomRes.getLimit());
        }
        if (roomRes.getPrice() != null) {
            r.setPrice(roomRes.getPrice());
        }
        roomRepo.save(r);
    }

    @Override
    public void deleteRoom(int id) throws Exception {
        Room Room = getRoomByRoomId(id).get();
        Room.setRoomType(null);
        roomRepo.delete(Room);
    }

    @Override
    public List<RoomRes> getAllRoomByLimit(int type) {
        List<Room> l;
        List<Room> r = roomRepo.findAll();
        if (type == 1) {
            l = r.stream().filter(temp -> historyCustomerRepo.getCustomersByRoom(temp.getRoomId()).size() == 0).collect(Collectors.toList());
        } else if (type == 2) {
            l = r.stream().filter(temp -> (historyCustomerRepo.getCustomersByRoom(temp.getRoomId()).size() < temp.getLimit() && historyCustomerRepo.getCustomersByRoom(temp.getRoomId()).size() > 0)).collect(Collectors.toList());
        } else {
            l = r.stream().filter(temp -> historyCustomerRepo.getCustomersByRoom(temp.getRoomId()).size() == temp.getLimit()).collect(Collectors.toList());
        }
        List<RoomRes> list = l.stream().map(
                temp ->
                {
                    RoomRes rm = new RoomRes(temp.getRoomId(),
                            temp.getLimit(),
                            temp.getRoomType().getRoomTypeId(),
                            temp.getPrice());
                    return rm;
                }
        ).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<Room> getAllRoomWithContract() {
        return roomRepo.getRoomWithContract();
    }

    @Override
    public List<RoomRes> getRoomForBill() {
        List<Room> list = roomRepo.findAll();
        list = list.stream().filter(r -> historyCustomerRepo.getCustomersByRoom(r.getRoomId()).size() != 0 &&
                        !r.getBill().stream().anyMatch(b -> b.getBeginDate().getMonth().getValue() == LocalDate.now().getMonth().getValue() - 1
                                && b.getBeginDate().getYear() == LocalDate.now().getYear()
                        ))
                .collect(Collectors.toList());
        List<RoomRes> l = list.stream().map(
                r ->
                {
                    RoomRes rm = new RoomRes(r.getRoomId(),
                            r.getLimit(),
                            r.getRoomType().getRoomTypeId(),
                            r.getPrice());
                    return rm;
                }
        ).collect(Collectors.toList());
        return l;
    }
}