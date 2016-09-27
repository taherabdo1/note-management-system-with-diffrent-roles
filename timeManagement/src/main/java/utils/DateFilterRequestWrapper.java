package utils;

import java.util.Date;

//this class is a wrapper class for the data needed to filter the notes of a user based on start and end date
public class DateFilterRequestWrapper {

	Date startDate;
	Date endDate;
	String userToken;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
}
