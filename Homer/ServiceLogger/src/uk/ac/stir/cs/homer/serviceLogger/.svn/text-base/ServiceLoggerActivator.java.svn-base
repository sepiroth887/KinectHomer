package uk.ac.stir.cs.homer.serviceLogger;


import java.io.ByteArrayInputStream;
import java.util.logging.LogManager;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;


public class ServiceLoggerActivator implements BundleActivator  {

	private static final String LOG_CONFIG = "handlers=\n" +
			"java.level=OFF\n" +
			"sun.level=OFF\n" +
			"javax.level=OFF\n" +
			".level=ALL";
	
	@Override
	public void start(BundleContext context) throws Exception {
		
		LogManager logManager = java.util.logging.LogManager.getLogManager();
		logManager.readConfiguration(new ByteArrayInputStream(LOG_CONFIG.getBytes()));
		SLF4JBridgeHandler.install();
		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("Logger started in slf4j!");
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
