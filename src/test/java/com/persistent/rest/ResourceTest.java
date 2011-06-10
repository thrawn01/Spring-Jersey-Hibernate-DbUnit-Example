package com.persistent.rest;

import com.persistent.util.AbstractFunctionalTest;
import com.meterware.httpunit.WebResponse;
import com.persistent.entity.Person;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;


public class ResourceTest extends AbstractFunctionalTest {
	
	@Before
	public void setUp() throws SQLException{
		
		// As your project and list of tables grows, specifying 
		// what tables to load will be more important
		dbUtil.createTables( new Class[] { Person.class } );
		
		// Different functional tests require different data sets
		dbUtil.loadData("src/test/resources/test-dataset");
	}
	
	@Test
	public void testFoo() throws Exception {
		
		WebResponse response = getClient().getResponse("http://localhost/webresources/persons");
		
		// Validate our XML Contains "Derrick Wippler"
		xmlUtil.assertHasValue(response.getDOM(), "/persons/person[id=1]/id/text()", "1");
		xmlUtil.assertHasValue(response.getDOM(), "/persons/person[id=1]/name/text()", "Derrick Wippler");
		xmlUtil.assertHasValue(response.getDOM(), "/persons/person[id=1]/age/text()", "32");

		// Just Because =)
		System.out.println(formatXml(response.getText()));
	}
	
}
