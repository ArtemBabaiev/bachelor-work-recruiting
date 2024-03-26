package edu.chnu.recruiting.front.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ErrorViews {
	
	public static Component get404(String message) {
		return getErrorView("404", message);
	}
	
	public static Component getErrorView(String code, String message) {
		VerticalLayout vl = new VerticalLayout();
		H1 h1 = new H1();
        H3 h3 = new H3();
        vl.setWidth("100%");
        vl.getStyle().set("flex-grow", "1");
        vl.setJustifyContentMode(JustifyContentMode.START);
        vl.setAlignItems(Alignment.CENTER);
        h1.setText(code);
        vl.setAlignSelf(Alignment.CENTER, h1);
        h1.setWidth("max-content");
        h3.setText(message);
        h3.setWidth("max-content");
        vl.add(h1);
        vl.add(h3);
        return vl;
	}
}
