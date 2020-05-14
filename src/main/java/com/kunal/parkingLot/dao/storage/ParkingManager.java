package com.kunal.parkingLot.dao.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kunal.parkingLot.dao.ParkingDataManager;
import com.kunal.parkingLot.dao.ParkingLevelDataManager;
import com.kunal.parkingLot.model.parking.FirstFreeSlotModel;
import com.kunal.parkingLot.model.parking.ParkingModel;
import com.kunal.parkingLot.model.vehicle.VehicleModel;

/**
 * This class is a singleton class to manage the dao of parking system
 * @param <T>
 */
public class ParkingManager<T extends VehicleModel> implements ParkingDataManager<T>
{
	private Map<Integer, ParkingLevelDataManager<T>> levelParkingMap;
	
	@SuppressWarnings("rawtypes")
	private static ParkingManager instance = null;

	/**
	 * To create/fetch object of singleton parking storage manager class
	 * @param parkingLevels
	 * @param capacityList
	 * @param parkingStrategies
	 * @param <T>
	 * @return
	 */
	public static <T extends VehicleModel> ParkingManager<T> getInstance(List<Integer> parkingLevels,
																		 List<Integer> capacityList, List<ParkingModel> parkingStrategies)
	{
		// Make sure the each of the lists are of equal size
		if (instance == null)
		{
			synchronized (ParkingManager.class)
			{
				if (instance == null)
				{
					instance = new ParkingManager<T>(parkingLevels, capacityList, parkingStrategies);
				}
			}
 		}
		return instance;
	}

	/**
	 * To create parking lot, its manager and initializing all provided levels of parking.
	 * @param parkingLevels
	 * @param capacityList
	 * @param parkingStrategies
	 */
	private ParkingManager(List<Integer> parkingLevels, List<Integer> capacityList,
						   List<ParkingModel> parkingStrategies)
	{
		if (levelParkingMap == null)
			levelParkingMap = new HashMap<>();

		//Initializing all parking level for this parking manager
		for (int i = 0; i < parkingLevels.size(); i++)
		{
			levelParkingMap.put(parkingLevels.get(i), ParkingLevelManager.getInstance(parkingLevels.get(i),
					capacityList.get(i), parkingStrategies.get(i)));
		}
	}

	/**
	 * To park vehicle on the level
	 * @param level
	 * @param vehicle
	 * @return
	 */
	@Override
	public int parkVehicle(int level, T vehicle)
	{
		return levelParkingMap.get(level).parkVehicle(vehicle);
	}

	/**
	 * freeing parking space from parking lot level
	 * @param level
	 * @param slotNumber
	 * @return
	 */
	@Override
	public boolean leaveVehicle(int level, int slotNumber)
	{
		return levelParkingMap.get(level).leaveVehicle(slotNumber);
	}

	/**
	 * To get the status of parking lot level
	 * @param level
	 * @return
	 */
	@Override
	public List<String> getStatus(int level)
	{
		return levelParkingMap.get(level).getStatus();
	}

	/**
	 * To fetch available free slots available in the parking slot level
	 * @param level
	 * @return
	 */
	public int getAvailableSlotsCount(int level)
	{
		return levelParkingMap.get(level).getAvailableSlotsCount();
	}

	/**
	 * To find the slot number of the vehicle with the given registration number
	 * @param level
	 * @param registrationNo
	 * @return
	 */
	@Override
	public int getSlotNoFromRegistrationNo(int level, String registrationNo)
	{
		return levelParkingMap.get(level).getSlotNoFromRegistrationNo(registrationNo);
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	public void doCleanup()
	{
		for (ParkingLevelDataManager<T> levelDataManager : levelParkingMap.values())
		{
			levelDataManager.doCleanUp();
		}
		levelParkingMap = null;
		instance = null;
	}
}
