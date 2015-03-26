grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.war.file = "target/${appName}.war"
grails.project.dependency.resolution = {
	// inherit Grails' default dependencies
	inherits("global") { // uncomment to disable ehcace
		excludes 'ehcache'}
	log "warn" // LOG level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
	checksums false
	repositories {
		// uncomment the below to enable remote dependency resolution
		// from public Maven repositories
		mavenLocal()
		mavenCentral()
		mavenRepo "http://snapshots.repository.codehaus.org"
		mavenRepo "http://ci-dev.renci.org/nexus/content/repositories/public"
		mavenRepo "http://repository.codehaus.org"
		mavenRepo "http://download.java.net/maven/2/"
		mavenRepo "http://repository.jboss.com/maven2/"
		mavenRepo "https://raw.github.com/DICE-UNC/DICE-Maven/master/releases"
		mavenRepo "https://raw.github.com/DICE-UNC/DICE-Maven/master/snapshots"
		mavenRepo "http://ci-dev.renci.org/nexus/content/repositories/public"
		grailsPlugins()
		grailsHome()
		grailsCentral()
	}
	dependencies {
		// specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
		//compile 'commons-io:commons-io:2.1'
		compile 'commons-io:commons-io:2.2'
		compile 'junit:junit:4.11'
		compile 'cglib:cglib:2.2.2'
		test 'org.mockito:mockito-all:1.8.1'
		compile ('org.irods.jargon:jargon-core:4.0.2-RELEASE') { excludes ([group:'org.jglobus'])}
		compile ('org.irods.jargon:jargon-data-utils:4.0.2-RELEASE')  { excludes ([group:'org.jglobus'])}
		compile ('org.irods.jargon:jargon-ticket:4.0.2-RELEASE')   { excludes ([group:'org.jglobus'])}
		compile ('org.irods.jargon:jargon-user-profile:4.0.2-RELEASE')  { excludes ([group:'org.jglobus'])}
		compile ('org.irods.jargon:jargon-user-tagging:4.0.2-RELEASE')  { excludes ([group:'org.jglobus'])}
		compile ('org.irods.jargon:jargon-workflow:4.0.2-RELEASE')  { excludes ([group:'org.jglobus'])}
		compile ('org.irods.jargon:jargon-ruleservice:4.0.2-RELEASE')  { excludes ([group:'org.jglobus'])}

		provided 'javax.servlet:servlet-api:2.5'
		compile 'org.jsoup:jsoup:0.2.2'

		compile 'org.springframework:spring-expression:4.0.5.RELEASE'
		compile 'org.springframework:spring-aop:4.0.5.RELEASE'


		compile( group: 'log4j', name: 'log4j', version: '1.2.16', export: false )

	}

	plugins {
		//build ":tomcat:7.0.52.1"
		//runtime ":hibernate:3.6.10.10"
		//compile ":asset-pipeline:1.8.3"
		
		// plugins for the build system only
        build ":tomcat:7.0.54"

        // plugins for the compile step
        compile ":scaffolding:2.1.2"
        compile ':cache:1.1.8'
        compile ":asset-pipeline:1.9.9"
        compile ":sass-asset-pipeline:1.9.0"

        // plugins needed at runtime but not for compilation
        runtime ":hibernate4:4.3.5.4" // or ":hibernate:3.6.10.16"
        runtime ":database-migration:1.4.0"
        runtime ":jquery:1.11.1"

        // Uncomment these to enable additional asset-pipeline capabilities
        //compile ":sass-asset-pipeline:1.7.4"
        //compile ":less-asset-pipeline:1.7.0"
        //compile ":coffee-asset-pipeline:1.7.0"
        //compile ":handlebars-asset-pipeline:1.3.0.3"

        // Load the Uploadr plugin
        compile ":uploadr:1.1.1"
		
	}
}
