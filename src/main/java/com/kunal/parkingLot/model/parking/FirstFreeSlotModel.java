/**
 * 
 */
package com.kunal.parkingLot.model.parking;

import java.util.TreeSet;

public class FirstFreeSlotModel implements ParkingModel
{
	private TreeSet<Integer> freeSlots;
	
	public FirstFreeSlotModel()
	{
		freeSlots = new TreeSet<Integer>();
	}
	
	@Override
	public void add(int i)
	{
		freeSlots.add(i);
	}
	
	@Override
	public int getSlot()
	{
		return freeSlots.first();
	}
	
	@Override
	public void removeSlot(int availableSlot)
	{
		freeSlots.remove(availableSlot);
	}
}
