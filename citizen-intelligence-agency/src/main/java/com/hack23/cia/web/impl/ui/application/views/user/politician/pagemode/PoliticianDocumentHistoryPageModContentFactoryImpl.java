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
package com.hack23.cia.web.impl.ui.application.views.user.politician.pagemode;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import com.hack23.cia.model.external.riksdagen.person.impl.PersonData;
import com.hack23.cia.model.internal.application.data.document.impl.ViewRiksdagenPoliticianDocument;
import com.hack23.cia.model.internal.application.data.document.impl.ViewRiksdagenPoliticianDocument_;
import com.hack23.cia.model.internal.application.data.politician.impl.ViewRiksdagenPolitician;
import com.hack23.cia.service.api.DataContainer;
import com.hack23.cia.web.impl.ui.application.views.common.labelfactory.LabelFactory;
import com.hack23.cia.web.impl.ui.application.views.common.sizing.ContentRatio;
import com.hack23.cia.web.impl.ui.application.views.common.viewnames.PoliticianPageMode;
import com.hack23.cia.web.impl.ui.application.views.common.viewnames.UserViews;
import com.hack23.cia.web.impl.ui.application.views.pageclicklistener.PageItemPropertyClickListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * The Class OverviewPageModContentFactoryImpl.
 */
@Component
public final class PoliticianDocumentHistoryPageModContentFactoryImpl extends AbstractPoliticianPageModContentFactoryImpl {

	/**
	 * Instantiates a new politician document history page mod content factory
	 * impl.
	 */
	public PoliticianDocumentHistoryPageModContentFactoryImpl() {
		super();
	}

	@Override
	public boolean matches(final String page, final String parameters) {
		return NAME.equals(page) && parameters.contains(PoliticianPageMode.DOCUMENTHISTORY.toString());
	}

	@Secured({ "ROLE_ANONYMOUS", "ROLE_USER", "ROLE_ADMIN" })
	@Override
	public Layout createContent(final String parameters, final MenuBar menuBar, final Panel panel) {
		final VerticalLayout panelContent = createPanelContent();

		final String pageId = getPageId(parameters);

		final DataContainer<PersonData, String> dataContainer = getApplicationManager()
				.getDataContainer(PersonData.class);

		final PersonData personData = dataContainer.load(pageId);
		if (personData != null) {

			final DataContainer<ViewRiksdagenPolitician, String> politicianDataContainer = getApplicationManager()
					.getDataContainer(ViewRiksdagenPolitician.class);

			final ViewRiksdagenPolitician viewRiksdagenPolitician = politicianDataContainer.load(personData.getId());

			getPoliticianMenuItemFactory().createPoliticianMenuBar(menuBar, pageId);

			final Label createHeader2Label = LabelFactory
					.createHeader2Label(PoliticianPageMode.DOCUMENTHISTORY.toString());
			panelContent.addComponent(createHeader2Label);

			final DataContainer<ViewRiksdagenPoliticianDocument, String> politicianDocumentDataContainer = getApplicationManager()
					.getDataContainer(ViewRiksdagenPoliticianDocument.class);

			final BeanItemContainer<ViewRiksdagenPoliticianDocument> politicianDocumentDataSource = new BeanItemContainer<>(
					ViewRiksdagenPoliticianDocument.class,
					politicianDocumentDataContainer.findOrderedListByProperty(
							ViewRiksdagenPoliticianDocument_.personReferenceId, personData.getId(),
							ViewRiksdagenPoliticianDocument_.madePublicDate));

			final Grid politicianDocumentBeanItemGrid = getGridFactory().createBasicBeanItemGrid(
					politicianDocumentDataSource, "Documents",
					new String[] { "referenceName", "partyShortCode", "personReferenceId", "roleDescription",
							"documentType", "subType", "title", "subTitle", "org", "rm", "madePublicDate", "id",
							"docId", "tempLabel", "label", "numberValue", "orderNumber", "status" },
					new String[] { "id", "partyShortCode", "personReferenceId", "numberValue", "orderNumber",
							"tempLabel", "referenceName" },
					null, new PageItemPropertyClickListener(UserViews.DOCUMENT_VIEW_NAME, "docId"), null);

			panelContent.addComponent(politicianDocumentBeanItemGrid);

			panelContent.setExpandRatio(createHeader2Label,ContentRatio.SMALL);
			panelContent.setExpandRatio(politicianDocumentBeanItemGrid, ContentRatio.GRID);

			pageCompleted(parameters, panel, pageId, viewRiksdagenPolitician);

		}
		return panelContent;

	}
}