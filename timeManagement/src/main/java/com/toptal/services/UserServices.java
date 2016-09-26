package com.toptal.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import model.*;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import utils.AuthenticationServiceHelper;
import utils.Credentials;





import utils.NoteWithAtoken;







//import com.google.gson.Gson;
//import com.google.gson.Gson;
import com.toptal.dao.UserDao;
import com.toptal.filters.AuthenticationFilter;

@Path("/user")
public class UserServices {

	
	@GET
	@Path("/getAllUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		UserDao userDao = new UserDao();
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<User> users = new ArrayList<User>(userDao.getAllUsers());
			String jsonInString = mapper.writeValueAsString(users);
			System.out.println("user email at userServiceis: "+users.get(0).getEmail());
			return Response.status(Status.OK).entity(jsonInString).build();
		} catch (Exception e) {
			return Response.status(Status.UNAUTHORIZED).entity("").build();
		}

	}

	//checked
	@POST
	@Path("/signup")
//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String signUp(String userJsonString) {
		UserDao userDao = new UserDao();
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			User user1 = mapper.readValue(
					userJsonString, User.class);

			System.out.println("note desc from create resource:"
					+ user1.getFirstName());

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
	public Response signIn(String userCredentials) {
		UserDao userDao = new UserDao();
		try {
			ObjectMapper mapper = new ObjectMapper();
			Credentials userCredentialsObject = mapper.readValue(userCredentials, Credentials.class);
			System.out.println("email:" + userCredentialsObject.getEmail()
					+ " and password : " + userCredentialsObject.getPassword());

			if(AuthenticationServiceHelper.loggedUsers.contains(userCredentialsObject)){
				System.out.println("this user already logged in");
				return Response.status(Status.CONFLICT).entity("{\"token\":"+"\"already signed in\"}").build();
			}
			User loggedUser = userDao.signIn(userCredentialsObject.getEmail(),
					userCredentialsObject.getPassword());
			loggedUser.setNotes(new ArrayList());
			//issue a token and relate it to the user in the map inside Authentication filter
			String token = AuthenticationServiceHelper.getToken();
			while (AuthenticationServiceHelper.tokens.get(token) != null) {
				token=AuthenticationServiceHelper.getToken();
			}
			//the token to the map and the user to the loggedUsers List
			AuthenticationServiceHelper.tokens.put(token , loggedUser);
			AuthenticationServiceHelper.loggedUsers.add(loggedUser);
		System.out.println("user logged in and the first name is: "
				+ loggedUser.getFirstName());


//			String jsonInString = mapper.writeValueAsString(loggedUser);
			String returnString = "{\"token\" :"+" \""+token+"\"}";
			return Response.status(Status.OK).entity(returnString).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return Response.status(Status.OK).entity("{\"token\":"+"\"null\"}").build();
		}
	}

	@POST
	@Path("/update")
//	@Consumes(MediaType.APPLICATION_JSON)
	public String update(String userJsonString) {
//		System.out.println("email:" + user.getEmail() + " and password : "
//				+ user.getPassword());
	try{	ObjectMapper mapper = new ObjectMapper();
		User user= mapper.readValue(userJsonString, User.class);
		System.out.println("user email from update resource:"
				+ user.getEmail());

		UserDao userDao = new UserDao();
		User loggedUser = userDao.update(user);
		System.out.println("user logged in and the first name is: "
				+ loggedUser.getFirstName());
		return "{\"updated\" : \"true\"}";

	}catch(Exception e){
		return "{\"updated\" : \"false\"}";

	}
	}

	@POST
	@Path("/delete")
//	@Consumes(MediaType.APPLICATION_JSON)
	public String delete(String userEmail) {
	try{
		System.out.println("email:" + userEmail);
		UserDao userDao = new UserDao();
		User user = new User();
		user.setEmail(userEmail);
		userDao.deleteUser(user);
		System.out.println("deleted");
		return "{\"deleted\" : \"true\"}";
	} catch (Exception e) {
		e.printStackTrace();
		return "{\"deleted\" : \"false\"}";
	}
	}

}