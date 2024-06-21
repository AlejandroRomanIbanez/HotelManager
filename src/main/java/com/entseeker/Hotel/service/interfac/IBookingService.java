package com.entseeker.Hotel.service.interfac;

import com.entseeker.Hotel.dto.Response;
import com.entseeker.Hotel.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);

}
