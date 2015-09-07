package org.anyframe.cloud.interfaces.facade.dto;

public class UserCargo {

	private String userId;
	
	private String bookingId;

	public UserCargo() {
	}

	public UserCargo(String userId, String bookingId) {
		this.userId = userId;
		this.bookingId = bookingId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

}
