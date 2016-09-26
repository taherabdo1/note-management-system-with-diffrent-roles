package com.toptal.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import model.User;

public class AuthorizationFilter implements ContainerRequestFilter {

	@Context
	HttpServletRequest request;

	public static final String[] userPages = { "/note/add", "/note/delete",
			"/note/update", "/note/getAllOfUser", "/note/getNote",
			"/user/signup", "/user/signin" };

	public static final String[] managerPages = { "/user/delete",
			"/user/update", "/user/update", "/user/getAllUsers" };

	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {

		System.out.println("inside the authrization filter");

		User user = AuthenticationFilter.tokens.get(requestContext
				.getHeaderString("Authorization"));
		System.out.println("user token from authorization filter: "+requestContext.getHeaderString("Authorization"));
		System.out.println(" and the path is: "+request.getPathInfo());
		// if signin or signup don't filter
		if (request.getPathInfo().equals("/user/signin")
				|| request.getPathInfo().equals("/user/signup")) {
			return;
		}
		// if the user role is ADMIN then he has all the previllages
		else if (user.getRole().equalsIgnoreCase("admin")) {
			System.out.println("user role from authorization filter: "
					+ user.getRole());
			return;
		}
		// if the user is manager and he has previllage to go for this path
		else if (user.getRole().equalsIgnoreCase("manager")
				|| Arrays.asList(userPages).contains(request.getPathInfo())) {
			if (Arrays.asList(managerPages).contains(request.getPathInfo()))
				System.out.println("user role from authorization filter: "
						+ user.getRole());
			return;
		}
		// if the user is user and he has previllage to go for this path
		else if (user.getRole().equalsIgnoreCase("user")
				&& Arrays.asList(userPages).contains(request.getPathInfo())) {
			System.out.println("user role from authorization filter: "
					+ user.getRole());
			return;
		}
		// otherwise return unauthorized
		else {
			System.out.println("user role from authorization filter: "
					+ user.getRole());
			requestContext.abortWith(Response.status(
					Response.Status.UNAUTHORIZED).build());
		}

		System.out.println("path: " + request.getPathInfo());
	}

}
