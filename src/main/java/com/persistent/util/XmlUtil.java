package com.persistent.util;

import java.io.File;
import java.io.IOException;

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

import org.xml.sax.SAXException;

public class XmlUtil {
	private JAXBContext context;
    private SchemaFactory schemaFactory;
	    
	public XmlUtil() throws JAXBException {
	    context = JAXBContext.newInstance( "com.persistent.entity" );
	    schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
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
	
	private class XmlUtilSchemaOutputResolver extends SchemaOutputResolver {
		 
	    public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
	        File file = new File(suggestedFileName);
	        StreamResult result = new StreamResult(file);
	        result.setSystemId(file.toURI().toURL().toString());
	        return result;
	    }
	}
	
	public void generateSchema( File file ) throws IOException {
		context.generateSchema( new XmlUtilSchemaOutputResolver() );
	}
}
