package com.rbrubaker.multizone4j;

import java.util.ArrayList;
import java.util.Optional;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.net.AbstractSerialConnection;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import com.rbrubaker.multizone4j.reference.AlarmStatus;
import com.rbrubaker.multizone4j.reference.CurrentState;
import com.rbrubaker.multizone4j.reference.OperatingMode;

/**
 * This class represents a single Bacharach MultiZone device.
 * This class assumes the Bacharach MultiZone default modbus communication parameters.
 * Mode: RTU
 * Baud: 19200
 * Parity: No Parity Bit
 * Stop Bits: 1 Stop Bit
 * @author Justin Brubaker
 *
 */
public class MultiZoneDevice {

	private int modbusAddress;		
	private String serialDeviceName;
	
	public MultiZoneDevice(int _modbusAddress, String _serialDeviceName) {
		super();
		modbusAddress = _modbusAddress;
		serialDeviceName = _serialDeviceName;
	}	
	
	/**
	 * Manual Section B.4.1.
	 * This method is blocks as it has to contact the modbus device
	 * Gets the current zone ppm and alarm status for the given zone number.
	 * @param zoneNumber Zone numbers start with 0. So zone 1 is zonenumber=0.
	 * @return
	 * @throws ModbusException, Exception
	 * 
	 */
	public CurrentZoneStatus getCurrentZoneStatus(int zoneNumber) throws ModbusException, Exception, IllegalArgumentException {
		if (zoneNumber < 0 || zoneNumber > 15) {
			throw new IllegalArgumentException("The zone number must be between 0-15. The zone number is base 0. Ex. Zone 1 = zoneNumber=0");
		}
		
		ModbusSerialMaster master;
		SerialParameters params = new SerialParameters(serialDeviceName, 19200, AbstractSerialConnection.FLOW_CONTROL_DISABLED,
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
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.	 
	 * @return
	 * @throws ModbusException
	 * @throws Exception
	 */
	public ArrayList<CurrentZoneStatus> getAllCurrentZoneStatuses() throws ModbusException, Exception {
		ArrayList<CurrentZoneStatus> zones = new ArrayList<CurrentZoneStatus>();
		
		ModbusSerialMaster master;
		SerialParameters params = new SerialParameters(serialDeviceName, 19200, AbstractSerialConnection.FLOW_CONTROL_DISABLED,
				AbstractSerialConnection.FLOW_CONTROL_DISABLED, 8, AbstractSerialConnection.ONE_STOP_BIT, AbstractSerialConnection.NO_PARITY, false);
		master = new ModbusSerialMaster(params);
		master.connect();
		
		InputRegister[] ppmRegs = master.readMultipleRegisters(modbusAddress, 2001, 16);
		InputRegister[] alarmRegs = master.readMultipleRegisters(modbusAddress, 2017, 16);
		
		master.disconnect();
		
		for (int i = 0; i < 16; i++) {
			CurrentZoneStatus zone = new CurrentZoneStatus(ppmRegs[i].getValue(), alarmRegs[i].getValue());
			zones.add(zone);
		}		
		
		return zones;
	}
	
	/**
	 * This internal method reads a single integer value from a modbus function 03 register.
	 * @param registerNumber
	 * @return
	 * @throws ModbusException
	 * @throws Exception
	 */
	private int readSingleFunction03RegisterAsInt(int registerNumber) throws ModbusException, Exception{
		ModbusSerialMaster master;
		SerialParameters params = new SerialParameters(serialDeviceName, 19200, AbstractSerialConnection.FLOW_CONTROL_DISABLED,
				AbstractSerialConnection.FLOW_CONTROL_DISABLED, 8, AbstractSerialConnection.ONE_STOP_BIT, AbstractSerialConnection.NO_PARITY, false);
		master = new ModbusSerialMaster(params);
		master.connect();
		
		InputRegister[] reg = master.readMultipleRegisters(modbusAddress, registerNumber, 1);
		
		master.disconnect();		
		
		return reg[0].getValue();		
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.	
	 * @return The current operating mode of the Multi-Zone device. Use {@link OperatingMode} to decode the meaning of this value.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public int getCurrentOperatingMode() throws ModbusException, Exception {
		return readSingleFunction03RegisterAsInt(2033);
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The current state of the Multi-Zone device. Use {@link CurrentState} to decode the meaning of this value.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public int getCurrentState() throws ModbusException, Exception {
		return readSingleFunction03RegisterAsInt(2034);
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The zone that the Multi-Zone device is currently taking a reading of. This zone number is base 1. So a value of 1 indicates zone 1.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public int getActiveZone() throws ModbusException, Exception {
		return readSingleFunction03RegisterAsInt(2036);
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The highest unacknowledged alarm level across all 16 zones. Use {@link AlarmStatus} to decode the meaning of this value.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public int getMaxAlarm() throws ModbusException, Exception {
		return readSingleFunction03RegisterAsInt(2037);
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The number of zones with alarms active. This will be a number between 0 and 16.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public int getActiveAlarmCount() throws ModbusException, Exception {
		return readSingleFunction03RegisterAsInt(2038);
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The number of zones with alarms that have been acknowledged. This will be a number between 0 and 16.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public int getAcknowledgedAlarmCount() throws ModbusException, Exception {
		return readSingleFunction03RegisterAsInt(2039);
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The fault code on the Multi-Zone device. No faults is a value of 0000. See Manual Section 4.5 for information on decoding system faults.
	 * @throws Exception 
	 * @throws ModbusException 
	 */
	public int getFaultCodeStatus() throws ModbusException, Exception {
		return readSingleFunction03RegisterAsInt(2041);
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * This method only exists for completeness as the register list has two different registers for fault code.
	 * @return The fault code on the Multi-Zone device. No faults is a value of 0000. See Manual Section 4.6 for information on decoding system faults.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public int getFaultCode() throws ModbusException, Exception {
		return readSingleFunction03RegisterAsInt(2000);
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return An {@link Optional} containing true or false. If the modbus response is not 0 or 1 the optional will be empty.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public Optional<Boolean> isAudibleAlarm() throws ModbusException, Exception {
		switch (readSingleFunction03RegisterAsInt(2042)) {
			case 0: return Optional.of(false);
			case 1: return Optional.of(true);
			default: return Optional.empty();
		}
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return An {@link Optional} containing true of false. If the modbus response is not 0 or 1 the optional will be empty.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public Optional<Boolean> isSilenced() throws ModbusException, Exception {
		switch (readSingleFunction03RegisterAsInt(2043)) {
			case 0: return Optional.of(false);
			case 1: return Optional.of(true);
			default: return Optional.empty();
		}
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The zone with the highest concentration of refrigerant.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public int getHighestConcentrationZone() throws ModbusException, Exception {
		return readSingleFunction03RegisterAsInt(2044);
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The concentration, in parts per million, in the zone with the highest concentration.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public int getHighestConcentration() throws ModbusException, Exception {
		return readSingleFunction03RegisterAsInt(2045);
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The number of zones installed. According to the manual this can be 4,8,12,16.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public int getZonesInstalled() throws ModbusException, Exception {
		return readSingleFunction03RegisterAsInt(2046);
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The manifold pressure.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public double getManifoldPressure() throws ModbusException, Exception {
		int pressure = readSingleFunction03RegisterAsInt(2047);
		double dPressure = pressure / 100;
		return dPressure;
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The ambient pressure.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public double getAmbientPressure() throws ModbusException, Exception {
		int pressure = readSingleFunction03RegisterAsInt(2048);
		double dPressure = pressure / 100;
		return dPressure;
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The vacuum pressure.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public double getVacuumPressure() throws ModbusException, Exception {
		int pressure = readSingleFunction03RegisterAsInt(2049);
		double dPressure = pressure / 100;
		return dPressure;
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The Bench Temperature
	 * @throws ModbusException
	 * @throws Exception
	 */
	public double getBenchTemp() throws ModbusException, Exception {
		int temp = readSingleFunction03RegisterAsInt(2050);
		double dTemp = temp / 100;
		return dTemp;
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The Ave Voltage.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public double getAveVoltage() throws ModbusException, Exception {
		int volts = readSingleFunction03RegisterAsInt(2051);
		double dVolts = volts / 1000;
		return dVolts;
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The zero volts
	 * @throws ModbusException
	 * @throws Exception
	 */
	public double getZeroVolts() throws ModbusException, Exception {
		int volts = readSingleFunction03RegisterAsInt(2052);
		double dVolts = volts / 1000;
		return dVolts;
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The Ave Au value.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public double getAveAu() throws ModbusException, Exception {
		int aveAu = readSingleFunction03RegisterAsInt(2053);
		double dAveAu = aveAu / 10000;
		return dAveAu;
	}
	
	/**
	 * Manual Section B.4.1.
	 * This method blocks as it has to contact the modbus device.
	 * @return The current bench parts per million.
	 * @throws ModbusException
	 * @throws Exception
	 */
	public int getBenchPpm() throws ModbusException, Exception {
		return readSingleFunction03RegisterAsInt(2054);
	}
}
