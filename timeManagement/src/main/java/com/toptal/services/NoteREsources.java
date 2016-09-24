package com.toptal.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;

import utils.Secured;
import model.Note;
import model.User;

import com.toptal.dao.*;

@Path("/note")
public class NoteREsources {

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public void add(Note note) {
		UserDao userDao = new UserDao();
		NoteDao noteDao = new NoteDao();
		User user =userDao.findByEmail("abdo@gmail.com");
		note.setUser(user);
		noteDao.persist(note);
	}
	
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public void delete(Note note) {
//		System.out.println("email:" +user.getEmail()+ " and password : " +user.getPassword());
		NoteDao noteDao = new NoteDao();
		noteDao.delete(note);
		System.out.println("deleted");
	}
	
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(Note note) {
//		System.out.println("email:" +user.getEmail()+ " and password : " +user.getPassword());
		NoteDao noteDao = new NoteDao();
		noteDao.update(note);
		System.out.println("deleted");
	}
	
	
	@POST
	@Path("/getAllOfUser")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllOfUser(User user) {
		NoteDao noteDao = new NoteDao();
		// userDao.getAllUsers();
		BaseResponse response = new BaseResponse();

		List<Note> notes = new ArrayList<Note>(noteDao.getAllOfUser(user));

		 System.out.println(notes.get(0).getDescription());
		return "test";
	}
	
	@GET
	@Path("/getAll")
	public String getAll() {
		NoteDao noteDao = new NoteDao();
		// userDao.getAllUsers();
		BaseResponse response = new BaseResponse();

		List<Note> notes = new ArrayList<Note>(noteDao.getAll());

		 System.out.println(notes.get(0).getDescription());
		return "test";
	}
	
    @Secured
	@POST
	@Path("/getNote")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNoteById(Note note) {
		NoteDao noteDao = new NoteDao();
		UserDao userDao = new UserDao();
		note = noteDao.findByID(note.getId());
		User user = userDao.findByNoteId(note.getId());
		user.setNotes(new ArrayList<Note>());
		note.setUser(user);
//		 System.out.println(note.getDescription());
//		 System.out.println(user.getEmail());
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonInString = mapper.writeValueAsString(note);
			return Response.status(Status.OK).entity(jsonInString).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
}
