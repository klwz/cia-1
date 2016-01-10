/*
 * Copyright 2014 James Pether Sörling
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *	$Id$
 *  $HeadURL$
*/
package com.hack23.cia.web.impl.ui.application.views.admin.system;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import com.hack23.cia.model.internal.application.system.impl.ApplicationConfiguration;
import com.hack23.cia.model.internal.application.system.impl.ApplicationConfiguration_;
import com.hack23.cia.model.internal.application.system.impl.ApplicationEventGroup;
import com.hack23.cia.service.api.ApplicationManager;
import com.hack23.cia.service.api.DataContainer;
import com.hack23.cia.service.api.action.admin.UpdateApplicationConfigurationRequest;
import com.hack23.cia.web.impl.ui.application.action.ViewAction;
import com.hack23.cia.web.impl.ui.application.views.admin.common.AbstractAdminView;
import com.hack23.cia.web.impl.ui.application.views.common.formfactory.FormFactory;
import com.hack23.cia.web.impl.ui.application.views.common.gridfactory.GridFactory;
import com.hack23.cia.web.impl.ui.application.views.common.labelfactory.LabelFactory;
import com.hack23.cia.web.impl.ui.application.views.common.viewnames.AdminViews;
import com.hack23.cia.web.impl.ui.application.views.pageclicklistener.PageItemPropertyClickListener;
import com.hack23.cia.web.impl.ui.application.views.pageclicklistener.UpdateApplicationConfigurationClickListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * The Class AdminDataSummaryView.
 */
@Service
@Scope("prototype")
@VaadinView(AdminApplicationConfigurationView.NAME)
@Secured({ "ROLE_ADMIN" })
public final class AdminApplicationConfigurationView extends AbstractAdminView {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant NAME. */
	public static final String NAME = AdminViews.ADMIN_APPLICATIONS_CONFIGURATION_VIEW_NAME;

	/** The application manager. */
	@Autowired
	@Qualifier("ApplicationManager")
	private transient ApplicationManager applicationManager;

	/** The grid factory. */
	@Autowired
	private transient GridFactory gridFactory;

	/** The form factory. */
	@Autowired
	private transient FormFactory formFactory;

	/**
	 * Post construct.
	 */
	@PostConstruct
	public void postConstruct() {
		createListAndForm(null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.
	 * ViewChangeEvent)
	 */
	@Override
	public void enter(final ViewChangeEvent event) {
		final String parameters = event.getParameters();

		if (parameters != null) {
			createListAndForm(parameters.substring(parameters.lastIndexOf('/') + "/".length(), parameters.length()));
		}
	}

	/**
	 * Creates the list and form.
	 *
	 * @param pageId
	 *            the page id
	 */
	private void createListAndForm(final String pageId) {
		final VerticalLayout content = new VerticalLayout();

		content.addComponent(LabelFactory.createHeader2Label("Admin Application Configuration"));

		final DataContainer<ApplicationConfiguration, Long> dataContainer = applicationManager
				.getDataContainer(ApplicationConfiguration.class);

		final BeanItemContainer<ApplicationConfiguration> politicianDocumentDataSource = new BeanItemContainer<ApplicationConfiguration>(
				ApplicationConfiguration.class,
				dataContainer.getAllOrderBy(ApplicationConfiguration_.configurationGroup));

		content.addComponent(gridFactory.createBasicBeanItemGrid(politicianDocumentDataSource,
				"ApplicationConfiguration",
				new String[] { "hjid", "configTitle", "configDescription", "configurationGroup", "component",
						"componentTitle", "componentDescription", "propertyId", "propertyValue" },
				new String[] { "modelObjectId", "modelObjectVersion", "createdDate", "updatedDate" }, "hjid",
				new PageItemPropertyClickListener(AdminViews.ADMIN_APPLICATIONS_CONFIGURATION_VIEW_NAME, "hjid"),
				null));

		if (pageId != null && !pageId.isEmpty()) {

			final ApplicationConfiguration applicationConfiguration = dataContainer.load(Long.valueOf(pageId));

			if (applicationConfiguration != null) {

				final VerticalLayout leftLayout = new VerticalLayout();
				leftLayout.setSizeFull();
				final VerticalLayout rightLayout = new VerticalLayout();
				rightLayout.setSizeFull();
				final HorizontalLayout horizontalLayout = new HorizontalLayout();
				horizontalLayout.setWidth("100%");
				content.addComponent(horizontalLayout);
				horizontalLayout.addComponent(leftLayout);
				horizontalLayout.addComponent(rightLayout);

				formFactory.addTextFields(leftLayout, new BeanItem<ApplicationConfiguration>(applicationConfiguration),
						ApplicationConfiguration.class,
						Arrays.asList(new String[] { "hjid", "configTitle", "configDescription", "component",
								"componentTitle", "componentDescription", "propertyId", "propertyValue", "createdDate",
								"updatedDate" }));


				final UpdateApplicationConfigurationRequest request = new UpdateApplicationConfigurationRequest();
				request.setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId());
				request.setApplicationConfigurationId(applicationConfiguration.getHjid());

				request.setConfigTitle(applicationConfiguration.getConfigTitle());
				request.setConfigDescription(applicationConfiguration.getConfigDescription());

				request.setComponentTitle(applicationConfiguration.getConfigTitle());
				request.setComponentDescription(applicationConfiguration.getComponentDescription());

				request.setPropertyValue(applicationConfiguration.getPropertyValue());

				final ClickListener buttonListener = new UpdateApplicationConfigurationClickListener(request,applicationManager);
				formFactory.addRequestInputFormFields(rightLayout,  new BeanItem<UpdateApplicationConfigurationRequest>(request), UpdateApplicationConfigurationRequest.class,
						Arrays.asList(new String[] {"configTitle", "configDescription", "componentTitle", "componentDescription" ,"propertyValue"}),"Update Configuration",buttonListener);


			}
		}

		content.addComponent(pageLinkFactory.createMainViewPageLink());

		setContent(content);
		pageActionEventHelper.createPageEvent(ViewAction.VISIT_ADMIN_APPLICATION_CONFIGURATION_VIEW,
				ApplicationEventGroup.ADMIN, NAME, null, pageId);

	}

}