package com.rbrubaker.multizone4j;

import com.ghgande.j2mod.modbus.ModbusException;

public class TestModbusRead {

	public static void main(String[] args) throws IllegalArgumentException, ModbusException, Exception {
		// This object represents a Bacharach MultiZone device on the modbus serial network.
		// /dev/serial1 - This is the default RS485 port name on the Comfile PI 
		MultiZoneDevice one = new MultiZoneDevice(1, "/dev/serial1");
		
		// Read the current ppm and alarm status for zone 1.
		CurrentZoneStatus single = one.getCurrentZoneStatus(0);
		System.out.println(String.format("Single Read - Zone 1: %s ppm - %s alarm status", single.getPPM() + "", single.getAlarmStatus() + ""));
		
		int address = 1;
		for (CurrentZoneStatus status : one.getAllCurrentZoneStatuses()) {			
			System.out.println(String.format("Zone %s: %s ppm - %s alarm status", address + "", status.getPPM() + "", status.getAlarmStatus() + ""));
			address++;
		}

		
	}

}
