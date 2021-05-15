package com.intuit.craft.booking.controller;

import com.intuit.craft.booking.domain.BookingRequest;
import com.intuit.craft.booking.domain.EventSeatMapper;
import com.intuit.craft.booking.service.BookingEventService;
import com.intuit.craft.booking.service.GrantBookingRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/booking")
@Validated
@Api(value = "CRUD operation on booking", tags = {"API :: Booking"})
public class BookingController {

    private final GrantBookingRequestService grantBookingRequestService;
    private final BookingEventService bookingEventService;

    /**
     * Grant lock if seats are available based on parameter provided in request body {@link BookingRequest}
     *
     * @param bookingRequest {@link BookingRequest}
     * @return
     */
    @ApiOperation(value = "Grant lock if seats are available")
    @PostMapping(path = "/grant")
    public ResponseEntity<Object> grantBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        boolean granted = grantBookingRequestService.grant(bookingRequest);
        if(granted){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    /**
     * API to retrieve over all seat status
     *
     * @param eventId
     * @param subEventId
     * @return
     */
    @ApiOperation(value = "Returns list of all lists based on movie and show time")
    @GetMapping("/all")
    public ResponseEntity<List<EventSeatMapper>> retrieveCurrentBookingStatus(@RequestParam String eventId, @RequestParam String subEventId) {
        return ResponseEntity.of(bookingEventService.retrieveAllSeats(eventId, subEventId));
    }

}
