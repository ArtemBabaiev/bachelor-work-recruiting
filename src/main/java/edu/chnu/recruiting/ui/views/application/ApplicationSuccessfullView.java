package edu.chnu.recruiting.ui.views.application;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.ui.MainLayout;
import edu.chnu.recruiting.ui.views.open.PositionListingView;
import edu.chnu.recruiting.ui.views.profile.ApplicationsProfileView;
import jakarta.annotation.security.PermitAll;

@PageTitle("Application Successfull")
@Route(value = "application/success", layout = MainLayout.class)
@PermitAll
public class ApplicationSuccessfullView extends VerticalLayout {
	public ApplicationSuccessfullView() {
		initComponent();
	}
	
	private void initComponent(){
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		H2 message = new H2("Your application has been submitted");
		Button listingNav = new Button("Explore more positions",
				e -> UI.getCurrent().navigate(PositionListingView.class));
		Button profileNav = new Button("View submitted applications",
				e -> UI.getCurrent().navigate(ApplicationsProfileView.class));

		HorizontalLayout controls = new HorizontalLayout(listingNav, profileNav);

		add(message, controls);
	}
}
