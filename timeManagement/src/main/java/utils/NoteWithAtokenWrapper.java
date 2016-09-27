package utils;

import model.Note;


//this is a wrapper class to hold the new note and the userToken
public class NoteWithAtokenWrapper {

	Note note;
	String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}
	
	

}
