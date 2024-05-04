package edu.chnu.recruiting.models.templates;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectedTemplateModel {
	private String positionName;
	private String companyName;
	private String rejectReason;
}
