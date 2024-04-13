package edu.chnu.recruiting.front.views.management.position.components;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.front.views.management.position.components.SectionComponent.DownSectionEvent;
import edu.chnu.recruiting.front.views.management.position.components.SectionComponent.UpSectionEvent;

@CssImport("./themes/recruiting/styles.css")
public class FormСreationComponent extends VerticalLayout {
	VerticalLayout box = new VerticalLayout();
	Button addSectionBtn = new Button("Add section");
	public FormСreationComponent() {
		addSectionBtn.addClickListener(e -> handleAddSectionClick(e));
		this.setWidthFull();
		box.setSizeUndefined();
		box.addClassNames("resizable", LumoUtility.Margin.NONE, LumoUtility.Padding.NONE);
		this.setAlignItems(Alignment.CENTER);
		add(box, addSectionBtn);
	}
	
	private void handleAddSectionClick(ClickEvent<Button> e) {
		SectionComponent sc = new SectionComponent();
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
	
	public List<SectionComponent> getSections(){
		return box.getChildren().filter(c -> c instanceof SectionComponent)
		.map(c -> (SectionComponent) c).toList();
	}
}
