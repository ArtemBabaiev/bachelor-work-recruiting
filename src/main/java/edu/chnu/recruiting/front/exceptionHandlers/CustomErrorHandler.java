package edu.chnu.recruiting.front.exceptionHandlers;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.ErrorHandlerUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomErrorHandler implements ErrorHandler {

	@Override
	public void error(ErrorEvent errorEvent) {
		boolean redirected = ErrorHandlerUtil.handleErrorByRedirectingToErrorView(errorEvent.getThrowable());
		if (!redirected) {
			// We did not have a matching error view, logging and showing notification.
			log.error("Something wrong happened", errorEvent.getThrowable());
			if (UI.getCurrent() != null) {
				UI.getCurrent().access(() -> {
					var not = Notification.show("An internal error has occurred.", 5000, Position.BOTTOM_STRETCH);
					not.addThemeVariants(NotificationVariant.LUMO_ERROR);
				});
			}
		}
	}
}
