package com.entseeker.Hotel.service.impl;


import com.entseeker.Hotel.dto.Response;
import com.entseeker.Hotel.dto.RoomDTO;
import com.entseeker.Hotel.entity.Room;
import com.entseeker.Hotel.exception.CustomException;
import com.entseeker.Hotel.repository.BookingRepository;
import com.entseeker.Hotel.repository.RoomRepository;
import com.entseeker.Hotel.request.roomRequests.RoomAvailabilityRequest;
import com.entseeker.Hotel.request.roomRequests.RoomRequest;
import com.entseeker.Hotel.service.FirebaseService;
import com.entseeker.Hotel.service.interfac.IRoomService;
import com.entseeker.Hotel.utils.Utils;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Override
    public Response addNewRoom(RoomRequest roomRequest) {
        Response response = new Response();

        try {
            String imageUrl = firebaseService.saveImageToFirebaseStorage(roomRequest.getPhoto());

            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomDescription(roomRequest.getRoomDescription());
            room.setRoomPrice(roomRequest.getRoomPrice());
            room.setRoomType(roomRequest.getRoomType());
            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("Room added successfully");
            response.setRoom(roomDTO);

        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while adding new room" + e.getMessage());;
        }
        return response;
    }

    @Override
    public List<String> getAllRoomsTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Rooms fetched successfully");
            response.setRoomList(roomDTOList);

        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching rooms" + e.getMessage());;
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();

        try {
            roomRepository.findById(roomId).orElseThrow(() -> new CustomException("Room not found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Rooms deleted successfully");

        }catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());;
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while deleting rooms" + e.getMessage());;
        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId, RoomRequest roomRequest) {
        Response response = new Response();

        try {
            String imageUrl = null;
            if (roomRequest.getPhoto() != null && !roomRequest.getPhoto().isEmpty()) {
                imageUrl = firebaseService.saveImageToFirebaseStorage(roomRequest.getPhoto());
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException("Room not found"));
            if (roomRequest.getRoomType() != null) room.setRoomType(roomRequest.getRoomType());
            if (roomRequest.getRoomPrice() != null) room.setRoomPrice(roomRequest.getRoomPrice());
            if (roomRequest.getRoomDescription() != null) room.setRoomDescription(roomRequest.getRoomDescription());
            if (imageUrl != null) room.setRoomPhotoUrl(roomRequest.getRoomType());

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("Rooms updated successfully");
            response.setRoom(roomDTO);

        }catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());;
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while updating rooms" + e.getMessage());;
        }
        return response;
    }

    @Override
    public Response getRoomId(Long roomId) {
        Response response = new Response();

        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException("Room not found"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusBookings(room);
            response.setStatusCode(200);
            response.setMessage("Room fetched successfully");
            response.setRoom(roomDTO);

        }catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());;
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching the room" + e.getMessage());;
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDataAndTypes(RoomAvailabilityRequest roomAvailabilityRequest) {
        Response response = new Response();

        try {
            List<Room> availableRooms = roomRepository
                    .findAvailableRoomsByDatesAndType(
                            roomAvailabilityRequest.getCheckInDate(),
                            roomAvailabilityRequest.getCheckOutDate(),
                            roomAvailabilityRequest.getRoomType());
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);
            response.setStatusCode(200);
            response.setMessage("Rooms deleted successfully");
            response.setRoomList(roomDTOList);

        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching rooms" + e.getMessage());;
        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Room fetched successfully");
            response.setRoomList(roomDTOList);

        }catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());;
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching the rooms" + e.getMessage());;
        }
        return response;
    }
}
