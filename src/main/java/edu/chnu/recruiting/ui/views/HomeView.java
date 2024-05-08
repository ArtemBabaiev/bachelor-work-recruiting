package edu.chnu.recruiting.ui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

import edu.chnu.recruiting.services.PositionService;
import edu.chnu.recruiting.services.ServiceManager;
import edu.chnu.recruiting.ui.MainLayout;
import edu.chnu.recruiting.ui.views.open.PositionListingView;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "home", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends VerticalLayout {
	private TextField positionSearch = new TextField();
	private PositionService positionService;

	public HomeView(ServiceManager sm) {
		positionService = sm.getPositionService();
		VerticalLayout content = getContent();
		content.setMaxWidth("850px");

		this.setSizeFull();
		this.setAlignItems(Alignment.CENTER);
		this.setJustifyContentMode(JustifyContentMode.CENTER);
		add(content);
	}

	private VerticalLayout getContent() {
		VerticalLayout content = new VerticalLayout();

		content.add(new H1("WELCOME TO RECRUITING APPLICATION"));

		Span message = new Span("Here you can find " + positionService.countActivePositions() + " active positions");
		message.addClassName(LumoUtility.FontSize.LARGE);
		content.add(message);

		Div search = new Div();
		search.addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.BETWEEN);
		search.setWidthFull();

		Button searchBtn = new Button("Search");
		searchBtn.addClickListener(e -> {
			UI.getCurrent().navigate(PositionListingView.class,
					QueryParameters.of("positionName", positionSearch.getValue()));
		});
		searchBtn.setHeight("3rem");
		searchBtn.addClassNames(LumoUtility.Margin.Left.MEDIUM);

		positionSearch.setPlaceholder("Enter the name of position you are looking for");
		positionSearch.setWidthFull();
		positionSearch.setPrefixComponent(VaadinIcon.SEARCH.create());
		positionSearch.getElement().getStyle().set("--vaadin-input-field-height", "3rem");

		search.add(positionSearch, searchBtn);
		content.add(search);
		return content;
	}
}
