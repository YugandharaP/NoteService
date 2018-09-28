package com.bridgelabz.noteservice;

import java.net.URL;
import java.util.Date;
import java.util.List;

import com.bridgelabz.noteservice.model.Label;
import com.bridgelabz.noteservice.model.MetaData;

public class NoteModel {

	private String type;
	private String id;
	private String title;
	private String discription;
	private Date reminder;
	private boolean trashStatus;
	private boolean pinNote;
	private boolean archiveNOte;
	private String userId;
	private List<MetaData> metadata;
	private List<URL>imageList;
	private List<Label> labelNameList;

	public List<Label> getLabelNameList() {
		return labelNameList;
	}

	public void setLabelNameList(List<Label> labelNameList) {
		this.labelNameList = labelNameList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<MetaData> getMetadata() {
		return metadata;
	}

	public List<URL> getImageList() {
		return imageList;
	}

	

	public void setMetadata(List<MetaData> metadata) {
		this.metadata = metadata;
	}

	public void setImageList(List<URL> imageList) {
		this.imageList = imageList;
	}

	
	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getDiscription() {
		return discription;
	}

	public Date getReminder() {
		return reminder;
	}

	public boolean isTrashStatus() {
		return trashStatus;
	}

	public boolean isPinNote() {
		return pinNote;
	}

	public boolean isArchiveNOte() {
		return archiveNOte;
	}

	public String getUserId() {
		return userId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public void setReminder(Date reminder) {
		this.reminder = reminder;
	}

	public void setTrashStatus(boolean trashStatus) {
		this.trashStatus = trashStatus;
	}

	public void setPinNote(boolean pinNote) {
		this.pinNote = pinNote;
	}

	public void setArchiveNOte(boolean archiveNOte) {
		this.archiveNOte = archiveNOte;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
