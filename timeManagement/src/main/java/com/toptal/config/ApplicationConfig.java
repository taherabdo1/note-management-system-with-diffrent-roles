package com.toptal.config;

import org.glassfish.jersey.server.ResourceConfig;

import com.toptal.filters.AuthenticationFilter;
import com.toptal.filters.AuthorizationFilter;

public class ApplicationConfig extends ResourceConfig{
	 public ApplicationConfig()
	    {
	        packages("com.toptal.services");
	        register(AuthenticationFilter.class);
	        register(AuthorizationFilter.class);
	    }
}
