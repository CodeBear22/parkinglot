package com.kunal.parkingLot.requestHandler;

import com.kunal.parkingLot.constants.CommandInputMap;
import com.kunal.parkingLot.exception.ParkingException;
import com.kunal.parkingLot.service.Service;

public interface RequestHandler
{
	public void setService(Service service);
	
	public void execute(String action) throws ParkingException;
	
	public default boolean validate(String inputString)
	{
		boolean valid = true;
		try
		{
			//To split input string into params which includes command name and argumes
			String[] inputs = inputString.split(" ");

			//To check if command name exist and how many param it requires
			int params = CommandInputMap.getCommandsParameterMap().get(inputs[0]);
			switch (inputs.length)
			{
				case 1:
					if (params != 0) // e.g status
						valid = false;
					break;
				case 2:
					if (params != 1) // create_parking_lot 6
						valid = false;
					break;
				case 3:
					if (params != 2) // park DL-1C-K-2211
						valid = false;
					break;
				default:
					valid = false;
			}
		}
		catch (Exception e)
		{
			valid = false;
		}
		return valid;
	}
}
