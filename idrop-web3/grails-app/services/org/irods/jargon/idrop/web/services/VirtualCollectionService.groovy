package org.irods.jargon.idrop.web.services

import javax.servlet.http.HttpSession

import org.irods.jargon.core.connection.IRODSAccount
import org.irods.jargon.core.exception.DataNotFoundException
import org.irods.jargon.core.pub.IRODSAccessObjectFactory
import org.irods.jargon.vircoll.VirtualCollectionDiscoveryService
import org.irods.jargon.vircoll.VirtualCollectionExecutorFactory
import org.irods.jargon.vircoll.impl.VirtualCollectionDiscoveryServiceImpl

class VirtualCollectionService {

	static transactional = false
	IRODSAccessObjectFactory irodsAccessObjectFactory
	VirtualCollectionFactoryCreatorService virtualCollectionFactoryCreatorService
	public enum ListingType {
		ALL, COLLECTIONS, DATA_OBJECTS
	}


	/**
	 * Get the default list of virtual collections associated with a user
	 * @param irodsAccount {@link IRODSAccount} for the target user
	 * @return <code>List</code> of {@link AbstractVirtualCollection} 
	 */
	def virtualCollectionHomeListingForUser(IRODSAccount irodsAccount, HttpSession session) {

		log.info("virtualCollectionHomeListingForUser()")

		if (!irodsAccount) {
			throw new IllegalArgumentException("null irodsAccount")
		}

		log.info("irodsAccount: ${irodsAccount}")

		VirtualCollectionDiscoveryService virtualCollectionDiscoveryService = new VirtualCollectionDiscoveryServiceImpl(irodsAccessObjectFactory, irodsAccount)

		def colls = virtualCollectionDiscoveryService.listDefaultUserCollections()

		session.virtualCollections = colls

		return colls
	}

	/**
	 * Get the requested virtual collection data, which should be cached in the session. If it is not yet cached,the vcs for the user will be refreshed
	 * @param vcName <code>String</coee> with the name of the virtual collection to retrieve
	 * @param irodsAccount
	 * @param session
	 * @return
	 */
	def virtualCollectionDetails(String vcName, IRODSAccount irodsAccount, HttpSession session) {

		log.info("virtualCollectionDetails()")

		if (!vcName) {
			throw new IllegalArgumentException("null or empty vcName")
		}

		if (!irodsAccount) {
			throw new IllegalArgumentException("null irodsAccount")
		}

		if (!session) {
			throw new IllegalArgumentException("null session")
		}

		log.info("getting info for vc name:${vcName}")

		def vcsInSession = session.virtualCollections

		if (!vcsInSession) {
			log.info("re-acquiring listing")
			vcsInSession = virtualCollectionHomeListingForUser(irodsAccount, session)
		}

		for (vc in vcsInSession) {
			if (vc.uniqueName == vcName) {
				log.info("found: ${vc}")
				return vc
			}
		}

		// should have returned....what happened?
		log.error("no vc found for name:${vcName}")
		throw new DataNotFoundException("could not find requested virtual collection")

	}


	/**
	 * Given a virtual collection type, generate a listing of data for display
	 * @param vcName
	 * @param listingType
	 * @param offset
	 * @param irodsAccount
	 * @return
	 */
	def virtualCollectionListing(String vcName, ListingType listingType, int offset, IRODSAccount irodsAccount, HttpSession session) {

		log.info("virtualCollectionListing")

		if (!vcName) {
			throw new IllegalArgumentException("null or empty vcName")
		}

		if (!listingType) {
			throw new IllegalArgumentException("null or empty listingType")
		}

		if (!irodsAccount) {
			throw new IllegalArgumentException("null irodsAccount")
		}

		log.info("listing for vc: ${vcName} listing type:${listingType} offset:{$offset}")

		def virColls = session.virtualCollections

		def virColl
		for (virCollEntry in virColls) {
			if (virCollEntry.uniqueName == vcName) {
				log.info("found it")
				session.virtualCollection = virCollEntry
				break
			}
		}

		// TODO: if not found, or missing, should I refresh?

		if (!session.virtualCollection) {
			throw new Exception("no virtual collections found for name:${vcName}")
		}

		VirtualCollectionExecutorFactory executorFactory = virtualCollectionFactoryCreatorService.instanceVirtualCollectionExecutorFactory(irodsAccount)

		def executor = executorFactory.instanceExecutorBasedOnVirtualCollection(session.virtualCollection)

		if (listingType == ListingType.ALL) {
			return executor.queryAll(offset)
		} else {
			throw new UnsupportedOperationException("not supported yet")
		}
	}
}