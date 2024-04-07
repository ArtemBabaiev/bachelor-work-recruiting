package edu.chnu.recruiting.front.views.management.position;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import edu.chnu.recruiting.front.layouts.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Positions")
@Route(value = "management/positions", layout = MainLayout.class)
@RolesAllowed({ "COMPANY", "RECRUITER" })
public class PositionsMgmtView {

}
