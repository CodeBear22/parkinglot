package com.kunal.parkingLot.model.vehicle;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

/**
 *Implementation of Vehicle as car
 *
 */
public class Car extends VehicleModel
{
	
	public Car(String registrationNo, Date entryTime)
	{
		super(registrationNo, entryTime);
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		super.writeExternal(out);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		super.readExternal(in);
	}
}
