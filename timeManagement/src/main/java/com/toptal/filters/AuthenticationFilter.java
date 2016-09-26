package com.toptal.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

//import javax.annotation.Priority;







import model.*;

//@Secured
//@Provider
//@Priority(Priorities.AUTHENTICATION)

public class AuthenticationFilter implements ContainerRequestFilter , ContainerResponseFilter{

	public static Map<String, User> tokens = new HashMap<>();
	@Context
	HttpServletRequest request;

	public AuthenticationFilter() {
		
	}

	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
	/////////////
		System.out.println("inside the constructor");
		if(tokens.get(requestContext.getHeaderString("Authorization")) == null && !request.getPathInfo().contains("signin")&& !request.getPathInfo().contains("signup")){
			requestContext.abortWith(Response.status(
					Response.Status.UNAUTHORIZED).build());			
		}
		System.out.println("request headers: "+requestContext.getHeaders());
		System.out.println("token from headers in filter: "+requestContext.getHeaderString("Authorization"));
		
		System.out.println(tokens.size());
		// Get the HTTP Authorization header from the request
		// String authorizationHeader = requestContext
		// .getHeaderString(HttpHeaders.AUTHORIZATION);

		// Check if the HTTP Authorization header is present and formatted
		// correctly
		// if (authorizationHeader == null
		// || !authorizationHeader.startsWith("Bearer ")) {
		// throw new NotAuthorizedException(
		// "Authorization header must be provided");
		// }

		// Extract the token from the HTTP Authorization header
		// String token =
		// authorizationHeader.substring("Bearer".length()).trim();

//		try {
//			requestContext.setMethod("GET");// setProperty("id","5");
//			// Validate the token
//			// validateToken(token);
//			Response.status(Response.Status.BAD_REQUEST).build();
//
//		} catch (Exception e) {
//			requestContext.abortWith(Response.status(
//					Response.Status.UNAUTHORIZED).build());
//		}
	}

	private void validateToken(String token) throws Exception {
		// Check if it was issued by the server and if it's not expired
		// Throw an Exception if the token is invalid
	}

	//rsponse filter
	 @Override
	 public void filter(ContainerRequestContext requestContext,
	 ContainerResponseContext responseContext) throws IOException
	 {
	 StringBuilder sb = new StringBuilder();
	 sb.append("Header: ").append(responseContext.getHeaders());
	 sb.append(" - Entity: ").append(responseContext.getEntity());
	 System.out.println("HTTP RESPONSE : " + sb.toString());
	 }
}
