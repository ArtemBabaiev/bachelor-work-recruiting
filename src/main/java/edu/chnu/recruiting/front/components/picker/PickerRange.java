package edu.chnu.recruiting.front.components.picker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PickerRange<T> {
	private T start;
	private T end;
}
