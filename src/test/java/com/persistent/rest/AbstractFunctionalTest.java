package com.persistent.rest;

import java.io.File;

import org.junit.Before;

import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public abstract class AbstractFunctionalTest {
    
    private static final String WEB_XML_FILE = "src/main/webapp/WEB-INF/web.xml";
	
	private ServletRunner servletRunner;
    private ServletUnitClient client;

    @Before
    public void setupFunctionalTest() throws Exception {
        setupServletClient();
    }

	private void setupServletClient() throws Exception {
        servletRunner = new ServletRunner(new File(WEB_XML_FILE));
        client = servletRunner.newClient();
	}
	
	public ServletUnitClient getClient() {
		return client;
	}
}
