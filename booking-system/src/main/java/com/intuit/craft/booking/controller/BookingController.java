package com.intuit.craft.booking.controller;

import com.intuit.craft.booking.domain.Booking;
import com.intuit.craft.booking.domain.BookingLockRequest;
import com.intuit.craft.booking.domain.BookingLockResponse;
import com.intuit.craft.booking.domain.BookingRequest;
import com.intuit.craft.booking.domain.EventSeatMapper;
import com.intuit.craft.booking.service.BookingEventService;
import com.intuit.craft.booking.service.GrantBookingRequestService;
import com.intuit.craft.booking.service.TokenService;
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
import java.util.Map;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/booking")
@Validated
@Api(value = "CRUD operation on booking", tags = {"API :: Booking"})
public class BookingController {

    private final GrantBookingRequestService grantBookingRequestService;
    private final BookingEventService bookingEventService;
    private final TokenService tokenService;

    /**
     * Grant lock if seats are available based on parameter provided in request body {@link BookingLockRequest}
     *
     * @param bookingLockRequest {@link BookingLockRequest}
     * @return
     */
    @ApiOperation(value = "Grant lock if seats are available")
    @PostMapping(path = "/grant")
    public ResponseEntity<BookingLockResponse> grantBooking(@Valid @RequestBody BookingLockRequest bookingLockRequest) {
        log.info("Getting booking lock request {}", bookingLockRequest);
        String transactionId = UUID.randomUUID().toString();
        boolean granted = grantBookingRequestService.grant(transactionId, bookingLockRequest);
        if (granted) {
            return ResponseEntity.ok().body(new BookingLockResponse(transactionId, tokenService.generateToken(transactionId, bookingLockRequest)));
        }
        log.info("sending back response");
        return ResponseEntity.badRequest().build();
    }


    /**
     * API to retrieve over all seat status
     *
     * @param theaterId
     * @param subEventId
     * @return
     */
    @ApiOperation(value = "Returns list of all lists based on movie and show time against theater id and sub event id")
    @GetMapping("/all")
    public ResponseEntity<List<EventSeatMapper>> retrieveCurrentBookingStatus(@RequestParam String theaterId, @RequestParam String subEventId) {
        return ResponseEntity.of(bookingEventService.retrieveAllSeats(theaterId, subEventId));
    }

    @ApiOperation(value = "Book seats")
    @PostMapping(path = "/order")
    public ResponseEntity book(@Valid @RequestBody BookingRequest bookingRequest) {
        log.info("Getting booking request {}", bookingRequest);
        boolean bookingDone = bookingEventService.booking(bookingRequest);
        return bookingDone ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @ApiOperation(value = "Returns all booking history")
    @GetMapping("/history")
    public ResponseEntity<List<Booking>> retrieveBooking() {
        return ResponseEntity.of(bookingEventService.findAll());
    }
}
