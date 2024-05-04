package edu.chnu.recruiting.ui.views.management.application;

import java.util.Collection;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class SectionDataComponent extends VerticalLayout {
	H3 step = new H3();
	Div box = new Div();

	public SectionDataComponent(String stepTxt, List<FieldDataComponent> fieldDataComponents) {
		step.setText(stepTxt);
		addClassNames(LumoUtility.Background.CONTRAST_10, LumoUtility.BorderRadius.MEDIUM);
		setSizeFull();
		box.add(fieldDataComponents.toArray(new FieldDataComponent[0]));
		box.addClassName(LumoUtility.Padding.MEDIUM);
		add(step,box);
	}

	public static class FieldDataComponent extends Div {
		private H4 question;
		private Component answer;

		public FieldDataComponent(String questionTxt, Object answerTxt) {
			question = new H4(questionTxt);
			if (answerTxt instanceof Collection<?> col) {	
				answer = new Paragraph(String.join(", ", (Collection<String>) col));
			} else {
				answer = new Paragraph(answerTxt == null? "": answerTxt.toString());
			}
			add(question, answer);
		}
		
		public FieldDataComponent(String questionTxt, Component answer) {
			question = new H4(questionTxt);
			this.answer = answer;
			add(question, answer);
		}
	}

}
