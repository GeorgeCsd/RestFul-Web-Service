package org.example;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Path("/book")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//@Stateless
public class BookRestService implements BookService{

    private static Map<String,Book> books = new HashMap<String,Book>();

    @PersistenceContext(unitName = "RestController")
    private EntityManager em;

    @Context
    private UriInfo uriInfo;
    @Override
    @POST
    @Path("/add")
    public Response addBook(Book b) {

        System.out.println(b);
        //if(b==null){
        //    throw new BadRequestException();
        //}
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("RestController");
        EntityManager em = emf.createEntityManager();
        // 3-Persists the book to the database
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(b);
        tx.commit();
        URI bookUri=uriInfo.getAbsolutePathBuilder().path(b.getId()).build();
        //return javax.ws.rs.core.Response.created(bookUri).build();
        Response response=new Response();
        response.setStatus(true);
        response.setMessage("Book created successfully \n");
        return response;
    }


    @GET
    @Path("/{id}/delete")
    public Response deleteBook(@PathParam("id") String id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("RestController");
        EntityManager em = emf.createEntityManager();
        Book book=em.find(Book.class,id);
        Response response = new Response();;
        response.setStatus(true);
        response.setMessage("Book deleted successfully\n");
        return response;
    }


    @GET
    @Path("/{id}/get")
    public Book getBook(@PathParam("id") String id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("RestController");
        EntityManager em = emf.createEntityManager();
        Book book=em.find(Book.class,id);

        return book;
    }


    @GET
    @Path("/getAll")
    public Book[] getAllBooks() {
        Set<String> ids = books.keySet();
        Book[] b = new Book[ids.size()];
        int i=0;
        for(String id : ids){
            b[i] = books.get(id);
            i++;
        }
        return b;
    }
}
