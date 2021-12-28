package com.rbrubaker.multizone4j;

import java.time.Instant;

public class CurrentZoneStatus {

	private int ppm = -1;
	private int alarmStatus = -1;
	private Instant updatedAt;	
		
	public CurrentZoneStatus(int ppm, int alarmStatus) {
		super();
		this.ppm = ppm;
		this.alarmStatus = alarmStatus;
		updatedAt = Instant.now();
	}

	public int getPPM() {
		return ppm;
	}
	
	public int getAlarmStatus() {
		return alarmStatus;
	}	
	
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	
}
