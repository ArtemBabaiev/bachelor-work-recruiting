package edu.chnu.recruiting.front.views.management.position;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Position form")
@Route(value = "management/positions/:id/form", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class PositionFormEditView {

}
