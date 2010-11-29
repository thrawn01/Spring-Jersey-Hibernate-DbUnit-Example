package com.persistent.entity;

import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "persons")
public class PersonList {

    private Collection<Person> persons;
    
    public PersonList() {}
    
    public PersonList(Collection<Person> persons) {
        this.persons = persons;
    }

    public void setPersons(Collection<Person> persons) {
        this.persons = persons;
    }

    @XmlElement(name = "person")
    public Collection<Person> getPersons() {
        return this.persons;
    }
}
