package com.bridgelabz.noteservice.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.bridgelabz.noteservice.model.MetaData;
import com.bridgelabz.noteservice.model.Note;
import com.bridgelabz.noteservice.model.NoteDTO;
import com.bridgelabz.noteservice.utilservice.exceptions.ToDoExceptions;


/**
 *@author yuga
* @since 17/07/2018
*<p><b>To connect the controller and mongoDb repository.
*service interface is the media between controller and repository 
*</b></p>
 *
 */
public interface INoteService {

	/**
	 * @param createnotedto
	 *  @param token 
	 * <p><b>To create a note in todo application</b></p>
	 * @return 
	 *removelabelfromnoteremovelabelfromnote
	 * @throws ToDoExceptions 
	 * @throws ParseException 
	 */
	String createNote(NoteDTO notedto, String userId) throws ToDoExceptions, ParseException;

	/**
	 * @param noteId @param token
	 * <p><b>To delete perticular  note from todo application</b></p>
	 *  
	 * @throws ToDoExceptions 
	 */
	String deleteNote(String noteId, String userId) throws ToDoExceptions;

	/**
	 * @param token 
	 * @param noteId
	 * @return Note
	 * <p><b>To read perticular  note from todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	Note readNote(String noteId, String userId) throws ToDoExceptions;
	
	
	/**
	 * @param token
	 * @return listOfNotes
	 * <p><b>To read all notes of perticular user id from todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	List<Note> readAllNotes(String userId) throws ToDoExceptions;

	/**
	 * @param token
	 * @param updatedto
	 * <p><b>To update note of perticular note id from todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	void updateNote(NoteDTO notedto, String userId) throws ToDoExceptions;

	/**
	 * @param noteId
	 * @param token
	 * <p><b>To delete note from trash</b></p>
	 * @throws ToDoExceptions 
	 */
	void deleteNoteFromTrash(String noteId, String userId) throws ToDoExceptions;

	/**
	 * @param noteId
	 * @param token
	 * <p><b>To restore note from trash</b></p>
	 * @param reminderDate 
	 * @throws ToDoExceptions 
	 */
	void restoreNoteFromTrash(String noteId, String userId) throws ToDoExceptions;

	/**
	 * @param token
	 * @param noteId
	 * <p><b>To pin note in todo application</b></p>
	 * @param value 
	 * @return 
	 * @throws ToDoExceptions 
	 */
	boolean pinNote(String userId, String noteId, boolean value) throws ToDoExceptions;
	

	/**
	 * @param tokenFromHeader
	 * @return list
	 * @throws ToDoExceptions 
	 */
	List<Note> readAllFromTrash(String userId) throws ToDoExceptions;
	
	/**
	 * @param token
	 * @param noteId
	 * <p><b>To archive note in todo application</b></p>
	 * @param condition 
	 * @return 
	 * @throws ToDoExceptions 
	 */
	boolean archieveNote(String userId, String noteId, boolean condition) throws ToDoExceptions;

	/**
	 * @param token
	 * @param noteId
	 * <p><b>To added reminder on note in todo application</b></p>
	 * @throws ToDoExceptions 
	 * @throws ParseException 
	 */
	void reminderNote(String userId, String noteId, String reminderDate) throws ToDoExceptions, ParseException;

	/**
	 * @param header
	 * @param noteId
	 * <p><b>To remove reminder from particular note in todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	void removeReminder(String userId, String noteId) throws ToDoExceptions;
	

	/**
	 * @param header
	 * @param labeldto
	 * <p><b>To create label in todo application</b></p>
	 * @return 
	 * @throws ToDoExceptions 
	 */
	void createLabels(String userId, String lableName) throws ToDoExceptions;



	/**
	 * @param userId
	 * @param labelName
	 * <p><b>To delete label from todo application of particular user</b></p>
	 * @throws ToDoExceptions 
	 */
	void deleteLabel(String userId, String labelName) throws ToDoExceptions;
	
	/**
	 * @param header
	 * @param noteId 
	 * <p><b>To added labels on note in todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	void addlabels(String userId, String noteId, List<String> labels) throws ToDoExceptions;

	/**
	 * @param userId
	 * @param currentLabelName
	 * @param newLabelName
	 *  <p><b>To edit the particular label on note  and label repository in todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	void editLabel(String userId, String currentLabelName, String newLabelName) throws ToDoExceptions;

	
	/**
	 * @param userId
	 *  <p><b>To return list of labels of the intended user</b></p>
	 * @return list of labels
	 * @throws ToDoExceptions 
	 */
	List<String> getAllLabels(String userId) throws ToDoExceptions;

	/**
	 * @param userId
	 *  <p><b>To return list of notes based on label name</b></p>
	 * @return list of notes
	 * @throws ToDoExceptions 
	 */
	List<Note> searchNotesByLabelName(String userId,String labelName) throws ToDoExceptions;

	/**
	 * @param userId
	 * @param noteId
	 * <p><b>remove note from archieve</b></p>
	 * @throws ToDoExceptions 
	 */
	void removeNoteFromArcheive(String userId, String noteId) throws ToDoExceptions;

	/**
	 * @param userId
	 * <p><b>list out all notes which is in archieve</b></p>
	 * @return list of notes is in archieve
	 * @throws ToDoExceptions 
	 */
	List<Note> getAllNotesFromArchive(String userId) throws ToDoExceptions;

	/**
	 * @param userId
	 * <p><b>remove all notes from trash</b></p>
	 * @throws ToDoExceptions 
	 */
	void emptyTrash(String userId) throws ToDoExceptions;

	/**
	 * @param userId
	 * @param noteId
	 * @param labelName
	 * <p><b>remove label from note</b></p>
	 * @throws ToDoExceptions 
	 */
	void removelabelfromnote(String userId, String noteId, String labelName) throws ToDoExceptions;

	/**
	 * @param header
	 * @param notedto
	 *  <p><b>To add color for particular note in todo application</b></p>
	 * @throws ToDoExceptions 
	 */
	void addColor(String userId, String noteId, String color)throws ToDoExceptions;

	/**
	 * @param userId
	 * @param notedto
	 * <p><b>To get metadata from note which contained url using content srapping</b></p>
	 * @return metadata
	 * @throws IOException 
	 * @throws ToDoExceptions 
	 */
	List<MetaData> getMetaData(String userId, NoteDTO notedto) throws IOException, ToDoExceptions;

	
	
	
}
