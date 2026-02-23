package com.sivvg.tradingservices.playload;

import java.util.List;

public class CategoryPerformance {

	private String category;
	private List<PeriodStatdto> periods;

	public CategoryPerformance(String category, List<PeriodStatdto> periods) {
		super();
		this.category = category;
		this.periods = periods;
	}

	public CategoryPerformance() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<PeriodStatdto> getPeriods() {
		return periods;
	}

	public void setPeriods(List<PeriodStatdto> periods) {
		this.periods = periods;
	}

	@Override
	public String toString() {
		return "CategoryPerformance [category=" + category + ", periods=" + periods + "]";
	}

}
