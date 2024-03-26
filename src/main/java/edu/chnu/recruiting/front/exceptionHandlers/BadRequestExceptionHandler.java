package edu.chnu.recruiting.front.exceptionHandlers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.exceptions.BadRequestException;
import edu.chnu.recruiting.front.layouts.MainLayout;
import jakarta.servlet.http.HttpServletResponse;

@Tag(Tag.DIV)
@ParentLayout(MainLayout.class)
@AnonymousAllowed
public class BadRequestExceptionHandler extends Component
implements HasErrorParameter<BadRequestException>{

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<BadRequestException> parameter) {
		getElement().setText(
	            "Bad request was sent when navigating");
	        return HttpServletResponse.SC_BAD_REQUEST;
	}

}
