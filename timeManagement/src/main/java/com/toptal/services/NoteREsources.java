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

import model.Note;
import model.User;

import org.codehaus.jackson.map.ObjectMapper;

import utils.Credentials;
import utils.NoteWithAtoken;
import utils.Secured;

import com.toptal.dao.*;
import com.toptal.filters.AuthenticationFilter;

@Path("/note")
public class NoteREsources {

	// to be upodated
	@POST
	@Path("/add")
//	@Consumes(MediaType.APPLICATION_JSON)
	public String add(String noteWithAtokenJsonString) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			NoteWithAtoken noteWithAtoken = mapper.readValue(
					noteWithAtokenJsonString, NoteWithAtoken.class);
			Note note = noteWithAtoken.getNote();
			// use userToken to get the user data
			User user = AuthenticationFilter.tokens.get(noteWithAtoken.getToken());

			System.out.println("note desc from create resource:"
					+ note.getDescription());

			UserDao userDao = new UserDao();
			NoteDao noteDao = new NoteDao();
//			// use the token to get User
//			user = userDao.findByEmail("abdo@gmail.com");
			note.setUser(user);
			noteDao.persist(note);
			return "{\"added\" : \"true\"}";
		} catch (Exception e) {
			return "{\"added\" : \"false\"}";
		}
	}

	@POST
	@Path("/delete")
	// @Consumes(MediaType.APPLICATION_JSON)
	public String delete(String noteId) {
		try {
			System.out.println("noteId: " + noteId);
			NoteDao noteDao = new NoteDao();
			Note note = new Note();
			note.setId(Integer.parseInt(noteId));
			noteDao.delete(note);
			System.out.println("deleted");
			return "{\"deleted\" : \"true\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"deleted\" : \"false\"}";
		}
	}

	@POST
	@Path("/update")
	// @Consumes(MediaType.APPLICATION_JSON)
	public String update(String noteJsonString) {
		// System.out.println("email:" +user.getEmail()+ " and password : "
		// +user.getPassword());
		try {
			ObjectMapper mapper = new ObjectMapper();
			Note note = mapper.readValue(noteJsonString, Note.class);
			System.out.println("note desc from update resource:"
					+ note.getDescription());

			NoteDao noteDao = new NoteDao();
			noteDao.update(note);
			System.out.println("updated");
			return "{\"updated\" : \"true\"}";

		} catch (Exception e) {
			return "{\"updated\" : \"false\"}";
		}
	}

	@POST
	@Path("/getAllOfUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllOfUser(String token) {
		NoteDao noteDao = new NoteDao();
		try {
			System.out.println("token from getAllOfUser:" + token);
			// temp to be updated from the token map
			User user = AuthenticationFilter.tokens.get(token);
			System.out.println("user email from getAllOfUser: "
					+ user.getEmail());
			// user.setId(token);
			List<Note> notes = new ArrayList<Note>(noteDao.getAllOfUser(user));
			for (Note note : notes) {
				note.setUser(null);
			}
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(notes);

			System.out.println(notes.get(0).getDescription());
			return Response.status(Status.OK).entity(jsonInString).build();
		} catch (Exception e) {
			return Response.status(Status.UNAUTHORIZED).entity("").build();
		}

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
		// System.out.println(note.getDescription());
		// System.out.println(user.getEmail());
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
