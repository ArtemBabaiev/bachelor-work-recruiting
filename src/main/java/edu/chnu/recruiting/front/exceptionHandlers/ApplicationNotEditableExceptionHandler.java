package edu.chnu.recruiting.front.exceptionHandlers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import edu.chnu.recruiting.exceptions.ApplicationNonEditableException;
import edu.chnu.recruiting.front.layouts.MainLayout;
import jakarta.servlet.http.HttpServletResponse;

@Tag(Tag.DIV)
@ParentLayout(MainLayout.class)
@AnonymousAllowed
public class ApplicationNotEditableExceptionHandler extends Component implements HasErrorParameter<ApplicationNonEditableException> {

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<ApplicationNonEditableException> parameter) {
		getElement().setText("Application is awaiting for review. Changes forbidden");
		return HttpServletResponse.SC_UNAUTHORIZED;
	}

}

