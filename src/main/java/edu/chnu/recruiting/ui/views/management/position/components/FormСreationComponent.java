package edu.chnu.recruiting.ui.views.management.position.components;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.models.wizard.Wizard;
import edu.chnu.recruiting.models.wizard.WizardStep;
import edu.chnu.recruiting.ui.views.management.position.components.SectionComponent.DownSectionEvent;
import edu.chnu.recruiting.ui.views.management.position.components.SectionComponent.UpSectionEvent;

public class FormСreationComponent extends VerticalLayout {
	VerticalLayout box = new VerticalLayout();
	Button addSectionBtn = new Button("Add section");

	public FormСreationComponent() {
		addSectionBtn.addClickListener(e -> handleAddSectionClick(e));
		this.setWidthFull();
		this.setAlignItems(Alignment.CENTER);
		this.addClassNames(LumoUtility.Padding.NONE);
		box.addClassNames(LumoUtility.Padding.NONE);
		add(box, addSectionBtn);
	}
	
	public FormСreationComponent(Wizard wizard) {
		this();
		for (var step : wizard.getSteps()) {
			SectionComponent sc = new SectionComponent(step);
			sc.addUpListener(eUp -> handleUpEvent(eUp));
			sc.addDownListener(eUp -> handleDownEvent(eUp));
			box.add(sc);
		}
	}

	private void handleAddSectionClick(ClickEvent<Button> e) {
		SectionComponent sc = new SectionComponent(new WizardStep());
		sc.addUpListener(eUp -> handleUpEvent(eUp));
		sc.addDownListener(eUp -> handleDownEvent(eUp));
		box.add(sc);
		return;
	}

	private void handleDownEvent(DownSectionEvent e) {
		var children = box.getChildren().collect(Collectors.toList());
		int size = children.size();
		int index = children.indexOf(e.getSource());
		if (index == size - 1 || index == -1) {
			return;
		}
		children.remove(index);
		children.add(index + 1, e.getSource());
		box.removeAll();
		box.add(children);
		return;
	}

	private void handleUpEvent(UpSectionEvent e) {
		var children = box.getChildren().collect(Collectors.toList());
		int index = children.indexOf(e.getSource());
		if (index == 0 || index == -1) {
			return;
		}
		children.remove(index);
		children.add(index - 1, e.getSource());
		box.removeAll();
		box.add(children);
		return;
	}

	public List<SectionComponent> getSections() {
		return box.getChildren().filter(c -> c instanceof SectionComponent).map(c -> (SectionComponent) c).toList();
	}
}
