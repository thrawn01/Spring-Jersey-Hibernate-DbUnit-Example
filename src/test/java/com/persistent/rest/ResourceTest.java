package com.persistent.rest;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import com.meterware.httpunit.WebResponse;
import com.persistent.util.AbstractFunctionalTest;


public class ResourceTest extends AbstractFunctionalTest {
	
	@Before
	public void setUp(){
		dbUtil.printProperties();
	}
	
	@Test
	public void testFoo() throws Exception {
		WebResponse response = getClient().getResponse("http://localhost/webresources/myresource");
		System.out.println(response.getText());
	}
}
