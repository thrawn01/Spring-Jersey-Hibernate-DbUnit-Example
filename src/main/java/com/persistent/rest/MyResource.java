package com.persistent.rest;

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
import com.persistent.entity.PersonList;

@Path("/persons")
@Component
public class MyResource {

	@Autowired
	SessionFactory sessionFactory;

	// TODO: @Produces({"application/json","application/xml"})

	@GET
	@Transactional
	@Produces("application/xml")
	@SuppressWarnings("unchecked")
	public PersonList getIt() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(Person.class);
		return new PersonList(criteria.list());
	}
}
