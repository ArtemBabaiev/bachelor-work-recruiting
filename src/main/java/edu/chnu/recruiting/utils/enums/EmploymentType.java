package edu.chnu.recruiting.utils.enums;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public enum EmploymentType {
	NONE("-"),
	PART_TIME("Part time"),
	FULL_TIME("Full time");
	
	private String label;
	
	private EmploymentType(String label) {
		this.label = label;
	}
	
	public static List<String> getAllValues() {
		return Arrays.stream(EmploymentType.values()).map(EmploymentType::toString).toList();
	}
	
	public static String getLabel(String e) {
		return EmploymentType.valueOf(e).getLabel();
	}
}
