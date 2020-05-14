package com.kunal.parkingLot.dao;

import com.kunal.parkingLot.model.vehicle.VehicleModel;

import java.util.List;

/**
 *Interface for parking storage manager
 *
 */
public interface ParkingDataManager<T extends VehicleModel>
{
	public int parkVehicle(int level, T vehicle);
	
	public boolean leaveVehicle(int level, int slotNumber);
	
	public List<String> getStatus(int level);
	
//	public List<String> getRegNumberForColor(int level, String color);
	
//	public List<Integer> getSlotNumbersFromColor(int level, String colour);
	
	public int getSlotNoFromRegistrationNo(int level, String registrationNo);
	
	public int getAvailableSlotsCount(int level);
	
	public void doCleanup();
}
