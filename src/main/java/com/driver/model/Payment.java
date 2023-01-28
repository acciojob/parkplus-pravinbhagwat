package com.driver.model;

import javax.persistence.*;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Boolean isPaymentCompleted;
    private PaymentMode paymentMode;

    @OneToOne
    @JoinColumn
    private Reservation reservation;

    public Payment() {
    }

    public Payment(Boolean paymentComplete, PaymentMode paymentMode, Reservation reservation) {
        this.isPaymentCompleted = paymentComplete;
        this.paymentMode = paymentMode;
        this.reservation = reservation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean isPaymentCompleted() {
        return isPaymentCompleted;
    }

    public void setIsPaymentCompleted(Boolean isPaymentCompleted) {
        this.isPaymentCompleted = isPaymentCompleted;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
