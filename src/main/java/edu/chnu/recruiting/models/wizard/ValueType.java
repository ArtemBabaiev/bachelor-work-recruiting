package edu.chnu.recruiting.models.wizard;

import com.vaadin.flow.component.icon.VaadinIcon;

import lombok.Getter;

@Getter
public enum ValueType {
	NUMBER("With numeric answer", VaadinIcon.CALC), 
	TEXT("With text answer", VaadinIcon.TEXT_LABEL), 
	DATE("Date", VaadinIcon.CALENDAR), 
	AUDIO("With audio recording answer", VaadinIcon.MICROPHONE), 
	SELECTION_SINGLE("With answer options", VaadinIcon.OPTIONS), 
	SELECTION_MULTIPLE("With answer checkboxes", VaadinIcon.CHECK_SQUARE), 
	UPLOAD("File upload", VaadinIcon.CLOUD_UPLOAD);
	
	private String label;
	private VaadinIcon icon;
	
	private ValueType(String label, VaadinIcon icon) {
		this.label = label;
		this.icon = icon;
	}
}
