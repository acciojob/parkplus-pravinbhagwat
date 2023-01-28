package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
//        ReservationController: Reserve a spot in a given parking lot for a specific user and vehicle, ensuring that the total price for the reservation is minimized.
//        The price per hour for each spot is different, and the vehicle can only be parked in a spot with a type that is equal to or larger than the given vehicle.
//        In the event that the parking lot is not found, the user is not found or no spot is available, the system should throw an exception indicating that the reservation cannot be made.

        if(!userRepository3.findById(userId).isPresent() || !parkingLotRepository3.findById(parkingLotId).isPresent()) {
            return null;
        }

        User user = userRepository3.findById(userId).get();
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();


//        Getting the spot list from parkingLot
        List<Spot> spotList = parkingLot.getSpotList();
        if(spotList.size() == 0) return null;


        SpotType requiredSpotType = null;

//        Getting required spot type
        if(numberOfWheels <= 2) requiredSpotType = SpotType.TWO_WHEELER;
        else if (numberOfWheels <= 4 ) requiredSpotType = SpotType.FOUR_WHEELER;
        else requiredSpotType = SpotType.OTHERS;

        int minPrice = Integer.MAX_VALUE;
        Spot selectedSpot = null;

//        Finding the spot which satisfy the condition
//        1. total price should be minimum
//        2. Vehicle can be spot in equal and greater type

        for (Spot spot : spotList) {
            if (!spot.getOccupied()) {
                if(spot.getSpotType() == SpotType.OTHERS) {
                    int parkingPrice = spot.getPricePerHour()*timeInHours;
                    if(parkingPrice < minPrice) {
                        minPrice = parkingPrice;
                        selectedSpot = spot;
                    }
                }
                if(spot.getSpotType() == SpotType.FOUR_WHEELER && numberOfWheels <=4) {
                    int parkingPrice = spot.getPricePerHour()*timeInHours;
                    if(parkingPrice < minPrice) {
                        minPrice = parkingPrice;
                        selectedSpot = spot;
                    }
                }
                if (spot.getSpotType() == SpotType.TWO_WHEELER && numberOfWheels <= 2) {
                    int parkingPrice = spot.getPricePerHour()*timeInHours;
                    if(parkingPrice < minPrice) {
                        minPrice = parkingPrice;
                        selectedSpot = spot;
                    }
                }
            }
        }

        if(selectedSpot == null) return null;

        Reservation reservation = new Reservation(timeInHours, user, selectedSpot);
        selectedSpot.setOccupied(Boolean.TRUE);
        selectedSpot.getReservationList().add(reservation);
        user.getReservationList().add(reservation);

        userRepository3.save(user);
        spotRepository3.save(selectedSpot);

        return reservation;
    }
}
