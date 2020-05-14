package com.kunal.parkingLot.dao.storage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.kunal.parkingLot.constants.Constants;
import com.kunal.parkingLot.dao.ParkingLevelDataManager;
import com.kunal.parkingLot.model.parking.FirstFreeSlotModel;
import com.kunal.parkingLot.model.parking.ParkingModel;
import com.kunal.parkingLot.model.vehicle.VehicleModel;

/**
 * This class is a singleton class to manage the dao of parking system
 * Implementation of parking lot level/floor
 *
 * @param <T>
 */
public class ParkingLevelManager<T extends VehicleModel> implements ParkingLevelDataManager<T>
{
	// For Multilevel Parking lot - 0 -> Ground floor 1 -> First Floor etc
	private AtomicInteger	level			= new AtomicInteger(0);
	private AtomicInteger	capacity		= new AtomicInteger();
	private AtomicInteger	availability	= new AtomicInteger();
	// Allocation Strategy for parking
	private ParkingModel parkingModel;
	// this is per level - slot - vehicle
	private Map<Integer, Optional<T>> slotVehicleMap;
	
	@SuppressWarnings("rawtypes")
	private static ParkingLevelManager instance = null;

	/**
	 * Parking level instance creation for parking manager
	 * @param level
	 * @param capacity
	 * @param parkingModel
	 * @param <T>
	 * @return
	 */
	public static <T extends VehicleModel> ParkingLevelManager<T> getInstance(int level, int capacity,
																			  ParkingModel parkingModel)
	{
		if (instance == null)
		{
			synchronized (ParkingLevelManager.class)
			{
				if (instance == null)
				{
					instance = new ParkingLevelManager<T>(level, capacity, parkingModel);
				}
			}
		}
		return instance;
	}

	/**
	 * Creating parking level with the capacity, level ID and parking strategy
	 * @param level
	 * @param capacity
	 * @param parkingModel
	 */
	private ParkingLevelManager(int level, int capacity, ParkingModel parkingModel)
	{
		this.level.set(level);
		this.capacity.set(capacity);
		this.availability.set(capacity);
		this.parkingModel = parkingModel;
		if (parkingModel == null)
			parkingModel = new FirstFreeSlotModel();
		slotVehicleMap = new ConcurrentHashMap<>();
		for (int i = 1; i <= capacity; i++)
		{
			slotVehicleMap.put(i, Optional.empty());
			parkingModel.add(i);
		}
	}

	/**
	 * Parking car on this level
	 * @param vehicle
	 * @return
	 */
	@Override
	public int parkVehicle(T vehicle)
	{
		int availableSlot;
		if (availability.get() == 0)
		{
			return Constants.NOT_AVAILABLE;
		}
		else
		{
			availableSlot = parkingModel.getSlot();
			if (slotVehicleMap.containsValue(Optional.of(vehicle)))
				return Constants.VEHICLE_ALREADY_EXIST;
			
			slotVehicleMap.put(availableSlot, Optional.of(vehicle));
			availability.decrementAndGet();
			parkingModel.removeSlot(availableSlot);
		}
		return availableSlot;
	}


	/**
	 * Freeing the parking slot
	 * @param slotNumber
	 * @return
	 */
	@Override
	public boolean leaveVehicle(int slotNumber)
	{
		if (!slotVehicleMap.get(slotNumber).isPresent()) // Slot already empty
			return false;

//		Optional<T> vehicle = slotVehicleMap.get(slotNumber);
//		int charge = this.calculateParkingCharge(vehicle.get().getEntryTime());
//		System.out.println("Registration number "+ vehicle.get().getRegistrationNo() +" with Slot Number "+ slotNumber +" is free with Charge " + charge);
		slotVehicleMap.get(slotNumber);
		availability.incrementAndGet();
		parkingModel.add(slotNumber);
		slotVehicleMap.put(slotNumber, Optional.empty());
		return true;
	}

	/**
	 * Getting parking status of this level.
	 * @return
	 */
	@Override
	public List<String> getStatus()
	{
		List<String> statusList = new ArrayList<>();
		for (int i = 1; i <= capacity.get(); i++)
		{
			Optional<T> vehicle = slotVehicleMap.get(i);
			if (vehicle.isPresent())
			{
				statusList.add(i + "           " + vehicle.get().getRegistrationNo());
			}
		}
		return statusList;
	}

	/**
	 * to get available slots count
	 * @return
	 */
	public int getAvailableSlotsCount()
	{
		return availability.get();
	}

	/**
	 * To fetch slot number of a vehicle registration number
	 * @param registrationNo
	 * @return
	 */
	@Override
	public int getSlotNoFromRegistrationNo(String registrationNo)
	{
		int result = Constants.NOT_FOUND;
		for (int i = 1; i <= capacity.get(); i++)
		{
			Optional<T> vehicle = slotVehicleMap.get(i);
			if (vehicle.isPresent() && registrationNo.equalsIgnoreCase(vehicle.get().getRegistrationNo()))
			{
				result = i;
			}
		}
		return result;
	}

	public int calculateParkingCharge(Date entryTime)
	{
		int charge = Constants.BASE_PARKING_CHARGE;
		double diffHours =  new Date().getTime() - entryTime.getTime();
		diffHours = diffHours / (60 * 60 * 1000);

		if(diffHours <= Constants.BASE_PARKING_HOURS)
		{
			return charge;
		} else {
			diffHours -= 2;
			return charge += Constants.ADDITIONAL_HOUR_PARKING_CHARGE * (int)diffHours;
		}



	}
	
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	@Override
	public void doCleanUp()
	{
		this.level = new AtomicInteger();
		this.capacity = new AtomicInteger();
		this.availability = new AtomicInteger();
		this.parkingModel = null;
		slotVehicleMap = null;
		instance = null;
	}
}
