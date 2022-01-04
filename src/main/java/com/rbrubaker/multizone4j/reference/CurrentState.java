package com.rbrubaker.multizone4j.reference;

public class CurrentState {

	public static final int IDLE = 0;
	public static final int SAMPLING = 1;
	public static final int ZEROING = 2;
	public static final int WARM_UP = 3;
	public static final int PRESSURE_CHECK = 4;
	
	public static String getValueOf(int currentState) {
		switch (currentState) {
			case 0: return "Idle";
			case 1: return "Sampling";
			case 2: return "Zeroing";
			case 3: return "Warm up";
			case 4: return "Pressure check";
			default: return "Unknown current state";
		}
	}
	
}
