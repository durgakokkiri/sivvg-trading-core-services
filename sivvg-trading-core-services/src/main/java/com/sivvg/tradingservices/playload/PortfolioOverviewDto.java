package com.sivvg.tradingservices.playload;

import java.util.List;

public class PortfolioOverviewDto {

	private double returnRateOutOf20;
	private double diversificationScoreOutOf10;
	private List<PortfolioMonthlyUserDto> users;

	public double getReturnRateOutOf20() {
		return returnRateOutOf20;
	}

	public void setReturnRateOutOf20(double returnRateOutOf20) {
		this.returnRateOutOf20 = returnRateOutOf20;
	}

	public double getDiversificationScoreOutOf10() {
		return diversificationScoreOutOf10;
	}

	public void setDiversificationScoreOutOf10(double diversificationScoreOutOf10) {
		this.diversificationScoreOutOf10 = diversificationScoreOutOf10;
	}

	public List<PortfolioMonthlyUserDto> getUsers() {
		return users;
	}

	public void setUsers(List<PortfolioMonthlyUserDto> users) {
		this.users = users;
	}
}
