package rest.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import domain.Person;

@Path("people")
@Stateless
public class PersonResource{
	
	@PersistenceContext
	EntityManager em;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPerson(Person person){
		
		em.persist(person);
		return Response.ok(person.getId()).build();
	}
	
	@GET
	@Path("page={page}")
	@Produces(MediaType.APPLICATION_JSON)
	public List <Person> getPersons(@PathParam("page") int page){
		
		return em.createNamedQuery("person.between", Person.class)
				.setFirstResult((page - 1) * 3)
				.setMaxResults(3)
				.getResultList();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePerson(@PathParam("id") int id, Person person){
		
		Person result = em.createNamedQuery("person.id", Person.class)
				.setParameter("personId", id)
				.getSingleResult();
		
		if(result == null){
			
			return Response.status(404).build();
		}
		else{
			
			result.setFirstName(person.getFirstName());
			result.setLastName(person.getLastName());
			result.setGender(person.getGender());
			result.setBirthday(person.getBirthday());
			result.setEmail(person.getEmail());
			result.setAge(person.getAge());
			
			em.persist(result);
		}
		
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response deletePerson(@PathParam("id") int id){
		
		Person result = em.createNamedQuery("person.id", Person.class)
				.setParameter("personId", id)
				.getSingleResult();
		
		if(result == null){
			
			return Response.status(404).build();
		}
		
		em.remove(result);
		return Response.ok().build();
	}
}