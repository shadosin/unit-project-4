package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.repositories.PurchaseTicketRepository;
import com.kenzie.unit.four.ticketsystem.repositories.model.PurchasedTicketRecord;
import com.kenzie.unit.four.ticketsystem.service.model.PurchasedTicket;

import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchasedTicketService {
    private PurchaseTicketRepository purchaseTicketRepository;
    private ReservedTicketService reservedTicketService;

    public PurchasedTicketService(PurchaseTicketRepository purchaseTicketRepository,
                                  ReservedTicketService reservedTicketService) {
        this.purchaseTicketRepository = purchaseTicketRepository;
        this.reservedTicketService = reservedTicketService;
    }

    public PurchasedTicket purchaseTicket(String reservedTicketId, Double pricePaid) {
        // Your code here
        ReservedTicket reservedTicket = reservedTicketService.findByReserveTicketId(reservedTicketId);

        if(reservedTicket == null || reservedTicket.getReservationClosed()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Concert not available");
        }

        PurchasedTicketRecord record = new PurchasedTicketRecord();
        record.setConcertId(reservedTicket.getConcertId());
        record.setTicketId(reservedTicket.getTicketId());
        record.setDateOfPurchase(LocalDateTime.now().toString());
        record.setPricePaid(pricePaid);
        purchaseTicketRepository.save(record);

        ReservedTicket updated = new ReservedTicket(
                reservedTicket.getConcertId(),
                reservedTicket.getTicketId(),
                reservedTicket.getDateOfReservation(),
                true,
                LocalDateTime.now().toString(),
                true);

        reservedTicketService.updateReserveTicket(updated);

        return new PurchasedTicket(record.getConcertId(), record.getTicketId(),
                record.getDateOfPurchase(), record.getPricePaid());
    }

    public List<PurchasedTicket> findByConcertId(String concertId) {
        List<PurchasedTicketRecord> purchasedTicketRecords = purchaseTicketRepository
                .findByConcertId(concertId);

        List<PurchasedTicket> purchasedTickets = new ArrayList<>();

        for (PurchasedTicketRecord purchasedTicketRecord : purchasedTicketRecords) {
            purchasedTickets.add(new PurchasedTicket(purchasedTicketRecord.getConcertId(),
                    purchasedTicketRecord.getTicketId(),
                    purchasedTicketRecord.getDateOfPurchase(),
                    purchasedTicketRecord.getPricePaid()));
        }

        return purchasedTickets;
    }
}
