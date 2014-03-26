package org.irods.jargon.idrop.web.controllers

import grails.test.mixin.Mock
import grails.test.mixin.TestFor

import org.irods.jargon.core.connection.AuthScheme
import org.irods.jargon.core.connection.auth.AuthResponse
import org.irods.jargon.core.exception.AuthenticationException
import org.irods.jargon.idrop.web.filters.AuthenticationFilters
import org.irods.jargon.idrop.web.filters.ConnectionClosingFilterFilters
import org.irods.jargon.idrop.web.services.AuthenticationService

import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(LoginController)
@Mock([AuthenticationFilters, ConnectionClosingFilterFilters])

class LoginControllerSpec extends Specification  {

	void setup() {
		//mockCommandObject(LoginCommand)
	}

	void "test authenticate with a invalid credential"() {
		given:
		def authMock = mockFor(AuthenticationService)
		authMock.demand.authenticate { irodsAccount ->
			throw new AuthenticationException("no way")
		}

		controller.authenticationService = authMock.createMock()
 
		def loginCommand = new LoginCommand() 
		loginCommand.host = "host"
		loginCommand.port = 1247
		loginCommand.zone = "zone"
		loginCommand.userName = "userName"
		loginCommand.password = "password"
		loginCommand.defaultStorageResource = "defaultStorageResource"
		loginCommand.authType = AuthScheme.STANDARD
                                     // loginCommand = loginComand.createMock()
                                     loginCommand.validate()
		when:
		controller.save(loginCommand)

		then:
		thrown(AuthenticationException)
	}

	void "test authenticate with a null command"() {
		given:
		def authMock = mockFor(AuthenticationService)

		controller.authenticationService = authMock.createMock()

		LoginCommand loginCommand = null

		when:
		controller.save(loginCommand)

		then:
		thrown(IllegalArgumentException)
	}

	void "test authenticate with a valid credential"() {
		given:
		def authMock = mockFor(AuthenticationService)
		authMock.demand.authenticate { irodsAccount ->
			return new AuthResponse()
		}

		controller.authenticationService = authMock.createMock()

		def loginCommand = mockFor(LoginCommand) 
		loginCommand.host = "host"
		loginCommand.port = 1247
		loginCommand.zone = "zone"
		loginCommand.userName = "userName"
		loginCommand.password = "password"
		loginCommand.defaultStorageResource = "defaultStorageResource"
		loginCommand.authType = AuthScheme.STANDARD
                
                                      loginCommand.validate()


		when:
		controller.save(loginCommand)

		then:
		controller.response.status == 200
		controller.session.authenticationSession != null
		log.info("response:${response.text}")
		assert '{"authMessage":"","authenticatedIRODSAccount":null,"authenticatingIRODSAccount":null,"challengeValue":"","class":"org.irods.jargon.core.connection.auth.AuthResponse","responseProperties":{},"startupResponse":null,"successful":false}' == response.text
	}

	void "test authenticate with a missing user gives validation error"() {
		given:
		def authMock = mockFor(AuthenticationService)
		authMock.demand.authenticate { irodsAccount ->
			return new AuthResponse()
		}

		controller.authenticationService = authMock.createMock()

		LoginCommand loginCommand = new LoginCommand()
		loginCommand.host = "host"
		loginCommand.port = 1247
		loginCommand.zone = "zone"
		loginCommand.userName = ""
		loginCommand.password = "password"
		loginCommand.defaultStorageResource = "defaultStorageResource"
		loginCommand.authType = AuthScheme.STANDARD

		assert !loginCommand.validate()
	}
}
