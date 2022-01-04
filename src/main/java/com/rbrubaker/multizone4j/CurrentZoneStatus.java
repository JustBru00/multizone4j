package com.rbrubaker.multizone4j;

import java.time.Instant;

import com.rbrubaker.multizone4j.reference.AlarmStatus;

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
	/**
	 * The {@link AlarmStatus} class is provided to provide easy translations of the alarm status codes.
	 * Keep in mind that the level for LEAK and EVACUATE can be set different for each zone.
	 * @return
	 */
	public int getAlarmStatus() {
		return alarmStatus;
	}	
	
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	
}
