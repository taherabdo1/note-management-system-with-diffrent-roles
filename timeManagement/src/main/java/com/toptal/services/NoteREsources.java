package com.toptal.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

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

import utils.AuthenticationServiceHelper;
import utils.DateFilterRequestWrapper;
import utils.DateFilterResponseWrapper;
import utils.NoteWithAtokenWrapper;
import utils.Util;

import com.toptal.dao.*;

@Path("/note")
public class NoteREsources {

	@POST
	@Path("/add")
	// @Consumes(MediaType.APPLICATION_JSON)
	public String add(String noteWithAtokenJsonString) {
		try {
			System.out.println("from create note resource:" + noteWithAtokenJsonString);

			ObjectMapper mapper = new ObjectMapper();
			NoteWithAtokenWrapper noteWithAtoken = mapper.readValue(
					noteWithAtokenJsonString, NoteWithAtokenWrapper.class);
			Note note = noteWithAtoken.getNote();
			System.out.println("note desc from create resource:"
					+ note.getDescription());

			UserDao userDao = new UserDao();
			User user;
			// if the token is email use the user whose email is this
			System.out.println(Util.isEmail(noteWithAtoken.getToken())
					+ " sent to add note to and it is email");
			if (Util.isEmail(noteWithAtoken.getToken())) {
				user = userDao.findByEmail(noteWithAtoken.getToken());
				System.out.println("user email to put the note for is: "
						+ user.getEmail());
			} else { // the token is not email then use the token to get the
						// user from tokens map
				user = AuthenticationServiceHelper.tokens.get(noteWithAtoken
						.getToken());
				System.out
						.println("user email from a token to put the note for is: "
								+ user.getEmail());

			}
			NoteDao noteDao = new NoteDao();
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
			NoteDao noteDao = new NoteDao();
			Note note = new Note();
			note.setId(Integer.parseInt(noteId));
			noteDao.delete(note);
			return "{\"deleted\" : \"true\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"deleted\" : \"false\"}";
		}
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public String update(String noteJsonString) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Note note = mapper.readValue(noteJsonString, Note.class);
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
			System.out.println("token from getAllOfUser in Notes:" + token);
			User user = AuthenticationServiceHelper.tokens.get(token);
			List<Note> notes = new ArrayList<Note>(noteDao.getAllOfUser(user));
			if (notes.size() > 0) {
				for (Note note : notes) {
					note.setUser(null);
				}
				ObjectMapper mapper = new ObjectMapper();
				String jsonInString = mapper.writeValueAsString(notes);
				return Response.status(Status.OK).entity(jsonInString).build();
			} else {
				return Response.status(Status.OK).entity("{}").build();
			}
		} catch (Exception e) {
			return Response.status(Status.UNAUTHORIZED).entity("").build();
		}

	}

	@POST
	@Path("/getAllNotesOfAnotherUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllNotesOfAnotherUser(String email) {
		NoteDao noteDao = new NoteDao();
		try {
			User user = new User();
			user.setEmail(email);
			List<Note> notes = new ArrayList<Note>(noteDao.getAllOfUser(user));
			if (notes.size() > 0) {
				for (Note note : notes) {
					note.setUser(null);
				}
				ObjectMapper mapper = new ObjectMapper();
				String jsonInString = mapper.writeValueAsString(notes);
				System.out.println(notes.get(0).getDescription());
				return Response.status(Status.OK).entity(jsonInString).build();
			} else {
				return Response.status(Status.OK).entity("{}").build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.UNAUTHORIZED).entity("").build();
		}
	}

	// unused
	@Deprecated
	@GET
	@Path("/getAll")
	public String getAll() {
		NoteDao noteDao = new NoteDao();

		List<Note> notes = new ArrayList<Note>(noteDao.getAll());

		System.out.println(notes.get(0).getDescription());
		return "test";
	}

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
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonInString = mapper.writeValueAsString(note);
			return Response.status(Status.OK).entity(jsonInString).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("/filterByStartAndEndDateForUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response filterByStartAndEndDateForUser(String filterData) {
		try {

			List<DateFilterResponseWrapper> dateFilterResponseWrappers = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper();
			DateFilterRequestWrapper dateFilterRequestWrapper = mapper
					.readValue(filterData, DateFilterRequestWrapper.class);
			NoteDao noteDao = new NoteDao();
			// get User from token map
			User user = AuthenticationServiceHelper.tokens
					.get(dateFilterRequestWrapper.getUserToken());

			List<Object[]> notesGrouped = noteDao
					.getFilteredNotesByDateForUser(
							dateFilterRequestWrapper.getStartDate(),
							dateFilterRequestWrapper.getEndDate(), user);

			// go through the data returned to construct a list of
			// DateFilterResponseWrapper
			DateFilterResponseWrapper dateFilterResponseWrapper;
			StringTokenizer tokenizer;
			for (Object[] row : notesGrouped) {
				dateFilterResponseWrapper = new DateFilterResponseWrapper();
				dateFilterResponseWrapper.setDate((Date) row[0]);
				dateFilterResponseWrapper.setTotalPeriod(((Double) row[1])
						.floatValue());
				dateFilterResponseWrapper.setNotes(new ArrayList<String>());
				tokenizer = new StringTokenizer((String) row[2], ",");
				while (tokenizer.hasMoreTokens()) {
					dateFilterResponseWrapper.getNotes().add(
							tokenizer.nextToken());
				}
				dateFilterResponseWrappers.add(dateFilterResponseWrapper);
			}
			String responseJson = mapper
					.writeValueAsString(dateFilterResponseWrappers);
			return Response.status(Status.OK).entity(responseJson).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.UNAUTHORIZED).entity("").build();
		}
	}
}
