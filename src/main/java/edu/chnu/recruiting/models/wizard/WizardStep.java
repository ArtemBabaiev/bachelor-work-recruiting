package edu.chnu.recruiting.models.wizard;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WizardStep {
	private String id;
	private String label;
	private int orderIndex;
	private List<WizardField> fields = new ArrayList<WizardField>();
	
	public void addField(WizardField field) {
		this.fields.add(field);
	}
}
