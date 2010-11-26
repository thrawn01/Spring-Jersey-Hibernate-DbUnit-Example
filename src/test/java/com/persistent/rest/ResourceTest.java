package com.persistent.rest;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import com.meterware.httpunit.WebResponse;
import com.persistent.entity.Person;
import com.persistent.util.AbstractFunctionalTest;


public class ResourceTest extends AbstractFunctionalTest {
	
	@Before
	public void setUp() throws SQLException{
		dbUtil.createTables( new Class[] { Person.class } );
		dbUtil.loadData("src/test/resources/test-dataset");
	}
	
	@Test
	public void testFoo() throws Exception {
		WebResponse response = getClient().getResponse("http://localhost/webresources/myresource");
		System.out.println(response.getText());
	}
}
