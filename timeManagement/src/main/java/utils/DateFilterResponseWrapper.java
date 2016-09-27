package utils;

import java.util.Date;
import java.util.List;

//this class is a wrapper for the filtered notes of a user based on start and end date
public class DateFilterResponseWrapper {
	Date date;
	float totalPeriod;
	List<String> notes;
	
	public DateFilterResponseWrapper() {
		super();
	}
	public DateFilterResponseWrapper(Date date, float totalPeriod,
			List<String> notes) {
		super();
		this.date = date;
		this.totalPeriod = totalPeriod;
		this.notes = notes;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public float getTotalPeriod() {
		return totalPeriod;
	}
	public void setTotalPeriod(float totalPeriod) {
		this.totalPeriod = totalPeriod;
	}
	public List<String> getNotes() {
		return notes;
	}
	public void setNotes(List<String> notes) {
		this.notes = notes;
	}
}
