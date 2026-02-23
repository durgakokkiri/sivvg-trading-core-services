package com.sivvg.tradingservices.playload;

public class SectorPerformance {
	private String sectorType;
	private Long totalTips;

	public String getSectorType() {
		return sectorType;
	}

	public void setSectorType(String sectorType) {
		this.sectorType = sectorType;
	}

	public Long getTotalTips() {
		return totalTips;
	}

	public void setTotalTips(Long totalTips) {
		this.totalTips = totalTips;
	}

	public SectorPerformance(String sectorType, Long totalTips) {
		super();
		this.sectorType = sectorType;
		this.totalTips = totalTips;
	}

	public SectorPerformance() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "SectorPerformance [sectorType=" + sectorType + ", totalTips=" + totalTips + "]";
	}

}
