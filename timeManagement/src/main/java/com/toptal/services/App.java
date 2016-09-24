package com.toptal.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.toptal.dao.*;
import model.*;

//import com.toptal.entities.User;
//import com.toptal.entities.Role;

/**
 * Hello world!
 *
 */


@Path("/testCase")
public class App 
{
	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTrackInJSON() {

		UserDao dao = new UserDao();
		User userTest = new User("abdo@gmail.com" , "abdo" , "ahmed" , "123" , "admin");
		dao.persist(userTest);
		String track = dao.findByID(1).getEmail();

		return track;

	}
}
