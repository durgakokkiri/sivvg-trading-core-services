package com.sivvg.tradingservices.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "market_holidays")
public class MarketHolidayEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holiday_date", nullable = false, unique = true)
    private LocalDate holidayDate;

    @Column(nullable = false)
    private String description;

    @Column(name = "holiday_year", nullable = false)
    private int year;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(LocalDate holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public MarketHolidayEntity(Long id, LocalDate holidayDate, String description, int year) {
		super();
		this.id = id;
		this.holidayDate = holidayDate;
		this.description = description;
		this.year = year;
	}

	public MarketHolidayEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "MarketHolidayEntity [id=" + id + ", holidayDate=" + holidayDate + ", description=" + description
				+ ", year=" + year + "]";
	}

}
