package com.persistent.util;

import java.io.File;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import com.persistent.util.DatabaseUtil;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration("/applicationContext.xml")
public abstract class AbstractFunctionalTest extends AbstractJUnit4SpringContextTests {
    
    private static final String WEB_XML_FILE = "src/main/webapp/WEB-INF/web.xml";
	
	private ServletRunner servletRunner;
    private ServletUnitClient client;
	
    @Autowired
	protected XmlUtil xmlUtil;
    
    @Autowired
	protected DatabaseUtil dbUtil;
    
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
