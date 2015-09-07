package org.anyframe.cargotracker.interfaces.tracking.dto;

public class HandlingEventViewDto {
	private final boolean expected;
	private final String description;
	
	public HandlingEventViewDto(boolean expected, String description) {
		this.expected = expected;
		this.description = description;
	}
	
	public boolean isExpected() {
		return expected;
	}
	
	public String getDescription() {
		return description;
	}
}