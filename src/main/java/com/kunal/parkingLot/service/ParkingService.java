package com.kunal.parkingLot.service;

import java.util.Optional;

import com.kunal.parkingLot.exception.ParkingException;
import com.kunal.parkingLot.model.vehicle.VehicleModel;

/**
 * Parking lot parking service interface
 */
public interface ParkingService extends Service
{

	public void createParkingLot(int level, int capacity) throws ParkingException;
	
	public Optional<Integer> park(int level, VehicleModel vehicleModel) throws ParkingException;
	
//	public void unPark(int level, int slotNumber) throws ParkingException;

	public void unPark(int level, String registrationNumber, int hours) throws ParkingException;
	
	public void getStatus(int level) throws ParkingException;
	
	public Optional<Integer> getAvailableSlotsCount(int level) throws ParkingException;
	
	public int getSlotNoFromRegistrationNo(int level, String registrationNo) throws ParkingException;
	
	public void doCleanup();
}
