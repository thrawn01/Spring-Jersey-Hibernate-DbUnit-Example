package com.persistent.rest;

import static junit.framework.Assert.fail;

import java.text.MessageFormat;

import org.junit.Test;
import com.persistent.util.XmlUtil;
import org.springframework.core.io.FileSystemResource;


public class XmlUtilTest {
	
	@Test
	public void testSchemaGenerationToFile() throws Exception {
		// TODO: Need to ensure the file doesn't exist already
		XmlUtil xml = new XmlUtil();
		FileSystemResource file = new FileSystemResource( "src/main/resources/generated.xsd" );
		xml.generateSchema( file.getFile() );
		if( !file.exists() ) {
			fail(MessageFormat.format("file: {0} doesn't exist", file.getFilename()));
		}
	}
	
	@Test
	public void testSchemaGenerationToString() throws Exception {
		XmlUtil xml = new XmlUtil();
		String result = xml.generateSchema( );
		// Assert 'result' has the XML Schema
		xml.assertHasValue(result, "/schema/element[@name='person']/@type", "person" );
	}
	
}
