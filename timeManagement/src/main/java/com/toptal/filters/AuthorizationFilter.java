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

import utils.AuthenticationServiceHelper;
import model.User;

public class AuthorizationFilter implements ContainerRequestFilter {

	@Context
	HttpServletRequest request;

	public static final String[] userPages = { "/note/add", "/note/delete",
			"/note/update", "/note/getAllOfUser", "/note/getNote", "/note/filterByStartAndEndDateForUser",
			"/user/signup", "/user/signin"};

	public static final String[] managerPages = { "/user/delete",
			"/user/update", "/user/update", "/user/getAllUsers" };

	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {

		System.out.println("inside the authrization filter");

		User user = AuthenticationServiceHelper.tokens.get(requestContext
				.getHeaderString("Authorization"));

		// if signin or signup don't filter
		if (request.getPathInfo().equals("/user/signin")
				|| request.getPathInfo().equals("/user/signup")) {
			return;
		}
		try {
			// if the user role is ADMIN then he has all the previllages
			if (user.getRole().equalsIgnoreCase("admin")) {
				System.out.println("user role from authorization filter: "
						+ user.getRole());
				return;
			}
			// if the user is manager and he has previllage to go for this path
			else if (user.getRole().equalsIgnoreCase("manager")) {
				if (Arrays.asList(managerPages).contains(request.getPathInfo())
						|| Arrays.asList(userPages).contains(
								request.getPathInfo()))
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
			// }
			// otherwise return unauthorized
			else {
				requestContext.abortWith(Response.status(
						Response.Status.UNAUTHORIZED).build());
			}
		} // this means the user is not logged in and request pages other than
		// signin and signup
		catch (NullPointerException ex) {
			requestContext.abortWith(Response.status(
					Response.Status.UNAUTHORIZED).build());
		}

		System.out.println("path: " + request.getPathInfo());
	}

}
