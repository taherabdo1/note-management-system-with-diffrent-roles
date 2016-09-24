package com.toptal.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import utils.Credentials;

//import com.google.gson.Gson;
//import com.google.gson.Gson;
import com.toptal.dao.UserDao;

import model.*;

@Path("/user")
public class UserServices {

	@GET
	@Path("/getAllUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllUsers() {
		UserDao userDao = new UserDao();
		// userDao.getAllUsers();
		BaseResponse response = new BaseResponse();

		List<User> users = new ArrayList<User>(userDao.getAllUsers());
		// if (users != null) {
		// response.setStatus(2);
		// response.setData(new Gson().toJson(users));
		// } else {
		// response.setStatus(3);
		// }
		// return response.toString();
		// System.out.println(users.get(0).getEmail());
		return "test";
	}

	//checked
	@POST
	@Path("/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String signUp(User user1) {
		UserDao userDao = new UserDao();

		try {
			userDao.persist(user1);
			return "{\"response\":\"DONE\"}";// Response.status(Status.OK).build();
		} catch (Exception e) {
			// e.printStackTrace();
			return "{\"response\":\"false\"}"; // Response.status(Status.BAD_REQUEST).build();
		}
	}

	// check the uniqueness of the new email
	@POST
	@Path("/checkMail")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response isUserEmailFoundBefore(User user1) {
		UserDao userDao = new UserDao();
		try {
			userDao.findByEmail(user1.getEmail());
			return Response.status(Status.OK).entity("true").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.CONFLICT).entity("false").build();
		}
	}

	@POST
	@Path("/signin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signIn(Credentials userCredentials) {
		System.out.println("email:" + userCredentials.getEmail()
				+ " and password : " + userCredentials.getPassword());
		UserDao userDao = new UserDao();
		try {
			User loggedUser = userDao.signIn(userCredentials.getEmail(),
				userCredentials.getPassword());
		loggedUser.setNotes(new ArrayList());
		System.out.println("user logged in and the first name is: "
				+ loggedUser.getFirstName());

		ObjectMapper mapper = new ObjectMapper();

//			String jsonInString = mapper.writeValueAsString(loggedUser);
			String returnString = "{\"token\" :"+" \"jfsgskjksjfgksjfsghfjkghgsg\"}";
			return Response.status(Status.OK).entity(returnString).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return Response.status(Status.OK).entity("{\"token\":"+"\"null\"}").build();
		}
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(User user) {
		System.out.println("email:" + user.getEmail() + " and password : "
				+ user.getPassword());
		UserDao userDao = new UserDao();
		User loggedUser = userDao.update(user);
		System.out.println("user logged in and the first name is: "
				+ loggedUser.getFirstName());
	}

	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public void delete(User user) {
		System.out.println("email:" + user.getEmail() + " and password : "
				+ user.getPassword());
		UserDao userDao = new UserDao();
		userDao.deleteUser(user);
		System.out.println("deleted");
	}

}