package com.entseeker.Hotel.service.impl;

import com.entseeker.Hotel.dto.BookingDTO;
import com.entseeker.Hotel.dto.Response;
import com.entseeker.Hotel.dto.RoomDTO;
import com.entseeker.Hotel.entity.Booking;
import com.entseeker.Hotel.entity.Room;
import com.entseeker.Hotel.entity.User;
import com.entseeker.Hotel.exception.CustomException;
import com.entseeker.Hotel.repository.BookingRepository;
import com.entseeker.Hotel.repository.RoomRepository;
import com.entseeker.Hotel.repository.UserRepository;
import com.entseeker.Hotel.service.interfac.IBookingService;
import com.entseeker.Hotel.service.interfac.IRoomService;
import com.entseeker.Hotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response = new Response();

        try {
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("Check in date should be before check out date");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException("Room not found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomException("User not found"));
            List<Booking> existingBookings = room.getBookings();
            
            if (!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new CustomException("Room is not available for selected dates");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomAlphanumeric(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Room Booked successfully");
            response.setBookingConfirmationCode(bookingConfirmationCode);
        }catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());;
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while booking a room" + e.getMessage());;
        }
        return response;
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {

        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(() -> new CustomException("Booking not found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
            response.setStatusCode(200);
            response.setMessage("Room Booked successfully");
            response.setBooking(bookingDTO);
        }catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());;
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred fetching a booking" + e.getMessage());;
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("Booking fetched successfully");
            response.setBookingList(bookingDTOList);
        }catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());;
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while fetching bookings" + e.getMessage());;
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();

        try {
            bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new CustomException("Booking not found"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("Booking canceled successfully");
        }catch (CustomException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());;
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while canceling a booking" + e.getMessage());;
        }
        return response;
    }
}
