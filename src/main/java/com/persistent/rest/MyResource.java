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
import org.springframework.transaction.annotation.Transactional;
import com.persistent.entity.Person;

@Path("/myresource")
@Component
public class MyResource {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@GET
    @Transactional
	@Produces("text/plain")
    @SuppressWarnings("unchecked")
	public String getIt() {
		// Query
        final Session session = sessionFactory.getCurrentSession();
        final Criteria criteria = session.createCriteria(Person.class);
        
        // Construct Result
		StringBuffer buffer = new StringBuffer();
		for(Person person2 : (List<Person>)criteria.list()) {
			buffer.append(person2.getName()).append(":").append(person2.getAge()).append("\n");
		}
		// Return Result
		return buffer.toString();
	}
}

