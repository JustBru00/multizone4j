package com.rbrubaker.multizone4j;

import java.util.ArrayList;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.net.AbstractSerialConnection;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;

/**
 * This class represents a single Bacharach MultiZone device.
 * @author Justin Brubaker
 *
 */
public class MultiZoneDevice {

	private int modbusAddress;		
	
	public MultiZoneDevice(int _modbusAddress) {
		super();
		modbusAddress = _modbusAddress;
	}	
	
	/**
	 * This method is blocks as it has to contact the modbus device
	 * Gets the current zone ppm and alarm status for the given zone number.
	 * @param zoneNumber Zone numbers start with 0. So zone 1 is zonenumber=0.
	 * @return
	 * @throws ModbusException, Exception
	 * 
	 */
	public CurrentZoneStatus getCurrentZoneStatus(int zoneNumber) throws ModbusException, Exception, IllegalArgumentException {
		if (zoneNumber < 0 || zoneNumber > 15) {
			throw new IllegalArgumentException("The zone number must be between 0-15");
		}
		
		ModbusSerialMaster master;
		SerialParameters params = new SerialParameters("/dev/serial1", 19200, AbstractSerialConnection.FLOW_CONTROL_DISABLED,
				AbstractSerialConnection.FLOW_CONTROL_DISABLED, 8, AbstractSerialConnection.ONE_STOP_BIT, AbstractSerialConnection.NO_PARITY, false);
		master = new ModbusSerialMaster(params);
		master.connect();
		
		InputRegister[] ppmRegs = master.readMultipleRegisters(modbusAddress, 2001 + zoneNumber, 1);
		int ppmZone = ppmRegs[0].getValue();
		
		InputRegister[] alarmRegs = master.readMultipleRegisters(modbusAddress, 2017 + zoneNumber, 1);
		int alarmLevel = alarmRegs[0].getValue();
		
		CurrentZoneStatus zone = new CurrentZoneStatus(ppmZone, alarmLevel);
		
		master.disconnect();
		
		return zone;
	}
	
	/**
	 * This method blocks as it has to contact the modbus device.	 
	 * @return
	 * @throws ModbusException
	 * @throws Exception
	 */
	public ArrayList<CurrentZoneStatus> getAllCurrentZoneStatuses() throws ModbusException, Exception {
		ArrayList<CurrentZoneStatus> zones = new ArrayList<CurrentZoneStatus>();
		
		ModbusSerialMaster master;
		SerialParameters params = new SerialParameters("/dev/serial1", 19200, AbstractSerialConnection.FLOW_CONTROL_DISABLED,
				AbstractSerialConnection.FLOW_CONTROL_DISABLED, 8, AbstractSerialConnection.ONE_STOP_BIT, AbstractSerialConnection.NO_PARITY, false);
		master = new ModbusSerialMaster(params);
		master.connect();
		
		InputRegister[] ppmRegs = master.readMultipleRegisters(modbusAddress, 2001, 16);
		InputRegister[] alarmRegs = master.readMultipleRegisters(modbusAddress, 2017, 16);
		
		master.disconnect();
		
		for (int i = 0; i > 16; i++) {
			CurrentZoneStatus zone = new CurrentZoneStatus(ppmRegs[i].getValue(), alarmRegs[i].getValue());
			zones.add(zone);
		}		
		
		return zones;
	}
	
}
