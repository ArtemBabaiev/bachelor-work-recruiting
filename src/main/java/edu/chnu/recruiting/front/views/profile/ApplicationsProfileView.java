package edu.chnu.recruiting.front.views.profile;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import jakarta.annotation.security.PermitAll;

@Route(value = "profile/applications", layout = MainLayout.class)
@PageTitle("Profile-Applications")
@PermitAll
public class ApplicationsProfileView extends VerticalLayout {

}
