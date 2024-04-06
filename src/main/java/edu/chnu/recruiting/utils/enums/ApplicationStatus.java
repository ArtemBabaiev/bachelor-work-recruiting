package edu.chnu.recruiting.utils.enums;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public enum ApplicationStatus {
	PENDING_DATA("pending data"),
	PENDING_REVIEW("pending review"),
	ACCEPTED("accepted"),
	REJECTED("rejected");
	
	private String label;
	
	private ApplicationStatus(String label) {
		this.label = label;
	}
	
	public static List<String> getAllValues() {
		return Arrays.stream(ApplicationStatus.values()).map(ApplicationStatus::toString).toList();
	}
	
	public static String getLabel(String e) {
		return ApplicationStatus.valueOf(e).getLabel();
	}
	
	public static boolean editable(String status) {
		 return ApplicationStatus.valueOf(status).equals(PENDING_DATA);
	}
}
