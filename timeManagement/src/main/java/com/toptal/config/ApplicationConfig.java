package com.toptal.config;

import org.glassfish.jersey.server.ResourceConfig;

import com.toptal.filters.AuthenticationFilter;

public class ApplicationConfig extends ResourceConfig{
	 public ApplicationConfig()
	    {
	        packages("com.toptal.services");
	        register(AuthenticationFilter.class);
	    }
}
