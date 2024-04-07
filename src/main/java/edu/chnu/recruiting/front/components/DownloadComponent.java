package edu.chnu.recruiting.front.components;

import java.io.ByteArrayInputStream;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.server.StreamResource;

public class DownloadComponent extends Div {
	public DownloadComponent(String fileName,byte[] data) {
		StreamResource streamResource = new StreamResource(fileName, () -> new ByteArrayInputStream(data));
		Anchor link = new Anchor(streamResource, "Dowload file");
        link.getElement().setAttribute("download", true);
        add(link);
	}
}
