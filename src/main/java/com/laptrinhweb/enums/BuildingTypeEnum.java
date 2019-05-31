package com.laptrinhweb.enums;

public enum BuildingTypeEnum {
	
	TANG_TRET("Tầng trệt"),
	NGUYEN_CAN("Nguyên Căn"),
	NOI_THAT("Nội thất");
	
	private String value;

	BuildingTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
