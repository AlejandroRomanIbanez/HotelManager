package com.entseeker.Hotel.service.interfac;

import com.entseeker.Hotel.dto.Response;
import com.entseeker.Hotel.entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IRoomService {

    Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String roomDescription);

    List<String> getAllRoomsTypes();

    Response getAllRooms();

    Response DeleteRoom(Long roomId);

    Response udpdateRoom(Long roomId, MultipartFile photo, String roomType, BigDecimal roomPrice, String roomDescription);

    Response getRoomId(Long roomId);

    Response getAvailableRoomsByDataAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    Response getAllAvailableRooms();
}
