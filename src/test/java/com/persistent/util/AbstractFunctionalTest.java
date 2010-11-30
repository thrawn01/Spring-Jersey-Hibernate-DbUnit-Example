package com.persistent.util;

import static junit.framework.Assert.fail;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import com.persistent.util.DatabaseUtil;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

//import org.hibernate.engine.ExecuteUpdateResultCheckStyle;

@ContextConfiguration("/applicationContext.xml")
public abstract class AbstractFunctionalTest extends AbstractJUnit4SpringContextTests {
    
    private static final String WEB_XML_FILE = "src/main/webapp/WEB-INF/web.xml";
	
	private ServletRunner servletRunner;
    private ServletUnitClient client;
	private final static XPath xPath;
    
    static {
        xPath = XPathFactory.newInstance().newXPath();
    }
    
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
	
	public void assertHasValue(Document document, String xPathExpression, String value) throws Exception {
        NodeList nodeList = (NodeList) xPath.evaluate(xPathExpression, document, XPathConstants.NODESET);
        
        // verify the node values
        for (int i = 0; i < nodeList.getLength(); i++) {
            String nodeValue = nodeList.item(i).getNodeValue();
            
            if (value.equals(nodeValue)) {
                return;
            }
        }
        
        // assertion failed, so build a set of values for the response.
        List<String> values = new ArrayList<String>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            values.add(nodeList.item(i).getNodeValue());
        }
        
        fail(MessageFormat.format("Document does not have a node with value: {0} at {1}, values: {2}", value, xPathExpression, values));
    }
}
