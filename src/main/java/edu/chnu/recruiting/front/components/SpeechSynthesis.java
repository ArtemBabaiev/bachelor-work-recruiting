package edu.chnu.recruiting.front.components;

import com.vaadin.flow.component.html.Div;

public class SpeechSynthesis extends Div {
	public void readOutLoad() {
		getElement().executeJs("""
				var msg = new SpeechSynthesisUtterance();
				msg.text = "%s";
				msg.lang = 'en';
				window.speechSynthesis.speak(msg);
				""");
	}
}
