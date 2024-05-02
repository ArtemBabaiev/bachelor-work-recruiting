package edu.chnu.recruiting.utils.enums;

import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;

import lombok.Getter;

@Getter
public enum ApplicationStatus {
	PENDING_DATA("pending data"), PENDING_REVIEW("pending review"), ACCEPTED("accepted"), REJECTED("rejected");

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

	public static Span getBadge(String status) {
		Span badge = null;
		switch (ApplicationStatus.valueOf(status)) {
		case ACCEPTED:
			badge = new Span(ApplicationStatus.ACCEPTED.getLabel());
			badge.getElement().getThemeList().add("badge success");
			break;
		case PENDING_DATA:
			badge = new Span(ApplicationStatus.PENDING_DATA.getLabel());
			badge.getElement().getThemeList().add("badge contrast");
			break;
		case PENDING_REVIEW:
			badge = new Span(ApplicationStatus.PENDING_REVIEW.getLabel());
			badge.getElement().getThemeList().add("badge");
			break;
		case REJECTED:
			badge = new Span(ApplicationStatus.REJECTED.getLabel());
			badge.getElement().getThemeList().add("badge error");
			break;
		}
		return badge;
	}
}
