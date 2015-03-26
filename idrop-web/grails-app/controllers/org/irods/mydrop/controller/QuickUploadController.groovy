package org.irods.mydrop.controller

import java.io.File

import org.irods.jargon.core.connection.SettableJargonProperties
import org.irods.jargon.core.connection.JargonProperties
import org.irods.jargon.core.connection.IRODSSession
import org.irods.jargon.core.connection.IRODSAccount
import org.irods.jargon.core.exception.JargonException
import org.irods.jargon.core.exception.NoResourceDefinedException
import org.irods.jargon.core.protovalues.ChecksumEncodingEnum
import org.irods.jargon.core.pub.DataObjectAOImpl
import org.irods.jargon.core.pub.io.IRODSFileFactoryImpl
import org.irods.jargon.core.pub.IRODSAccessObjectFactory
//import org.irods.jargon.core.pub.Stream2StreamAO
import org.irods.jargon.core.pub.io.IRODSFile
import org.irods.jargon.core.pub.io.IRODSFileFactory
import org.springframework.web.multipart.MultipartFile

class QuickUploadController {
	IRODSAccessObjectFactory irodsAccessObjectFactory
	IRODSAccount irodsAccount
	static long MAX_UPLOAD = 3221225472  // shooting for 3GB max, make parm?

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
	 * Process an actual call to upload data to iRODS as a multi-part file
	 */
	def upload = {
		log.info("upload action in file controller")
		MultipartFile f = request.getFile('file')
		def name = f.getOriginalFilename()

		log.info("f is ${f}")
		log.info("length of f is ${f.size}")
		log.info("max upload size is ${MAX_UPLOAD}")

		if (f.size > MAX_UPLOAD) {
			log.error("file size is too large, send error message to use bulk upload")
			def message = message(code:"error.use.bulk.upload")
			response.sendError(500,message)
			return
		} else if (f.size == 0) {
			log.error("file is zero length")
			def message = message(code:"error.zero.length.upload")
			response.sendError(500,message)
			return
		}

		log.info("name is : ${name}")
		def irodsCollectionPath = params.collectionParentName

		if (f == null || f.empty) {
			log.error("no file to upload")
			throw new JargonException("No file to upload")
		}

		if (irodsCollectionPath == null || irodsCollectionPath.empty) {
			log.error("no target iRODS collection given in upload request")
			throw new JargonException("No iRODS target collection given for upload")
		}

		InputStream fis = null
		
		OutputStream fos = null
		
		log.info("building irodsFile for file name: ${name}")


		def localPath = "/tmp/uploadr/" + irodsAccount.getUserName()
		
		File file = new File(localPath + "/${name}")
		
		
		try {
		
			fis = new BufferedInputStream(f.getInputStream())
			
			fos = new FileOutputStream(file)
			
			int read = 0
			byte[] bytes = new byte[1024]
 
			while ((read = fis.read(bytes)) != -1) {
				fos.write(bytes, 0, read)
			}
			
			
			//IRODSFileFactory irodsFileFactory = irodsAccessObjectFactory.getIRODSFileFactory(irodsAccount)			
			//IRODSFile targetFile = irodsFileFactory.instanceIRODSFile(irodsCollectionPath, name)			
			//targetFile.setResource(irodsAccount.defaultStorageResource)
			
			//Stream2StreamAO stream2Stream = irodsAccessObjectFactory.getStream2StreamAO(irodsAccount)
			
			//stream2Stream.transferStreamToFileUsingIOStreams(fis, targetFile, f.size, 0)
						
			
			IRODSSession session = irodsAccessObjectFactory.getIrodsSession()

            JargonProperties properties = session.getJargonProperties()

            SettableJargonProperties sProperties = new SettableJargonProperties(properties)
       		sProperties.setComputeChecksumAfterTransfer(true)
       		sProperties.setComputeAndVerifyChecksumAfterTransfer(true)            
       		sProperties.setChecksumEncoding(ChecksumEncodingEnum.MD5)

      		session.setJargonProperties(sProperties)
            
			irodsAccessObjectFactory.setIrodsSession(session)
			
			IRODSFileFactory  irodsFileFactory = irodsAccessObjectFactory.getIRODSFileFactory(irodsAccount)
			
			IRODSFile targetFile = irodsFileFactory.instanceIRODSFile(irodsCollectionPath, name)
			targetFile.setResource(irodsAccount.defaultStorageResource)
			
			
			DataObjectAOImpl dataObjectAO = (DataObjectAOImpl) irodsAccessObjectFactory.getDataObjectAO(irodsAccount);
 
 			try {
 		 		dataObjectAO.putLocalDataObjectToIRODS(file, targetFile, false) 		 			
 		 	}
 		 	catch(Exception _e) { 		 		
 		 		log.info(_e.getMessage())
 		 		log.info(targetFile.toString())
 		 		log.info(irodsCollectionPath.toString())
 		 	}
			
			// delete file after uploaded
    		if(file.delete()) {
        		log.info("file deleted")
    		}
    		else {
        		log.error("file not deleted")
    		}
			
			
		} catch (NoResourceDefinedException nrd) {
			log.error("no resource defined exception", nrd)
			response.sendError(500, message(code:"message.no.resource"))
		} catch (Exception e) {
			log.error("exception in upload transfer", e)
			response.sendError(500, message(code:"message.error.in.upload"))
		} finally {
			// stream2Stream will close input and output streams
			
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					// fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	 
			}			
			
		}

		render "{\"name\":\"${name}\",\"type\":\"image/jpeg\",\"size\":\"1000\"}"
	}

}
