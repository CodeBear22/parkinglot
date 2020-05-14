package com.kunal.parkingLot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import com.kunal.parkingLot.requestHandler.ProcessRequest;
import com.kunal.parkingLot.requestHandler.RequestHandler;
import com.kunal.parkingLot.exception.ErrorCode;
import com.kunal.parkingLot.exception.ParkingException;
import com.kunal.parkingLot.service.implementation.ParkingServiceImpl;


public class Main {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Please provide input file path and name in the argument");
            return;
        }

        RequestHandler processor = new ProcessRequest();
        processor.setService(new ParkingServiceImpl());
        BufferedReader bufferReader = null;
        String input = null;
        try {
            File inputFile = new File(args[0]);
            try {
                bufferReader = new BufferedReader(new FileReader(inputFile));
                int lineNo = 1;
                while ((input = bufferReader.readLine()) != null) {
                    input = input.trim();

                    //Validating input line
                    if (processor.validate(input)) {
                        try {
                            processor.execute(input);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    } else
                        System.out.println("Invalid command line at line number : " + lineNo + " --line-- " + input);
                    lineNo++;
                }
            } catch (Exception e) {
                throw new ParkingException(ErrorCode.INVALID_FILE.getMessage(), e);
            }
        } catch (ParkingException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            try {
                if (bufferReader != null)
                    bufferReader.close();
            } catch (IOException e) {
            }
        }
    }
}
