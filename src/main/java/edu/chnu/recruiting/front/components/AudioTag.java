package edu.chnu.recruiting.front.components;

import java.io.ByteArrayInputStream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.StreamResource;

@Tag("audio")
public class AudioTag extends Component {
	public AudioTag(byte[] src) {
		getElement().setAttribute("controls", true);
		getElement().setAttribute("controlsList", "nodownload");
		this.setSource(src);
	}

	public void setSource(byte[] data) {
		getElement().setAttribute("src", new StreamResource("audio", () -> new ByteArrayInputStream(data)));
	}
}
