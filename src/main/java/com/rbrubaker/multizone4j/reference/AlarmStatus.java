package com.rbrubaker.multizone4j.reference;

public class AlarmStatus {

	public static final int NO_ALARM = 0;
	public static final int LEAK = 1;
	public static final int EVACUATE = 3;
	
	public static String getValueOf(int alarmStatus) {
		switch (alarmStatus) {
			case 0: return "No Alarm";
			case 1: return "Leak";
			case 2: return "Evacuate";
			default: return "Unknown alarm status";
		}
	}
}
