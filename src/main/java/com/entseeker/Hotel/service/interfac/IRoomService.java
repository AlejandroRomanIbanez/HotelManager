package com.entseeker.Hotel.service.interfac;

import com.entseeker.Hotel.dto.Response;
import com.entseeker.Hotel.request.roomRequests.RoomAvailabilityRequest;
import com.entseeker.Hotel.request.roomRequests.RoomRequest;

import java.util.List;

public interface IRoomService {

    Response addNewRoom(RoomRequest roomRequest);

    List<String> getAllRoomsTypes();

    Response getAllRooms();

    Response deleteRoom(Long roomId);

    Response updateRoom(Long roomId, RoomRequest roomRequest);

    Response getRoomId(Long roomId);

    Response getAvailableRoomsByDataAndTypes(RoomAvailabilityRequest roomAvailabilityRequest);

    Response getAllAvailableRooms();
}
