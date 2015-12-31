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
package com.hack23.cia.service.impl.action.application;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hack23.cia.model.internal.application.system.impl.ApplicationEventGroup;
import com.hack23.cia.model.internal.application.system.impl.ApplicationOperationType;
import com.hack23.cia.model.internal.application.user.impl.UserAccount;
import com.hack23.cia.service.api.action.application.CreateApplicationEventRequest;
import com.hack23.cia.service.api.action.application.CreateApplicationEventResponse;
import com.hack23.cia.service.api.action.application.LogoutRequest;
import com.hack23.cia.service.api.action.application.LogoutResponse;
import com.hack23.cia.service.api.action.common.ServiceResponse.ServiceResult;
import com.hack23.cia.service.impl.action.common.AbstractBusinessServiceImpl;
import com.hack23.cia.service.impl.action.common.BusinessService;

/**
 * The Class LogoutService.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public final class LogoutService extends AbstractBusinessServiceImpl<LogoutRequest, LogoutResponse>
		implements BusinessService<LogoutRequest, LogoutResponse> {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LogoutService.class);

	/** The create application event service. */
	@Autowired
	private BusinessService<CreateApplicationEventRequest, CreateApplicationEventResponse> createApplicationEventService;



	/**
	 * Instantiates a new logout service.
	 */
	public LogoutService() {
		super(LogoutRequest.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.hack23.cia.service.impl.action.common.BusinessService#processService(
	 * com.hack23.cia.service.api.action.common.ServiceRequest)
	 */
	@Override
	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
	public LogoutResponse processService(LogoutRequest serviceRequest) {

		CreateApplicationEventRequest eventRequest = new CreateApplicationEventRequest();
		eventRequest.setEventGroup(ApplicationEventGroup.USER);
		eventRequest.setApplicationOperation(ApplicationOperationType.AUTHENTICATION);
		eventRequest.setActionName(LogoutRequest.class.getSimpleName());
		eventRequest.setSessionId(serviceRequest.getSessionId());


		UserAccount userAccount = getUserAccountFromSecurityContext();


		eventRequest.setElementId(userAccount.getEmail());

		LogoutResponse response;
		if (userAccount != null) {

			eventRequest.setUserId(userAccount.getUserId());


			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
			AnonymousAuthenticationToken anonymousAuthenticationToken = new AnonymousAuthenticationToken(
					serviceRequest.getSessionId(), "ROLE_ANONYMOUS", authorities);
			SecurityContextHolder.getContext().setAuthentication(anonymousAuthenticationToken);

			response=new LogoutResponse(ServiceResult.SUCCESS);
		} else {
			response= new LogoutResponse(ServiceResult.FAILURE);
		}

		eventRequest.setApplicationMessage(response.getResult().toString());

		createApplicationEventService.processService(eventRequest);
		return response;
	}

	private static UserAccount getUserAccountFromSecurityContext() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null) {
			Authentication authentication = context.getAuthentication();
			if (authentication != null) {
				Object principal = authentication.getPrincipal();

				if (principal instanceof UserAccount) {
					UserAccount userAccount = (UserAccount) principal;
					return userAccount;
				}
			}
		}

		return null;
	}

}