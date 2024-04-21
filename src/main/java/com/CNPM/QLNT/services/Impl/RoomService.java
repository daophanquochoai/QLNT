package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.RoomType;
import com.CNPM.QLNT.model.Room;
import com.CNPM.QLNT.repository.HistoryCustomerRepo;
import com.CNPM.QLNT.repository.RoomRepo;
import com.CNPM.QLNT.response.RoomRes;
import com.CNPM.QLNT.services.Inter.IRoomType;
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
    private final RoomRepo roomRepository;
    private final IRoomType iRoomType;
    private final HistoryCustomerRepo historyCustomerRepo;

    @Override
    public List<RoomRes> allRoom() {
        List<Room> list = roomRepository.findAll();
        List<RoomRes> l = list.stream().map(
                r ->
                {
                    RoomRes rm = new RoomRes(r.getRoomId(),
                            r.getLimit(),
                            r.getRoomType().getRoomTypeId(),
                            r.getPrice(),
                            r.getStatus());
                    return rm;
                }
        ).collect(Collectors.toList());
        return l;
    }

    @Override
    public Optional<Room> getRoom(Integer room_id) {
        return Optional.of(roomRepository.findById(room_id).get());
    }

    @Override
    public void addRoom(RoomRes roomRes) {
        try {
            Room room = new Room();
            room.setRoomId(roomRes.getRoomId());
            room.setLimit(roomRes.getLimit());
            room.setPrice(roomRes.getPrice());
            room.setRoomType(iRoomType.getRoomTypeByRoomTypeId(roomRes.getRoomTypeId()));
            room.setStatus(true);
            log.info("{}", room);
            roomRepository.save(room);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Khong tim thay loai hoac du lieu phong sai");
        }
    }

    @Override
    public void updateRoom(int id, RoomRes roomRes) {
        try {
            if (getRoom(id).isEmpty() || id == 0) throw new ResourceNotFoundException("Phong khong ton tai");
            Room r = getRoom(id).get();
            if (roomRes.getRoomTypeId() != 0) {
                r.setRoomType(iRoomType.getRoomTypeByRoomTypeId(roomRes.getRoomTypeId()));
            }
            if (roomRes.getLimit() != 0 && roomRes.getLimit() > 0) {
                r.setLimit(roomRes.getLimit());
            }
            if (roomRes.getStatus() != null) {
                r.setStatus(roomRes.getStatus());
            }
            if (roomRes.getPrice() != null) {
                r.setPrice(roomRes.getPrice());
            }
            roomRepository.save(r);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Du lieu sua doi loi can xem lai");
        }
    }

    @Override
    public void deleteRoom(int id) throws Exception {
        Room Room = getRoom(id).get();
        Room.setRoomType(null);
        roomRepository.delete(Room);
    }

    @Override
    public List<RoomRes> getAllRoomByStatus(boolean status) {
        List<Room> listRoom = roomRepository.getRoomByStatus(status);
        List<RoomRes> l = listRoom.stream().map(
                r ->
                {
                    RoomRes rm = new RoomRes(r.getRoomId(),
                            r.getLimit(),
                            r.getRoomType().getRoomTypeId(),
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
        List<Room> r = roomRepository.findAll();
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
                            temp.getPrice(),
                            temp.getStatus());
                    return rm;
                }
        ).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<Room> getAllRoomWithoutContract(){
        return roomRepository.getRoomWithContract();
    }
    @Override
    public List<RoomRes> getRoomForBill() {
        List<Room> list = roomRepository.getRoomByStatus(true);
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
                            r.getPrice(),
                            r.getStatus());
                    return rm;
                }
        ).collect(Collectors.toList());
        return l;
    }
}