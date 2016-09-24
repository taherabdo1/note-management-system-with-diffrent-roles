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

	@POST
	@Path("/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	public void signUp(User user1) {
		UserDao userDao = new UserDao();
		userDao.persist(user1);
	}
	
	@POST
	@Path("/signin")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signIn(Credentials userCredentials) {
		System.out.println("email:" +userCredentials.getEmail()+ " and password : " +userCredentials.getPassword());
		UserDao userDao = new UserDao();
		User loggedUser = userDao.signIn(userCredentials.getEmail(), userCredentials.getPassword());
		loggedUser.setNotes(new ArrayList());
		System.out.println("user logged in and the first name is: " + loggedUser.getFirstName());

		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonInString = mapper.writeValueAsString(loggedUser);
			return Response.status(Status.OK).entity(jsonInString).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(User user) {
		System.out.println("email:" +user.getEmail()+ " and password : " +user.getPassword());
		UserDao userDao = new UserDao();
		User loggedUser = userDao.update(user);
		System.out.println("user logged in and the first name is: " + loggedUser.getFirstName());
	}
	
	
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public void delete(User user) {
		System.out.println("email:" +user.getEmail()+ " and password : " +user.getPassword());
		UserDao userDao = new UserDao();
		userDao.deleteUser(user);
		System.out.println("deleted");
	}

}