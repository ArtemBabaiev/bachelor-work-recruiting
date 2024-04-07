package edu.chnu.recruiting.models.viewModels;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationProfileGridVM {
	private String positionName;
	private String status;
	private LocalDateTime submittedAt;
	private String rejectReason;
}
