package edu.chnu.recruiting.models.templates;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptedTemplateModel {
	private String positionName;
	private String companyName;
	private String notes;
}
