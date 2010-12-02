
package com.persistent.entity;

import javax.xml.bind.annotation.XmlRegistry;
import com.persistent.entity.PersonList;
import com.persistent.entity.Person;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() { } 

    public PersonList createPersons() {
        return new PersonList();
    }

    public Person createPerson() {
        return new Person();
    }

}
