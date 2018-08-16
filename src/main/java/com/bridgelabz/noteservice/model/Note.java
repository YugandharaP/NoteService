package com.bridgelabz.noteservice.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author yuga
 * @since 17-07-2018
 *        <p>
 *        <b>To provide setter and getter methods to deal with note details</b>
 *        </p>
 */
@Document(indexName = "noteindex", type = "note")
public class Note {

	@Id
	private String id;
	private String title;
	private String discription;
	private List<MetaData> metadata;
	private String colorCode;
	private Date reminder;
	private Date createdDate;
	private Date lastModifiedDate;
	private boolean trashStatus;
	private boolean pinNote;
	private boolean archiveNOte;
	private List<Label> listOfLabels;

	private String userId;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDiscription() {
		return discription;
	}

	public List<MetaData> getMetadata() {
		return metadata;
	}

	public String getColorCode() {
		return colorCode;
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

	public List<Label> getListOfLabels() {
		return listOfLabels;
	}

	public String getUserId() {
		return userId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public void setMetadata(List<MetaData> metadata) {
		this.metadata = metadata;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
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

	public void setListOfLabels(List<Label> listOfLabels) {
		this.listOfLabels = listOfLabels;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}