package com.entseeker.Hotel.controller;


import com.entseeker.Hotel.dto.Response;
import com.entseeker.Hotel.request.roomRequests.RoomAvailabilityRequest;
import com.entseeker.Hotel.request.roomRequests.RoomRequest;
import com.entseeker.Hotel.service.interfac.IBookingService;
import com.entseeker.Hotel.service.interfac.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private IRoomService roomService;

    @Autowired
    private IBookingService bookingService;


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewRoom(@ModelAttribute RoomRequest roomRequest) {
        if (roomRequest.getPhoto() == null || roomRequest.getPhoto().isEmpty()
                || roomRequest.getRoomType() == null || roomRequest.getRoomType().isBlank()
                || roomRequest.getRoomPrice() == null){
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please fill all the fields");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = roomService.addNewRoom(roomRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms() {
        Response response = roomService.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomsTypes();
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Response> getRoomById(@PathVariable Long roomId) {
        Response response = roomService.getRoomId(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available_rooms")
    public ResponseEntity<Response> getAvailableRooms() {
        Response response = roomService.getAllAvailableRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available_rooms_by_date_and_types")
    public ResponseEntity<Response> getAvailableRoomsByDateAndType(
            @ModelAttribute RoomAvailabilityRequest roomAvailabilityRequest) {
        if (roomAvailabilityRequest.getCheckInDate() == null || roomAvailabilityRequest.getCheckOutDate() == null
                || roomAvailabilityRequest.getRoomType() == null || roomAvailabilityRequest.getRoomType().isBlank()){
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please fill all the fields");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response = roomService.getAvailableRoomsByDataAndTypes(roomAvailabilityRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateRoom(@PathVariable Long roomId, @ModelAttribute RoomRequest roomRequest) {
        Response response = roomService.updateRoom(roomId, roomRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteRoom(@PathVariable Long roomId) {
        Response response = roomService.deleteRoom(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

}
