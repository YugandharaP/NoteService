package com.bridgelabz.noteservice.model;

import java.io.Serializable;
import java.util.List;

/**
 *@since 19/07/2018
 *<p><b>Label DTO can interact between view and controller to receive label data.</b></p>
 *@author yuga
 */
@SuppressWarnings("serial")
public class LabelDTO implements Serializable {
	private List<String> listOfLabels;
	private String noteId;

	public List<String> getListOfLabels() {
		return listOfLabels;
	}

	public void setListOfLabels(List<String> listOfLabels) {
		this.listOfLabels = listOfLabels;
	}

	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}
}
