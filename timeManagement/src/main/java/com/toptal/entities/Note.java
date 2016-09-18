package com.toptal.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;


/**
 * The persistent class for the note database table.
 * 
 */
@Entity
@NamedQuery(name="Note.findAll", query="SELECT n FROM Note n")
public class Note implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String description;

	private float period;

	@Column(name="preferred_working_hour_per_day")
	private Time preferredWorkingHourPerDay;

	@Temporal(TemporalType.DATE)
	@Column(name="start_date")
	private Date startDate;

	//bi-directional many-to-one association to User
	@ManyToOne
	private User user;

	public Note() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPeriod() {
		return this.period;
	}

	public void setPeriod(float period) {
		this.period = period;
	}

	public Time getPreferredWorkingHourPerDay() {
		return this.preferredWorkingHourPerDay;
	}

	public void setPreferredWorkingHourPerDay(Time preferredWorkingHourPerDay) {
		this.preferredWorkingHourPerDay = preferredWorkingHourPerDay;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}