package com.persistent.rest;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.persistent.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Path("persons")
public class MyResource {

   private static final Abdera abdera = new Abdera();

   @Autowired
   SessionFactory sessionFactory;

   @GET
   @Produces("application/atom+xml")
   @Transactional
   public Feed getFeed(@Context UriInfo uriInfo) throws JAXBException {
       Feed feed = abdera.getFactory().newFeed();
       feed.setTitle("Atom Test Feed");
       feed.setId("tag:example.org,2007:/feed");
       feed.setSubtitle("Feed subtitle");
       feed.setUpdated(new Date());
       feed.addAuthor(".......");

       URI feedLink = uriInfo.getRequestUri();
       feed.addLink(feedLink.toString(),"self");

       final Session session = sessionFactory.getCurrentSession();
       final Criteria criteria = session.createCriteria(Person.class);
       List<?> persons = criteria.list();

       for (Object obj : persons) {
    	   Person person = (Person) obj;

    	   Entry entry = feed.addEntry();
           entry.setId("tag:example.org,2007:/feed/entries/1");
           entry.setTitle("Entry Title");
           entry.setUpdated(new Date());
           entry.setPublished(new Date());
           entry.addLink(uriInfo.getRequestUri().toString(),person.getName()); // alternate

           JAXBContext ctx = JAXBContext.newInstance(Person.class);
           StringWriter writer = new StringWriter();
    	   ctx.createMarshaller().marshal(person, writer);
    	   entry.setContent(writer.toString(), "application/xml");
       }
       return feed;
   }

   @PUT
   @Consumes("application/atom+xml")
   public void putFeed(Feed feed) throws Exception
   {
      String content = feed.getEntries().get(0).getContent();
      JAXBContext ctx = JAXBContext.newInstance(Person.class);
      Person person = (Person) ctx.createUnmarshaller().unmarshal(new StringReader(content));
      // You can test the person here...
   }

}
