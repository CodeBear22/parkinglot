package com.kunal.parkingLot.dao;

import com.kunal.parkingLot.model.vehicle.VehicleModel;

import java.util.List;

/**
 * Parking level interface
 *
 */
public interface ParkingLevelDataManager<T extends VehicleModel>
{
	public int parkVehicle(T vehicle);
	
	public boolean leaveVehicle(int slotNumber);
	
	public List<String> getStatus();

	public int getSlotNoFromRegistrationNo(String registrationNo);
	
	public int getAvailableSlotsCount();
	
	public void doCleanUp();
}
