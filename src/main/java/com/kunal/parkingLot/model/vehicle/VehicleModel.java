package com.kunal.parkingLot.model.vehicle;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Interface for all types of vehicle
 *
 */
public abstract class VehicleModel implements Externalizable
{
	private String	registrationNo	= null;
	private Date entryTime;
	
	public VehicleModel(String registrationNo, Date entryTime)
	{
		this.registrationNo = registrationNo;
		this.entryTime = entryTime;
	}
	
	@Override
	public String toString()
	{
		return "[registrationNo=" + registrationNo + "]";
	}
	
	/**
	 * @return the registrationNo
	 */
	public String getRegistrationNo()
	{
		return registrationNo;
	}
	
	/**
	 * @param registrationNo
	 *            the registrationNo to set
	 */
	public void setRegistrationNo(String registrationNo)
	{
		this.registrationNo = registrationNo;
	}

	public void setEntryTime(Date entryTime)
	{
		this.entryTime = entryTime;
	}

	public Date getEntryTime()
	{
		return this.entryTime;
	}


	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeObject(getRegistrationNo());
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		setRegistrationNo((String) in.readObject());
	}

}
