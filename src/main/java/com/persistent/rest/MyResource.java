package com.persistent.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.persistent.entity.Person;



// The Java class will be hosted at the URI path "/myresource"
@Path("/myresource")
@Component
public class MyResource {
	
	@Autowired
	SessionFactory sessionFactory;
	
	// The Java method will process HTTP GET requests
	@GET
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@Produces("text/plain")
	public String getIt() {
        //final Session session = sessionFactory.getCurrentSession();
        //final Criteria criteria = session.createCriteria(Person.class);
		//StringBuffer buffer = new StringBuffer();
		//for (Person person2 : persons) {
		//		buffer.append(person2.getName()).append(":").append(person2.getAge())
		//			.append("\n");
		//}
		//return criteria.list();
		return "Hi there: ";
	}
}

