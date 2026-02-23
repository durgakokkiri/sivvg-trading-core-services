package com.sivvg.tradingservices.playload;

import java.util.Map;

public class DashboardStatsDto {
	private long totalUsers;
	private long activeUsers;
	private long disabledUsers;
	private long blockedUsers;

	private Map<String, Long> usersByPlan;
	private long activeFollowers;

	public DashboardStatsDto(long totalUsers, long activeUsers, long disabledUsers, long blockedUsers,
			Map<String, Long> usersByPlan, long activeFollowers) {
		super();
		this.totalUsers = totalUsers;
		this.activeUsers = activeUsers;
		this.disabledUsers = disabledUsers;
		this.blockedUsers = blockedUsers;
		this.usersByPlan = usersByPlan;
		this.activeFollowers = activeFollowers;
	}

	public DashboardStatsDto() {
		super();
	}

	public long getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(long totalUsers) {
		this.totalUsers = totalUsers;
	}

	public long getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(long activeUsers) {
		this.activeUsers = activeUsers;
	}

	public long getDisabledUsers() {
		return disabledUsers;
	}

	public void setDisabledUsers(long disabledUsers) {
		this.disabledUsers = disabledUsers;
	}

	public long getBlockedUsers() {
		return blockedUsers;
	}

	public void setBlockedUsers(long blockedUsers) {
		this.blockedUsers = blockedUsers;
	}

	public Map<String, Long> getUsersByPlan() {
		return usersByPlan;
	}

	public void setUsersByPlan(Map<String, Long> usersByPlan) {
		this.usersByPlan = usersByPlan;
	}

	public long getActiveFollowers() {
		return activeFollowers;
	}

	public void setActiveFollowers(long activeFollowers) {
		this.activeFollowers = activeFollowers;
	}

	@Override
	public String toString() {
		return "DashboardStatsDto [totalUsers=" + totalUsers + ", activeUsers=" + activeUsers + ", disabledUsers="
				+ disabledUsers + ", blockedUsers=" + blockedUsers + ", usersByPlan=" + usersByPlan
				+ ", activeFollowers=" + activeFollowers + "]";
	}

}
