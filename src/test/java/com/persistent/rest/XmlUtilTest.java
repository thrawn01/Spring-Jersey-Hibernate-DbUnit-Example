package com.persistent.rest;

import java.io.File;
import org.junit.Test;
import com.persistent.util.XmlUtil;

public class XmlUtilTest {
	
	@Test
	public void testSchemaGeneration() throws Exception {
		XmlUtil xml = new XmlUtil();
		xml.generateSchema( new File("src/main/resources") );
	}
	
}
