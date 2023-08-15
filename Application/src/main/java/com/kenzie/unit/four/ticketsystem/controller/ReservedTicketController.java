package com.kenzie.unit.four.ticketsystem.controller;

import com.kenzie.unit.four.ticketsystem.controller.model.ReservedTicketCreateRequest;
import com.kenzie.unit.four.ticketsystem.controller.model.ReservedTicketResponse;
import com.kenzie.unit.four.ticketsystem.service.ReservedTicketService;
import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/reservedtickets")
public class ReservedTicketController {

    private ReservedTicketService reservedTicketService;

    ReservedTicketController(ReservedTicketService reservedTicketService) {
        this.reservedTicketService = reservedTicketService;
    }

    // TODO - Task 2: reserveTicket() - POST
    // Add the correct annotation
    @PostMapping
    public ResponseEntity<ReservedTicketResponse> reserveTicket(
            @RequestBody ReservedTicketCreateRequest reservedTicketCreateRequest) {

        // Add your code here
        ReservedTicket reservedTicket = new ReservedTicket(reservedTicketCreateRequest.getConcertId(),
                                                            randomUUID().toString(),
                                                            LocalDateTime.now().toString());
        reservedTicketService.reserveTicket(reservedTicket);

        ReservedTicketResponse reservedTicketResponse = new ReservedTicketResponse();
        reservedTicketResponse.setTicketId(reservedTicket.getTicketId());
        reservedTicketResponse.setReservationClosed(reservedTicket.getReservationClosed());
        reservedTicketResponse.setDateOfReservation(reservedTicket.getDateOfReservation());
        reservedTicketResponse.setDateReservationClosed(reservedTicket.getDateReservationClosed());
        reservedTicketResponse.setPurchasedTicket(reservedTicket.getTicketPurchased());
        reservedTicketResponse.setConcertId(reservedTicket.getConcertId());


        // Return your ReservedTicketResponse instead of null
        return ResponseEntity.ok(reservedTicketResponse);
    }

    // TODO - Task 3: getAllReserveTicketsByConcertId() - GET `/concerts/{concertId}`
    // Add the correct annotation
    @GetMapping("/concerts/{concertId}")
    public ResponseEntity<List<ReservedTicketResponse>> getAllReserveTicketsByConcertId(
            @PathVariable("concertId") String concertId) {

        // Add your code here
        List<ReservedTicket> ticketsReserved = reservedTicketService.findByConcertId(concertId);
        List<ReservedTicketResponse> ticketResponses = new ArrayList<>();
        if(ticketsReserved.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        // Return your List<ReservedTicketResponse> instead of null
        for(ReservedTicket reservedTicket: ticketsReserved){

            ReservedTicketResponse reservedTicketResponse = new ReservedTicketResponse();
            reservedTicketResponse.setConcertId(reservedTicket.getConcertId());
            reservedTicketResponse.setReservationClosed(reservedTicket.getReservationClosed());
            reservedTicketResponse.setPurchasedTicket(reservedTicket.getTicketPurchased());
            reservedTicketResponse.setDateOfReservation(reservedTicketResponse.getDateOfReservation());
            reservedTicketResponse.setDateReservationClosed(reservedTicket.getDateReservationClosed());
            reservedTicketResponse.setTicketId(reservedTicketResponse.getTicketId());

            ticketResponses.add(reservedTicketResponse);
        }
        return ResponseEntity.ok(ticketResponses);
    }

}
