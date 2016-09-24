package com.toptal.services;

//import org.json.simple.JSONObject;

//import org.codehaus.jettison.json.JSONException;
//import org.codehaus.jettison.json.JSONObject;

public class BaseResponse {

	private int status;
	private String data;

//	@Override
//	public String toString() {
////		JSONObject object = new JSONObject();
////		object.put("status", status);
////		object.put("data", data);
////		return object.toString();
//	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
