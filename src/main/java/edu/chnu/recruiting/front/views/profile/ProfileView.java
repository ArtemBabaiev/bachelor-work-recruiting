package edu.chnu.recruiting.front.views.profile;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import edu.chnu.recruiting.models.security.User;

public abstract class ProfileView extends HorizontalLayout {
	private ProfileSideNav menuBar;
	private Div content = new Div();
	public ProfileView(User user) {
		menuBar = new ProfileSideNav(user);
		content.setSizeFull();
		add(menuBar, content);
	}
	
	public void setContent(Component content) {
		this.content.add(content);
	}
}
