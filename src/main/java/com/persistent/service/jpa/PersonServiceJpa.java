package com.persistent.service.jpa;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.persistent.entity.Person;
import com.persistent.service.PersonService;


@Service("personService")
public class PersonServiceJpa implements PersonService {
	
	@PersistenceContext
	private EntityManager entityManager;

	/*public EntityManager getEntityManager() {
		return entityManager;
	}*/
	
	@Transactional(readOnly = true)
	public Person getById(int id) {
		// TODO Auto-generated method stub
		return entityManager.find(Person.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Person> getAll() {
		Query query = entityManager.createNamedQuery("Person.findAll");
		List<Person> persons = null;
		persons = query.getResultList();
		return persons;
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean save(Person person) {
		
		entityManager.persist(person);
		entityManager.flush();
		
		return true;
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean update(Person person) {
		entityManager.merge(person);
		entityManager.flush();
		return true;
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean delete(Person person) {
		person = entityManager.getReference(Person.class, person.getId());
		if (person == null)
			return false;
		entityManager.remove(person);
		entityManager.flush();
		return true;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Person findPerson(Person person) {
		Person result = null;
		Query queryFindPerson = entityManager.createNamedQuery("Person.findPerson");
		queryFindPerson.setParameter("name", person.getName());
		queryFindPerson.setParameter("age", person.getAge());
		List<Person> persons = queryFindPerson.getResultList();
		if(persons.size() > 0) {
			result = persons.get(0);
		}
		return result;
	}
}

