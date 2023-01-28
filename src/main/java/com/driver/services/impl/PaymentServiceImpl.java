package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
//        PaymentController: Implement a payment functionality that allows users to make a payment for a specific reservation using different modes of payment (cash, card, or UPI). The system should validate the amount sent by the user and compare it with the bill amount for the reservation
//        If the amount sent is less than the bill, the system should throw an exception "Insufficient Amount"
//        Additionally, the system should validate the payment mode entered by the user and only accept "cash", "card", or "UPI" as valid modes of payment. Note that the characters of these strings can be in uppercase or lowercase. For example "cAsH" is a valid mode of payment. If the user enters a mode other than these, the system should throw an exception "Payment mode not detected"
//        The system should also update the payment attributes for the reservation after a successful payment.

        if (!reservationRepository2.findById(reservationId).isPresent()) return null;
        mode = mode.toUpperCase();
        PaymentMode givenPaymentMode;
        if(mode.equals("CASH") ) {
          givenPaymentMode = PaymentMode.CASH;
        }
        else if(mode.equals("CARD")){
            givenPaymentMode = PaymentMode.CARD;
        }
        else if (mode.equals("UPI")){
            givenPaymentMode = PaymentMode.UPI;
        }else {
            throw new Exception("Insufficient Amount");
        }

        Reservation reservation = reservationRepository2.findById(reservationId).get();

        int resBill = reservation.getSpot().getPricePerHour()*reservation.getNumberOfHours();

        if(amountSent < resBill) {
            throw new Exception("Insufficient Amount");
        }

        Payment newPayment = new Payment(Boolean.TRUE, givenPaymentMode, reservation);
        reservation.setPayment(newPayment);
        reservationRepository2.save(reservation);
        return newPayment;
    }
}
