package edu.chnu.recruiting.front.views.profile;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.models.security.User;
import edu.chnu.recruiting.utils.enums.StarterRoles;

public abstract class ProfileView extends HorizontalLayout {
	private ProfileSideNav menuBar;
	private Div content = new Div();

	private Component accountPreview;

	public ProfileView(User user) {
		accountPreview = getAccountPreview(user);
		menuBar = new ProfileSideNav(user);
		content.setSizeFull();
		VerticalLayout test = new VerticalLayout(accountPreview, menuBar);
		test.setSizeUndefined();
		this.add(test, content);
	}

	public void setContent(Component content) {
		this.content.add(content);
	}

	public Component getAccountPreview(User user) {
		HorizontalLayout row = new HorizontalLayout();
	    row.setAlignItems(Alignment.CENTER);
	    Avatar avatar = new Avatar();
	    avatar.setName(user.getUsername().toUpperCase());
	    Span name = new Span(user.getUsername());
	    Span profession = new Span();
	    if (user.getRole().getName().equals(StarterRoles.RECRUITER.getName())) {
	    	profession.setText("Account mananged by organisation");
		} else {
			profession.setText("Your personal account");			
		}
	    profession.getStyle()
	            .set("color", "var(--lumo-secondary-text-color)")
	            .set("font-size", "var(--lumo-font-size-s)");
	    name.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);
	    VerticalLayout column = new VerticalLayout(name, profession);
	    column.setPadding(false);
	    column.setSpacing(false);

	    row.add(avatar, column);
	    row.getStyle().set("line-height", "var(--lumo-line-height-m)");
	    return row;
	}
}
