package com.kunal.parkingLot.requestHandler;

import com.kunal.parkingLot.constants.Constants;
import com.kunal.parkingLot.exception.ErrorCode;
import com.kunal.parkingLot.exception.ParkingException;
import com.kunal.parkingLot.model.vehicle.Car;
import com.kunal.parkingLot.service.Service;
import com.kunal.parkingLot.service.ParkingService;

import java.util.Date;


public class ProcessRequest implements RequestHandler
{
	private ParkingService parkingService;

	/**
	 * To set the parking service
	 * @param parkingService
	 * @throws ParkingException
	 */
	public void setParkingService(ParkingService parkingService) throws ParkingException
	{
		this.parkingService = parkingService;
	}

	/**
	 * To execute input line command
	 * @param input
	 * @throws ParkingException
	 */

	@Override
	public void execute(String input) throws ParkingException
	{
		int level = 1;
		String[] inputs = input.split(" ");
		String key = inputs[0];

		//Identifying the command and executing accordingly
		switch (key)
		{
			//Create empty parking lot
			case Constants.CREATE_PARKING_LOT:
				try
				{
					int capacity = Integer.parseInt(inputs[1]);
					parkingService.createParkingLot(level, capacity);
				}
				catch (NumberFormatException e)
				{
					throw new ParkingException(ErrorCode.INVALID_VALUE.getMessage().replace("{variable}", "capacity"));
				}
				break;

			//Park vehicle in parking lot
			case Constants.PARK:
				parkingService.park(level, new Car(inputs[1], new Date()));
				break;

			//Removing vehicle from parking lot Or freeing the parking slot
			case Constants.LEAVE:
				try
				{
					String registrationNumber = inputs[1];
					int hours = Integer.parseInt(inputs[2]);
					parkingService.unPark(level, registrationNumber, hours);
				}
				catch (NumberFormatException e)
				{
					throw new ParkingException(
							ErrorCode.INVALID_VALUE.getMessage().replace("{variable}", "slot_number"));
				}
				break;

			//To show the status of the parking lot
			case Constants.STATUS:
				parkingService.getStatus(level);
				break;

				//To find the slot number by registration number of the vehicle
			case Constants.SLOTS_NUMBER_FOR_REG_NUMBER:
				parkingService.getSlotNoFromRegistrationNo(level, inputs[1]);
				break;
			default:
				break;
		}
	}
	
	@Override
	public void setService(Service service)
	{
		this.parkingService = (ParkingService) service;
	}
}
