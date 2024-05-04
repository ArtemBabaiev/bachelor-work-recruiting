package edu.chnu.recruiting.ui.components.fields.picker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalaryRange {
	private Double start;
	private Double end;
	private String currencyCode;
}
