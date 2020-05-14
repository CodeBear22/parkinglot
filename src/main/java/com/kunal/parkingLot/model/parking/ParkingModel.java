/**
 * 
 */
package com.kunal.parkingLot.model.parking;

/**
 * Interface for parking model/strategy
 *
 */
public interface ParkingModel
{
	public void add(int i);
	
	public int getSlot();
	
	public void removeSlot(int slot);
}
