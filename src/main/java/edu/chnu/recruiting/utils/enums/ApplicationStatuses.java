package edu.chnu.recruiting.utils.enums;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public enum ApplicationStatuses {
	PENDING_DATA("pending data"),
	PENDING_REVIEW("pending review"),
	ACCEPTED("accepted"),
	REJECTED("rejected");
	
	private String label;
	
	private ApplicationStatuses(String label) {
		this.label = label;
	}
	
	public static List<String> getAllValues() {
		return Arrays.stream(ApplicationStatuses.values()).map(ApplicationStatuses::toString).toList();
	}
	
	public static String getLabel(String e) {
		return ApplicationStatuses.valueOf(e).getLabel();
	}
}
