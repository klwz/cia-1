/*
 * Copyright 2010 James Pether Sörling
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
package com.hack23.cia.service.data.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.hack23.cia.model.external.riksdagen.documentcontent.impl.DocumentContentData;
import com.hack23.cia.model.external.riksdagen.documentcontent.impl.DocumentContentData_;
import com.hack23.cia.service.data.api.DocumentContentDataDAO;

/**
 * The Class DocumentContentDataDAOImpl.
 */
@Repository("DocumentContentDataDAO")
public final class DocumentContentDataDAOImpl extends
AbstractGenericDAOImpl<DocumentContentData, Long>
implements DocumentContentDataDAO {

	/** The entity manager. */
	@PersistenceContext(name = "ciaPersistenceUnit")
	private EntityManager entityManager;

	/**
	 * Instantiates a new document content data dao impl.
	 */
	public DocumentContentDataDAOImpl() {
		super(DocumentContentData.class);
	}

	/** (non-Javadoc)
	 * @see com.hack23.cia.service.data.api.DocumentContentDataDAO#checkDocumentContentData(java.lang.String)
	 */
	@Override
	public boolean checkDocumentContentData(final String documentId) {
		final CriteriaQuery<DocumentContentData> criteriaQuery = getCriteriaBuilder()
				.createQuery(DocumentContentData.class);
		final Root<DocumentContentData> root = criteriaQuery.from(DocumentContentData.class);
		criteriaQuery.select(root);
		final Predicate condition = getCriteriaBuilder().equal(root.get(DocumentContentData_.id), documentId);
		criteriaQuery.where(condition);
		final TypedQuery<DocumentContentData> typedQuery = getEntityManager()
				.createQuery(criteriaQuery);
		addCacheHints(typedQuery, "checkDocumentContentData");

		final List<DocumentContentData> resultList = typedQuery.getResultList();

		return !resultList.isEmpty();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see
	 * com.hack23.cia.service.data.impl.AbstractRiksdagenDAOImpl#getEntityManager
	 * ()
	 */
	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}


	/** (non-Javadoc)
	 * @see com.hack23.cia.service.data.api.DocumentContentDataDAO#getIdList()
	 */
	@Override
	public List<String> getIdList() {
		final CriteriaQuery<String> criteria = getCriteriaBuilder().createQuery(String.class);
		final Root<DocumentContentData> root = criteria.from(DocumentContentData.class);
		criteria.select(root.get(DocumentContentData_.id));
		return getEntityManager().createQuery(criteria).getResultList();
	}

	/** (non-Javadoc)
	 * @see com.hack23.cia.service.data.api.AbstractGenericDAO#getSize()
	 */
	@Override
	public Long getSize() {
		return (long) getIdList().size();
	}

}
