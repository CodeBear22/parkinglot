package com.kunal.parkingLot.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.kunal.parkingLot.dao.ParkingDataManager;
import com.kunal.parkingLot.dao.storage.ParkingManager;
import com.kunal.parkingLot.model.parking.FirstFreeSlotModel;
import com.kunal.parkingLot.model.parking.ParkingModel;
import com.kunal.parkingLot.model.vehicle.VehicleModel;
import com.kunal.parkingLot.constants.Constants;
import com.kunal.parkingLot.exception.ErrorCode;
import com.kunal.parkingLot.exception.ParkingException;

/**
 * 
 * This class has to be made singleton and used as service to be injected in
 * ProcessRequest
 */
public class ParkingServiceImpl implements com.kunal.parkingLot.service.ParkingService
{
	private ParkingDataManager<VehicleModel> dataManager = null;
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * Creating parking lot with n levels and m capacity on each level
	 * @param level
	 * @param capacity
	 * @throws ParkingException
	 */
	@Override
	public void createParkingLot(int level, int capacity) throws ParkingException
	{
		if (dataManager != null)
			throw new ParkingException(ErrorCode.PARKING_ALREADY_EXIST.getMessage());
		List<Integer> parkingLevels = new ArrayList<>();
		List<Integer> capacityList = new ArrayList<>();
		List<ParkingModel> parkingStrategies = new ArrayList<>();
		parkingLevels.add(level);
		capacityList.add(capacity);

		//providing parking strategy. can be modified to set different parking strategy as per requirement
		parkingStrategies.add(new FirstFreeSlotModel());
		this.dataManager = ParkingManager.getInstance(parkingLevels, capacityList, parkingStrategies);
		System.out.println("Created parking lot with " + capacity + " slots");
	}

	/**
	 * To park vehicle on nth level
	 * @param level
	 * @param vehicleModel
	 * @return
	 * @throws ParkingException
	 */
	@Override
	public Optional<Integer> park(int level, VehicleModel vehicleModel) throws ParkingException
	{
		Optional<Integer> value = Optional.empty();
		lock.writeLock().lock();
		validateParkingLot();
		try
		{
			value = Optional.of(dataManager.parkVehicle(level, vehicleModel));
			if (value.get() == Constants.NOT_AVAILABLE)
				System.out.println("Sorry, parking lot is full");
			else if (value.get() == Constants.VEHICLE_ALREADY_EXIST)
				System.out.println("This vehicle already parked");
			else
				System.out.println("Allocated slot number: " + value.get());
		}
		catch (Exception e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.writeLock().unlock();
		}
		return value;
	}
	
	/**
	 * @throws ParkingException
	 */
	private void validateParkingLot() throws ParkingException
	{
		if (dataManager == null)
		{
			throw new ParkingException(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage());
		}
	}

//	/**
//	 * Freeing the parking slot
//	 * @param level
//	 * @param slotNumber
//	 * @throws ParkingException
//	 */
//	@Override
//	public void unPark(int level, int slotNumber) throws ParkingException
//	{
//		lock.writeLock().lock();
//		validateParkingLot();
//		try
//		{
//			if (!dataManager.leaveVehicle(level, slotNumber))
//				System.out.println("Slot number is Empty Already.");
//		}
//		catch (Exception e)
//		{
//			throw new ParkingException(ErrorCode.INVALID_VALUE.getMessage().replace("{variable}", "slot_number"), e);
//		}
//		finally
//		{
//			lock.writeLock().unlock();
//		}
//	}

	/**
	 *
	 * @param level
	 * @param registrationNumber
	 * @param hours
	 * @throws ParkingException
	 */
	@Override
	public void unPark(int level, String registrationNumber, int hours) throws ParkingException
	{
		lock.writeLock().lock();
		validateParkingLot();
		int slotNumber = this.getSlotNoFromRegistrationNo(level, registrationNumber);
		try
		{
			if(slotNumber != -1) {
				if (dataManager.leaveVehicle(level, slotNumber)) {
					int charge = Constants.BASE_PARKING_CHARGE;
					hours -= Constants.BASE_PARKING_HOURS;
					charge += Constants.ADDITIONAL_HOUR_PARKING_CHARGE * hours;
					System.out.println("Registration number " + registrationNumber + " with Slot Number " + slotNumber + " is free with Charge " + charge);
				} else
					System.out.println("Slot number is Empty Already.");
			} else {
				System.out.println("Registration number "+ registrationNumber +" not found");
			}
		}
		catch (Exception e)
		{
			throw new ParkingException(ErrorCode.INVALID_VALUE.getMessage().replace("{variable}", "slot_number"), e);
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	/**
	 * to show state of the parking lot level that how many free slots are available and vehicle with registration number
	 * @param level
	 * @throws ParkingException
	 */
	@Override
	public void getStatus(int level) throws ParkingException
	{
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			System.out.println("Slot No.    Registration No.");
			List<String> statusList = dataManager.getStatus(level);
			if (statusList.size() == 0)
				System.out.println("Parking lot is empty");
			else
			{
				for (String statusSting : statusList)
				{
					System.out.println(statusSting);
				}
			}
		}
		catch (Exception e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	/**
	 * To get the available free slots from parking lot level
	 * @param level
	 * @return
	 * @throws ParkingException
	 */
	public Optional<Integer> getAvailableSlotsCount(int level) throws ParkingException
	{
		lock.readLock().lock();
		Optional<Integer> value = Optional.empty();
		validateParkingLot();
		try
		{
			value = Optional.of(dataManager.getAvailableSlotsCount(level));
		}
		catch (Exception e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
		return value;
	}

	/**
	 * To get the slot number of the vehicle with the given registration number on the parking lot level
	 * @param level
	 * @param registrationNo
	 * @return
	 * @throws ParkingException
	 */
	@Override
	public int getSlotNoFromRegistrationNo(int level, String registrationNo) throws ParkingException
	{
		int value = -1;
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			value = dataManager.getSlotNoFromRegistrationNo(level, registrationNo);
//			System.out.println(value != -1 ? value : "Not Found");
		}
		catch (Exception e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
		return value;
	}
	
	@Override
	public void doCleanup()
	{
		if (dataManager != null)
			dataManager.doCleanup();
	}
}
