package edu.chnu.recruiting.front.views.apply;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import jakarta.annotation.security.PermitAll;

@PageTitle("Apply")
@Route(value = "apply", layout = MainLayout.class)
@PermitAll
public class ApplyView extends VerticalLayout{

}
