package com.rbrubaker.multizone4j;

import com.ghgande.j2mod.modbus.ModbusException;
import com.rbrubaker.multizone4j.reference.CurrentState;

public class TestMultiZoneDeviceMethods {

	public static void main(String[] args) throws IllegalArgumentException, ModbusException, Exception {
		MultiZoneDevice one = new MultiZoneDevice(1, "/dev/serial1", 9600);
		
		// Read the current ppm and alarm status for zone 1.
		CurrentZoneStatus single = one.getCurrentZoneStatus(0);
		System.out.println(String.format("Single Read - Zone 1: %s ppm - %s alarm status", single.getPPM() + "", single.getAlarmStatus() + ""));
				
		int address = 1;
		for (CurrentZoneStatus status : one.getAllCurrentZoneStatuses()) {			
			System.out.println(String.format("Zone %s: %s ppm - %s alarm status", address + "", status.getPPM() + "", status.getAlarmStatus() + ""));
			address++;
		}				
				
		System.out.println("OperatingMode: " + one.getCurrentOperatingMode());		
				
		System.out.println("Current State: " + CurrentState.getValueOf(one.getCurrentState()));		
				
		System.out.println("Active Zone: " + one.getActiveZone());		
				
		System.out.println("Max Alarm: " + one.getMaxAlarm());		
				
		System.out.println("Active Alarm Count: " + one.getActiveAlarmCount());		
		
		System.out.println("Acknowledged Alarm Count: " + one.getAcknowledgedAlarmCount());
		
		System.out.println("Fault Code Status: " + one.getFaultCodeStatus());
		
		System.out.println("Fault Code: " + one.getFaultCode());
		
		System.out.println("Audible Alarm: " + one.isAudibleAlarm());
		
		System.out.println("Silenced: " + one.isSilenced());
		
		System.out.println("Highest Conc Zone: " + one.getHighestConcentrationZone());
		
		System.out.println("Highest Conc: " + one.getHighestConcentration());
		
		System.out.println("Zones Installed: " + one.getZonesInstalled());
		
		System.out.println("Leak Level Setpoint Zone 1: " + one.getLeakLevelSetpoint(0));	
		
	}	
	
}
