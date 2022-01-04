package com.rbrubaker.multizone4j.reference;

public class OperatingMode {

	public static final int NORMAL = 0;
	public static final int ZONE_HOLD = 1;
	public static final int DIAGNOSTIC = 2;
	public static final int SERVICE = 3;
	
	public static String getValueOf(int operatingMode) {
		switch (operatingMode) {
			case 0: return "Normal";
			case 1: return "Zone hold";
			case 2: return "Diagnostic";
			case 3: return "Service";
			default: return "Unknown operating mode";
		}
		
	}
	
}
