package com.entseeker.Hotel.request.roomRequests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class RoomRequest {
    private MultipartFile photo;
    private String roomType;
    private BigDecimal roomPrice;
    private String roomDescription;
}