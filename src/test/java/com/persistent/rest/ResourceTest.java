package com.persistent.rest;

import org.junit.Test;
import com.meterware.httpunit.WebResponse;

public class ResourceTest extends AbstractFunctionalTest {

	@Test
	public void testFoo() throws Exception {
		WebResponse response = getClient().getResponse("http://localhost/webresources/myresource");
		System.out.println(response.getText());
	}
}
