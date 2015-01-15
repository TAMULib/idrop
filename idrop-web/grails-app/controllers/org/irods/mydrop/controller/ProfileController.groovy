package org.irods.mydrop.controller

import org.irods.jargon.core.connection.IRODSAccount
import org.irods.jargon.core.pub.IRODSAccessObjectFactory
import org.irods.jargon.userprofile.UserProfile
import org.irods.mydrop.service.ProfileService
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

class ProfileController {
	IRODSAccessObjectFactory irodsAccessObjectFactory
	IRODSAccount irodsAccount
	ProfileService profileService
	def grailsApplication

	/**
	 * Interceptor grabs IRODSAccount from the SecurityContextHolder
	 */
	def beforeInterceptor = [action:this.&auth]

	def auth() {
		if(!session["SPRING_SECURITY_CONTEXT"]) {
			redirect(controller:"login", action:"login")
			return false
		}
		irodsAccount = session["SPRING_SECURITY_CONTEXT"]
	}

	def afterInterceptor = {
		log.debug("closing the session")
		irodsAccessObjectFactory.closeSession()
	}

	/**
	 * Initial listing of profile data, this will create a profile if none exists
	 * @return
	 */
	def index() {
		log.info "index()"
		if (irodsAccount.userName == IRODSAccount.PUBLIC_USERNAME) {
			render(view:"noProfileData")
		} else {

			try {
				UserProfile userProfile = profileService.retrieveProfile(irodsAccount)
				ProfileCommand profileCommand = new ProfileCommand()
				profileCommand.userName = userProfile.userName ? Jsoup.clean(userProfile.userName,Whitelist.basic()) : ""
				profileCommand.nickName = userProfile.userProfilePublicFields ? Jsoup.clean(userProfile.userProfilePublicFields.nickName,Whitelist.basic()) : ""
				profileCommand.givenName = userProfile.userProfilePublicFields.givenName ? Jsoup.clean(userProfile.userProfilePublicFields.givenName,Whitelist.basic()) : ""
				profileCommand.lastName = userProfile.userProfilePublicFields.s ? Jsoup.clean(userProfile.userProfilePublicFields.sn,Whitelist.basic()) : ""
				profileCommand.city= userProfile.userProfilePublicFields.localityName ? Jsoup.clean(userProfile.userProfilePublicFields.localityName,Whitelist.basic()) : ""
				profileCommand.state= userProfile.userProfilePublicFields.st ? Jsoup.clean(userProfile.userProfilePublicFields.st,Whitelist.basic()) : ""
				profileCommand.email = userProfile.userProfileProtectedFields.mail ? Jsoup.clean(userProfile.userProfileProtectedFields.mail,Whitelist.basic()) : ""
				profileCommand.description = userProfile.userProfilePublicFields.description ? Jsoup.clean(userProfile.userProfilePublicFields.description,Whitelist.basic()) : ""
				profileCommand.labeledURL = userProfile.userProfilePublicFields.labeledURL ? Jsoup.clean(userProfile.userProfilePublicFields.labeledURL,Whitelist.basic()) : ""
				profileCommand.postalAddress = userProfile.userProfilePublicFields.postalAddress ? Jsoup.clean(userProfile.userProfilePublicFields.postalAddress,Whitelist.basic()) : ""
				profileCommand.postalCode = userProfile.userProfilePublicFields.postalCode ? Jsoup.clean(userProfile.userProfilePublicFields.postalCode,Whitelist.basic()) : ""
				profileCommand.postOfficeBox = userProfile.userProfilePublicFields.postOfficeBox ? Jsoup.clean(userProfile.userProfilePublicFields.postOfficeBox,Whitelist.basic()) : ""
				profileCommand.telephoneNumber = userProfile.userProfilePublicFields.telephoneNumber ? Jsoup.clean(userProfile.userProfilePublicFields.telephoneNumber,Whitelist.basic()) : ""
				profileCommand.title = userProfile.userProfilePublicFields.title ? Jsoup.clean(userProfile.userProfilePublicFields.title,Whitelist.basic()) : ""

				render(view:"index", model:[userProfile:profileCommand])
			} catch (Exception e) {
				log.error("eception retrieving or creating user profile", e)
				response.sendError(500,e.message)
			}
		}
	}


	/**
	 * Update the profile
	 * @return
	 */
	def updateProfile(ProfileCommand profileCommand) {

		log.info("updateProfile")
		log.info "profileCommand: ${profileCommand}"

		/**
		 * If there is an error send back the view for redisplay with error messages
		 */
		if (!profileCommand.validate()) {
			log.info("errors in page, returning with error info")
			flash.error =  message(code:"error.data.error")
			render(view:"index", model:[userProfile:profileCommand])
			return
		}

		log.info("edits pass")


		/*
		 * Massage the params into the user profile
		 */


		UserProfile userProfile
		try {
			userProfile = profileService.retrieveProfile(irodsAccount)
		} catch (Exception e) {
			log.error("error retrieving user profile", e)
			flash.error =  e.message
			render(view:"index", model:[userProfile:profileCommand])
			return
		}

		userProfile.userName = irodsAccount.userName
		profileCommand.userName = userProfile.userName ? Jsoup.clean(userProfile.userName,Whitelist.basic()) : ""
		profileCommand.nickName = userProfile.userProfilePublicFields ? Jsoup.clean(userProfile.userProfilePublicFields.nickName,Whitelist.basic()) : ""
		profileCommand.givenName = userProfile.userProfilePublicFields.givenName ? Jsoup.clean(userProfile.userProfilePublicFields.givenName,Whitelist.basic()) : ""
		profileCommand.lastName = userProfile.userProfilePublicFields.s ? Jsoup.clean(userProfile.userProfilePublicFields.sn,Whitelist.basic()) : ""
		profileCommand.city= userProfile.userProfilePublicFields.localityName ? Jsoup.clean(userProfile.userProfilePublicFields.localityName,Whitelist.basic()) : ""
		profileCommand.state= userProfile.userProfilePublicFields.st ? Jsoup.clean(userProfile.userProfilePublicFields.st,Whitelist.basic()) : ""
		profileCommand.email = userProfile.userProfileProtectedFields.mail ? Jsoup.clean(userProfile.userProfileProtectedFields.mail,Whitelist.basic()) : ""
		profileCommand.description = userProfile.userProfilePublicFields.description ? Jsoup.clean(userProfile.userProfilePublicFields.description,Whitelist.basic()) : ""
		profileCommand.labeledURL = userProfile.userProfilePublicFields.labeledURL ? Jsoup.clean(userProfile.userProfilePublicFields.labeledURL,Whitelist.basic()) : ""
		profileCommand.postalAddress = userProfile.userProfilePublicFields.postalAddress ? Jsoup.clean(userProfile.userProfilePublicFields.postalAddress,Whitelist.basic()) : ""
		profileCommand.postalCode = userProfile.userProfilePublicFields.postalCode ? Jsoup.clean(userProfile.userProfilePublicFields.postalCode,Whitelist.basic()) : ""
		profileCommand.postOfficeBox = userProfile.userProfilePublicFields.postOfficeBox ? Jsoup.clean(userProfile.userProfilePublicFields.postOfficeBox,Whitelist.basic()) : ""
		profileCommand.telephoneNumber = userProfile.userProfilePublicFields.telephoneNumber ? Jsoup.clean(userProfile.userProfilePublicFields.telephoneNumber,Whitelist.basic()) : ""
		profileCommand.title = userProfile.userProfilePublicFields.title ? Jsoup.clean(userProfile.userProfilePublicFields.title,Whitelist.basic()) : ""

		log.info "updating profile...."
		try {
			profileService.updateProfile(irodsAccount, userProfile)
		} catch (Exception e) {
			log.error("error updating user profile", e)
			flash.error =  e.message
			render(view:"index", model:[userProfile:profileCommand])
			return
		}

		log.info "updated"
		flash.message =  message(code:"message.update.successful")

		redirect(view:"index", model:[userProfile:profileCommand])
	}
}

class ProfileCommand {
	String userName
	String nickName
	String givenName
	String lastName
	String city
	String state
	String email
	String description
	String labeledURL
	String postalAddress
	String postalCode
	String postOfficeBox
	String telephoneNumber
	String title

	static constraints = {
		nickName(null:false)
		givenName(null:false)
		lastName(null:false)
		city(null:false)
		state(null:false)
		email(null:false)
		description(null:false)
		labeledURL(null:false)
		postalAddress(null:false)
		postalCode(null:false)
		postOfficeBox(null:false)
		telephoneNumber(null:false)
		title(null:false)
	}
}

