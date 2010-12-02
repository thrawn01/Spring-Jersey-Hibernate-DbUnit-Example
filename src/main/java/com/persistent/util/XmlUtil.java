package com.persistent.util;

import static junit.framework.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.meterware.httpunit.HttpUnitUtils;

@Service("xmlUtil")
public class XmlUtil {
	private JAXBContext context;
    private XPath xPath;
    private SchemaFactory schemaFactory;
	    
	public XmlUtil() throws JAXBException {
	    context = JAXBContext.newInstance( "com.persistent.entity" );
	    schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        xPath = XPathFactory.newInstance().newXPath();
	}

	public void setContext( String classPath ) throws JAXBException{
	    context = JAXBContext.newInstance( classPath );
	}

	public void validate( File xmlFile, File xsdFile ) throws JAXBException, SAXException {
	    Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setSchema(schemaFactory.newSchema( xsdFile ));
        unmarshaller.setEventHandler(
            new ValidationEventHandler() {
                // allow un-marshalling to continue even if there are errors
                public boolean handleEvent(ValidationEvent ve) {
                    // ignore warnings
                    if (ve.getSeverity() != ValidationEvent.WARNING) {
                        ValidationEventLocator vel = ve.getLocator();
                        System.out.println("Line:Col[" + vel.getLineNumber() + ":" + vel.getColumnNumber() + "]:" + ve.getMessage());
                    }
                    return true;
                }
            }
        );
        // Attempt to un-marshal the XML File
        unmarshaller.unmarshal( xmlFile );
	}


	public void printXml( Object poe ) throws JAXBException {
		Marshaller m = context.createMarshaller();
        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        m.marshal(poe, System.out);	
	}
	
	private class XmlUtilSchemaToFile extends SchemaOutputResolver {
		OutputStream output;
		
		public void setOutput( File file ) throws IOException {
			this.output = new FileOutputStream( file );
		}
		
		public void setOutput( ByteArrayOutputStream stream ) throws IOException {
			this.output = stream;
		}
		
	    public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
	        //File file = new File(suggestedFileName);
	        StreamResult result = new StreamResult(output);
	        //result.setSystemId(output.toURI().toURL().toString());
	        result.setSystemId("generated-xsd.xml");
	        return result;
	    }
	}
	
	public void generateSchema( File file ) throws IOException {
		XmlUtilSchemaToFile xml = new XmlUtilSchemaToFile();
		xml.setOutput( file );
		context.generateSchema( xml );
	}
	
	public String generateSchema() throws IOException {
		XmlUtilSchemaToFile xml = new XmlUtilSchemaToFile();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream( 500 );
		xml.setOutput( buffer );
		context.generateSchema( xml );
		return buffer.toString();
	}
	
	public void assertHasValue(String xmlString, String xPathExpression, String value) throws Exception {
        Document document = HttpUnitUtils.parse( new InputSource( new StringReader( xmlString ) ) );
        this.assertHasValue(document, xPathExpression, value ); 
	}
	
	public void assertHasValue(Document document, String xPathExpression, String value) throws Exception {
        NodeList nodeList = (NodeList) this.xPath.evaluate(xPathExpression, document, XPathConstants.NODESET);
        
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
