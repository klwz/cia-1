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
package com.hack23.cia.web.impl.ui.application.views.user.document;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import com.hack23.cia.model.external.riksdagen.dokumentlista.impl.DocumentElement;
import com.hack23.cia.model.internal.application.system.impl.ApplicationEventGroup;
import com.hack23.cia.service.api.ApplicationManager;
import com.hack23.cia.service.api.action.user.SearchDocumentRequest;
import com.hack23.cia.service.api.action.user.SearchDocumentResponse;
import com.hack23.cia.web.impl.ui.application.action.ViewAction;
import com.hack23.cia.web.impl.ui.application.views.common.formfactory.FormFactory;
import com.hack23.cia.web.impl.ui.application.views.common.gridfactory.GridFactory;
import com.hack23.cia.web.impl.ui.application.views.common.viewnames.UserViews;
import com.hack23.cia.web.impl.ui.application.views.pageclicklistener.SearchDocumentClickListener;
import com.hack23.cia.web.impl.ui.application.views.pageclicklistener.SearchDocumentClickListener.SearchDocumentResponseHandler;
import com.hack23.cia.web.impl.ui.application.views.user.common.AbstractUserView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * The Class PartyView.
 */
@Service
@Scope(value = "prototype")
@VaadinView(value = SearchDocumentView.NAME, cached = true)
public class SearchDocumentView extends AbstractUserView {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant NAME. */
	public static final String NAME = UserViews.SEARCH_DOCUMENT_VIEW_NAME;

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
		setSizeFull();
		createBasicLayoutWithPanelAndFooter(NAME);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener
	 *      .ViewChangeEvent)
	 */
	@Override
	// @Secured({ "ROLE_ANONYMOUS","ROLE_USER", "ROLE_ADMIN" })
	public void enter(final ViewChangeEvent event) {

		final String parameters = event.getParameters();

		String pageId = null;
		if (parameters != null) {
			pageId = event.getParameters().substring(parameters.lastIndexOf('/') + "/".length(), parameters.length());
		}

		final VerticalLayout panelContent = new VerticalLayout();
		panelContent.setSizeFull();
		panelContent.setMargin(true);

		getPanel().setContent(panelContent);

		final VerticalLayout searchLayout = new VerticalLayout();
		searchLayout.setSizeFull();
		panelContent.addComponent(searchLayout);

		final VerticalLayout searchresultLayout = new VerticalLayout();
		searchresultLayout.setSizeFull();

		panelContent.addComponent(searchresultLayout);

		final SearchDocumentRequest searchRequest = new SearchDocumentRequest();
		searchRequest.setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId());
		searchRequest.setMaxResults(100);
		searchRequest.setSearchExpression("");
		SearchDocumentResponseHandler handler = new SearchDocumentResponseHandler() {

			@Override
			public void handle(SearchDocumentResponse response) {
				searchresultLayout.removeAllComponents();

				final BeanItemContainer<DocumentElement> documentActivityDataDataDataSource = new BeanItemContainer<>(
						DocumentElement.class, response.getResultElement());

				final Grid documentActivityDataItemGrid = gridFactory.createBasicBeanItemGrid(
						documentActivityDataDataDataSource, "Document",
						new String[] { "rm", "createdDate", "madePublicDate", "documentType", "subType", "title",
								"subTitle", "status" },
						new String[] { "label", "id", "hit", "relatedId", "org", "tempLabel", "numberValue",
								"systemDate", "kallId", "documentFormat", "documentUrlText", "documentUrlHtml",
								"documentStatusUrlXml", "committeeReportUrlXml" },
						null, null, null);

				searchresultLayout.addComponent(documentActivityDataItemGrid);
			}
		};
		final ClickListener searchListener = new SearchDocumentClickListener(searchRequest, applicationManager,
				handler);
		formFactory.addRequestInputFormFields(searchLayout, new BeanItem<>(searchRequest), SearchDocumentRequest.class,
				Arrays.asList(new String[] { "searchExpression" }), "Search", searchListener);

		getPanel().setContent(panelContent);
		pageActionEventHelper.createPageEvent(ViewAction.VISIT_DOCUMENT_VIEW, ApplicationEventGroup.USER, NAME,
				parameters, pageId);

	}

}