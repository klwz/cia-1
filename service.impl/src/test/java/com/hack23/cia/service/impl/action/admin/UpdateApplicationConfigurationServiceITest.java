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
package com.hack23.cia.service.impl.action.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hack23.cia.model.internal.application.system.impl.ApplicationConfiguration;
import com.hack23.cia.model.internal.application.system.impl.ApplicationSessionType;
import com.hack23.cia.model.internal.application.system.impl.ConfigurationGroup;
import com.hack23.cia.service.api.ApplicationManager;
import com.hack23.cia.service.api.action.admin.UpdateApplicationConfigurationRequest;
import com.hack23.cia.service.api.action.admin.UpdateApplicationConfigurationResponse;
import com.hack23.cia.service.api.action.application.CreateApplicationSessionRequest;
import com.hack23.cia.service.api.action.application.CreateApplicationSessionResponse;
import com.hack23.cia.service.api.action.common.ServiceResponse.ServiceResult;
import com.hack23.cia.service.data.api.ApplicationConfigurationService;
import com.hack23.cia.service.impl.AbstractServiceFunctionalIntegrationTest;

/**
 * The Class UpdateApplicationConfigurationServiceITest.
 */
public class UpdateApplicationConfigurationServiceITest extends AbstractServiceFunctionalIntegrationTest {

	/** The application manager. */
	@Autowired
	private ApplicationManager applicationManager;

	/** The application configuration service. */
	@Autowired
	private ApplicationConfigurationService applicationConfigurationService;


	/**
	 * Success test.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void successTest() throws Exception {

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

		SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("key", "principal", authorities));

		CreateApplicationSessionRequest createTestApplicationSession = createTestApplicationSession();

		String randomUUID = UUID.randomUUID().toString();
		ApplicationConfiguration applicationConfigurationToUpdate = applicationConfigurationService.checkValueOrLoadDefault(
				"UpdateApplicationRequestTest property",
				"UpdateApplicationRequestTest should be set to true/false",
				ConfigurationGroup.AUTHORIZATION, UpdateApplicationConfigurationServiceITest.class.getSimpleName(),
				"UpdateApplicationConfigurationService ITest", "FunctionalIntegrationTest",
				"UpdateApplicationRequestTest." + randomUUID, "true");


		UpdateApplicationConfigurationRequest serviceRequest = new UpdateApplicationConfigurationRequest();
		serviceRequest.setApplicationConfigurationId(applicationConfigurationToUpdate.getHjid());
		serviceRequest.setSessionId(createTestApplicationSession.getSessionId());

		serviceRequest.setComponentDescription("componentDescription");
		serviceRequest.setConfigDescription("configDescription");
		serviceRequest.setConfigTitle("configTitle");
		serviceRequest.setComponentTitle("componentTitle");
		serviceRequest.setPropertyValue("false");

		UpdateApplicationConfigurationResponse  response = (UpdateApplicationConfigurationResponse) applicationManager.service(serviceRequest);


		assertNotNull("Expect a result",response);
		assertEquals("Expect success", ServiceResult.SUCCESS,response.getResult());


		ApplicationConfiguration applicationConfigurationUpdated = applicationConfigurationService.checkValueOrLoadDefault(
				"UpdateApplicationRequestTest property",
				"UpdateApplicationRequestTest should be set to true/false",
				ConfigurationGroup.AUTHORIZATION, UpdateApplicationConfigurationServiceITest.class.getSimpleName(),
				"UpdateApplicationConfigurationService ITest", "FunctionalIntegrationTest",
				"UpdateApplicationRequestTest." + randomUUID, "true");


		assertEquals("false",applicationConfigurationUpdated.getPropertyValue());
		assertEquals("configTitle",applicationConfigurationUpdated.getConfigTitle());
		assertEquals("configDescription",applicationConfigurationUpdated.getConfigDescription());
		assertEquals("componentTitle",applicationConfigurationUpdated.getComponentTitle());
		assertEquals("componentDescription",applicationConfigurationUpdated.getComponentDescription());

	}

	/**
	 * Creates the test application session.
	 *
	 * @return the creates the application session request
	 */
	private CreateApplicationSessionRequest createTestApplicationSession() {
		CreateApplicationSessionRequest serviceRequest = new CreateApplicationSessionRequest();
		serviceRequest.setIpInformation("8.8.8.8");
		serviceRequest.setLocale("en_US.UTF-8");
		serviceRequest.setOperatingSystem("LINUX");
		serviceRequest.setSessionId(UUID.randomUUID().toString());
		serviceRequest.setSessionType(ApplicationSessionType.ANONYMOUS);
		serviceRequest.setUserAgentInformation("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:42.0) Gecko/20100101 Firefox/42.0");

		CreateApplicationSessionResponse sessionResponse = (CreateApplicationSessionResponse) applicationManager
				.service(serviceRequest);
		return serviceRequest;
	}


}