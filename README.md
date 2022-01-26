[![](https://jitpack.io/v/JustBru00/multizone4j.svg)](https://jitpack.io/#JustBru00/multizone4j)

### MultiZone for Java
This library is designed to simplify modbus communications with a Bacharach Multi-Zone leak detector.   

This library can be included in your project easily with maven.
First add the jitpack repository.
```XML
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

Then include this library as a dependency.

Latest Release:
```XML
<dependency>
    <groupId>com.github.JustBru00</groupId>
    <artifactId>multizone4j</artifactId>
    <version>0.2.0</version>
</dependency>
```

Development Snapshots:
```XML
<dependency>
    <groupId>com.github.JustBru00</groupId>
    <artifactId>multizone4j</artifactId>
    <version>-SNAPSHOT</version>
</dependency>
```

### Getting Started
Below is some example code to show how to read the current parts per million and alarm status for each zone.
Bacharach documentation states that a modbus master device, such as this library, should not poll the current zone statuses more often than every 20 seconds. (Manual B.3.3.)

```Java
	// This object represents a Bacharach MultiZone device on the modbus serial network.
	// /dev/serial1 - This is the default RS485 port name on the Comfile PI 
	MultiZoneDevice one = new MultiZoneDevice(1, "/dev/serial1");
	
	// Read the current ppm and alarm status for zone 1.
	CurrentZoneStatus single = one.getCurrentZoneStatus(0);
	System.out.println(String.format("Single Read - Zone 1: %s ppm - %s alarm status", single.getPPM() + "", single.getAlarmStatus() + ""));
	
	// Read the status of all 16 zones.
	int address = 1;
	for (CurrentZoneStatus status : one.getAllCurrentZoneStatuses()) {			
		System.out.println(String.format("Zone %s: %s ppm - %s alarm status", address + "", status.getPPM() + "", status.getAlarmStatus() + ""));
		address++;
	}
```

You can view the release history and roadmap below:

### Roadmap
- Implement the 3000 series, write, Function Code 06 registers. (Manual Section B.4.2.)
- Fully implement the block mode registers for advanced remote setup and complete detail access. (Manual Section B.5.)

### Release History
```
Release version 0.2.0:
~ Changed the MultiZoneDevice so that is only closes the serial port when requested by the api user.

Release version 0.1.0:
+ Implemented all of the modbus registers from Manual Section B.4.1.
+ Implemented read only modbus registers from Manual Section B.4.2.
```

