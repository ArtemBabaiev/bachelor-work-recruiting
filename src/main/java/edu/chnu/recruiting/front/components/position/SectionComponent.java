package edu.chnu.recruiting.front.components.position;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropEvent;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import edu.chnu.recruiting.front.components.questions.NumberQuestion;
import edu.chnu.recruiting.front.components.questions.QuestionCard;
import edu.chnu.recruiting.front.components.questions.QuestionComponent;
import edu.chnu.recruiting.front.components.questions.TextQuestion;

public class SectionComponent extends VerticalLayout implements DropTarget<QuestionCard> {
	Div box = new Div();
	TextField sectionName = new TextField("Section name");
	ObjectMapper objectMapper = new ObjectMapper();
	Button removeBtn = new Button(new Icon(VaadinIcon.CLOSE));

	public SectionComponent() {
		setActive(true);
		getStyle().set("border", "6px dotted DarkOrange");
		this.addDropListener(e -> handleDrop(e));
		removeBtn.addClickListener(e -> remove());
		var hzl = new HorizontalLayout(sectionName, removeBtn);
		hzl.setAlignItems(Alignment.BASELINE);
		add(hzl, box);
	}

	private void handleDrop(DropEvent<QuestionCard> e) {
		var optSource = e.getDragSourceComponent();
		if (optSource.isPresent()) {
			var source = (QuestionCard) optSource.get();
			QuestionComponent target = switch (source.getType()) {
			case NUMBER -> new NumberQuestion();
			case TEXT -> new TextQuestion();
			default -> null;
			};
			box.add(target);
		}

	}

	public void remove() {
		this.removeFromParent();
	}

}
