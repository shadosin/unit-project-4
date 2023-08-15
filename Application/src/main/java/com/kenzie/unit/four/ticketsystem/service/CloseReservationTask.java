package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.repositories.model.ReserveTicketRecord;
import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CloseReservationTask implements Runnable {

    private final Integer durationToPay;
    private final ConcurrentLinkedQueue<ReservedTicket> reservedTicketsQueue;
    private final ReservedTicketService reservedTicketService;

    public CloseReservationTask(Integer durationToPay,
                                ReservedTicketService reservedTicketService,
                                ConcurrentLinkedQueue<ReservedTicket> reservedTicketsQueue) {
        this.durationToPay = durationToPay;
        this.reservedTicketService = reservedTicketService;
        this.reservedTicketsQueue = reservedTicketsQueue;
    }

    @Override
    public void run() {
        // Your code here
        ReservedTicket reservedTicket = reservedTicketsQueue.poll();

        if (reservedTicket != null) {

            if (!reservedTicket.getTicketPurchased()) {
                LocalDateTime reservedTime = LocalDateTime.parse(reservedTicket.getDateOfReservation());
                LocalDateTime currentTime = LocalDateTime.now();
                long differenceInSeconds = Duration.between(reservedTime, currentTime).getSeconds();

                if (differenceInSeconds >= durationToPay) {
                    ReservedTicket update = new ReservedTicket(reservedTicket.getConcertId(), reservedTicket.getTicketId(),
                            reservedTicket.getDateOfReservation(), true, currentTime.toString(),false);
                    // Update the reserved ticket record in the repository
                    reservedTicketService.updateReserveTicket(update);
                } else {
                    // Ticket has not exceeded duration, re-add to the queue
                    reservedTicketsQueue.add(reservedTicket);
                }
            }
        }
    }
}