package com.persistent.util;

import java.io.File;
import java.io.StringWriter;

import com.google.common.base.Strings;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

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
	
    public String formatXml(final String xml){

      if (Strings.isNullOrEmpty(xml)) {
          throw new RuntimeException("xml was null or blank in prettyPrint()");
      }

      try {
          final OutputFormat format = OutputFormat.createPrettyPrint();
          final org.dom4j.Document document = DocumentHelper.parseText(xml);
          StringWriter string = new StringWriter();
          final XMLWriter writer = new XMLWriter(string, format);
          writer.write(document);
          return string.toString();
      }
      catch (Exception e) {
          throw new RuntimeException("Error pretty printing xml:\n" + xml, e);
      }
    }
}
