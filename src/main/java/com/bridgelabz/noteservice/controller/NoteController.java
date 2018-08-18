package com.bridgelabz.noteservice.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.noteservice.model.MetaData;
import com.bridgelabz.noteservice.model.Note;
import com.bridgelabz.noteservice.model.NoteDTO;
import com.bridgelabz.noteservice.model.ResponseDTO;
import com.bridgelabz.noteservice.service.IFeignClient;
import com.bridgelabz.noteservice.service.INoteService;
import com.bridgelabz.noteservice.utilservice.exceptions.ToDoExceptions;
import com.bridgelabz.noteservice.utilservice.messageservice.MessageSourceService;


/**
 * @author yuga
 * @since 17/07/2018
 *<p><b>To interact with the view and services.controller is the mediabetween view and model.</b></p>
 *
 */
@RestController
@RefreshScope
@RequestMapping("/notes")
public class NoteController {
	final static String REQUEST_ID = "IN_NOTE";
	final static String RESPONSE_ID = "OUT_NOTE";
	private Logger logger = LoggerFactory.getLogger(NoteController.class);
	@Autowired
	INoteService noteService;
   
	@Autowired
	private IFeignClient feignClient;
	/**
	 * @param token
	 * @param notedto
	 * <p><b>To take create note url from view and perform operations </b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/createnote")
	public ResponseEntity<ResponseDTO> createNote(HttpServletRequest request ,@RequestBody NoteDTO notedto) throws ToDoExceptions, ParseException {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId =request.getHeader("userId");

		String noteId = noteService.createNote(notedto, userId);
		logger.info(RESPONSE_ID+request.getRequestURI());
		return new ResponseEntity(MessageSourceService.getMessage("117")+noteId, HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param noteId
	 * @param req
	 * <p><b>To take readNote url from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/readnote/{noteId}")
	public ResponseEntity<ResponseDTO> readNote(HttpServletRequest request,@PathVariable String noteId) throws ToDoExceptions {
			logger.info(REQUEST_ID+request.getRequestURI());
			
			String userId =request.getHeader("userId");
			
			Note note = noteService.readNote(noteId, userId);
			logger.info(RESPONSE_ID+request.getRequestURI());
			return new ResponseEntity(note, HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param req
	 * <p><b>To take readAllNotes url from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/readallnotes")
	public ResponseEntity<ResponseDTO> readAllNotes(HttpServletRequest request,
												@RequestParam (required=false)String sortByTitle_sortByDate,
												@RequestParam (required=false)String ascendingOrdescending,
												@RequestHeader("TOKEN") String token) throws ToDoExceptions {
			logger.info(REQUEST_ID+request.getRequestURI());	
		
			String userId = (String) request.getAttribute("userId");
			List<Note> note = noteService.readAllNotes(userId,sortByTitle_sortByDate,ascendingOrdescending);
			logger.info(RESPONSE_ID+request.getRequestURI());
			return new ResponseEntity(note, HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param updatedto
	 * @param req
	 * <p><b>To take updateNote url from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/updatenote")
	public ResponseEntity<ResponseDTO> updateNote(HttpServletRequest request,@RequestBody NoteDTO notedto) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId = (String) request.getAttribute("userId");

		noteService.updateNote(notedto, userId);
		logger.info(RESPONSE_ID+request.getRequestURI());
		return new ResponseEntity(MessageSourceService.getMessage("118"), HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param noteId
	 * @param req
	 * <p><b>To take deleteNote url and request body or request param and also take token from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PutMapping("/deletenote/{noteId}")
	public ResponseEntity<ResponseDTO> deleteNote(HttpServletRequest request,@PathVariable String noteId) throws ToDoExceptions {
		logger.info(REQUEST_ID +request.getRequestURI());
	
		String userId =request.getHeader("userId");
		noteService.deleteNote(noteId, userId);
		logger.info(RESPONSE_ID +request.getRequestURI());	
		return new ResponseEntity(MessageSourceService.getMessage("119"), HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param noteId
	 * @param req
	 * <p><b>To take deleteNoteFromTrash url and request body or request param and also take token  from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping("/permanentdelete/{noteId}")
	public ResponseEntity<ResponseDTO> deleteNoteFromTrash(HttpServletRequest request,@PathVariable String noteId) throws ToDoExceptions {
			logger.info(REQUEST_ID+request.getRequestURI());
			
			String userId =request.getHeader("userId");
			
			noteService.deleteNoteFromTrash(noteId, userId);
			logger.info(RESPONSE_ID+request.getRequestURI());	
			return new ResponseEntity(MessageSourceService.getMessage("120"), HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param noteId
	 * @param req
	 * <p><b>To take restoreNoteFromTrash url and request body or request param and also take token from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PutMapping("/restorefromtrash/{noteId}")
	public ResponseEntity<ResponseDTO> restoreNoteFromTrash(HttpServletRequest request,@PathVariable String noteId) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());	
		
		String userId =request.getHeader("userId");
		
		noteService.restoreNoteFromTrash(noteId, userId);
			logger.info(RESPONSE_ID+request.getRequestURI());	
			return new ResponseEntity(MessageSourceService.getMessage("121"), HttpStatus.OK);
	}
	/******************************************************************************************************/

	
	/**
	 * @param request
	 * @return list
	 * <p><b>To take readAllNotesFromTrash url and also take token from view and perform operations</b></p>
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/readallfromtrash")
	public ResponseEntity<ResponseDTO> readAllFromTrash(HttpServletRequest request) throws ToDoExceptions {
			logger.info(REQUEST_ID+request.getRequestURI());	
			
			String userId =request.getHeader("userId");
			
			List<Note> note = noteService.readAllFromTrash(userId);
			logger.info(RESPONSE_ID+request.getRequestURI());
			return new ResponseEntity(note, HttpStatus.OK);
	}
	/******************************************************************************************************/

	/**
	 * @param request
	 * <p><b>To take emptytrash url, perform operations on it and gives back appropriate response to view</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("/emptytrash")
	public ResponseEntity<ResponseDTO> emptyTrash(HttpServletRequest request) throws ToDoExceptions {
			logger.info(REQUEST_ID+request.getRequestURI());	
			
			String userId =request.getHeader("userId");
			
			noteService.emptyTrash(userId);
			logger.info(RESPONSE_ID+request.getRequestURI());
			return new ResponseEntity(MessageSourceService.getMessage("122"), HttpStatus.OK);
	}
	
	
	/******************************************************************************************************/
	/**
	 * @param noteId
	 * @param req
	 * <p><b>To take pinnote url and request body or request param and also take token from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/pinnote/{boolean-value}/{noteId}")
	public ResponseEntity<ResponseDTO> pinNote(HttpServletRequest request,@PathVariable String noteId,@PathVariable boolean value) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());	
		
		String userId =request.getHeader("userId");

		boolean condition = noteService.pinNote(userId,noteId,value);
		if(condition) {
		logger.info(RESPONSE_ID+request.getRequestURI());	
			return new ResponseEntity(MessageSourceService.getMessage("123"), HttpStatus.OK);
		}else
			return new ResponseEntity(MessageSourceService.getMessage("124"), HttpStatus.OK);
	}
	
/******************************************************************************************************/
	/**
	 * @param noteId
	 * @param req
	 * <p><b>To take archiveNote url and request body or request param and also take token  from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/archievenote/{noteId}/{boolean-value}")
	public ResponseEntity<ResponseDTO> archieveNote(HttpServletRequest request,@PathVariable String noteId,@PathVariable boolean value) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());	
		
		String userId =request.getHeader("userId");
		
	boolean condition =	noteService.archieveNote(userId,noteId,value);
	if(condition) {
		logger.info(RESPONSE_ID+request.getRequestURI());	
			return new ResponseEntity(MessageSourceService.getMessage("125"), HttpStatus.OK);
	}else
		return new ResponseEntity(MessageSourceService.getMessage("126"), HttpStatus.OK);
	}
/******************************************************************************************************/
	
	/**
	 * @param request
	 *<p><b>To take getallarchievenotes url,perform operations on it and send back the list of notes which is in achieve to view</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/getallarchievenotes")
	public ResponseEntity<ResponseDTO> getAllNotesFromArchive(HttpServletRequest request) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());	
		
		String userId =request.getHeader("userId");
		
		List<Note>listOfNotesFromArcheieve = noteService.getAllNotesFromArchive(userId);
		logger.info(RESPONSE_ID+request.getRequestURI());	
			return new ResponseEntity(listOfNotesFromArcheieve, HttpStatus.OK);
	}
	
	/******************************************************************************************************/
	/**
	 * @param request
	 * @param noteId
	 * <p><b>To take remindme url and request body or request param and also take token from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/remindme/{noteId}")
	public ResponseEntity<ResponseDTO> remindMe(HttpServletRequest request,@PathVariable String noteId , @RequestBody String reminderDate) throws ToDoExceptions, ParseException {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId =request.getHeader("userId");
		
		noteService.reminderNote(userId,noteId, reminderDate);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity(MessageSourceService.getMessage("127"), HttpStatus.OK);
	}
	/******************************************************************************************************/
	/**
	 * @param request
	 * @param noteId
	 * <p><b>To take remove reminder url and request param  from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 * @throws ParseException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/removereminder/{noteId}")
	public ResponseEntity<ResponseDTO> removeReminder(HttpServletRequest request,@PathVariable String noteId) throws ToDoExceptions, ParseException {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId =request.getHeader("userId");

		noteService.removeReminder(userId,noteId);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity(MessageSourceService.getMessage("128"), HttpStatus.OK);
	}
	/******************************************************************************************************/	
	
	/**
	 * @param request
	 * @param notedto
	 * @return response
	 * <p><b>To take addcolor url and perform operations on given requirement and send back the appropriate response to view</b></p>
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PutMapping("/addcolor/{noteId}/{color}")
	public ResponseEntity<ResponseDTO> addColor(HttpServletRequest request,@PathVariable String color,@PathVariable String noteId) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId =request.getHeader("userId");
		
		noteService.addColor(userId, noteId,color);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity(MessageSourceService.getMessage("129"), HttpStatus.OK);
	}
	
/******************************************************************************************************/
	
	/**
	 * @param request
	 * @param labelName
	 *<p><b>To take createlabel url and perform operations on given requirement and send back the appropriate response to view</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/createlabel/{LabelName}")
	public ResponseEntity<ResponseDTO> createLabels(HttpServletRequest request,@PathVariable String labelName) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId =request.getHeader("userId");
		
		noteService.createLabels(userId, labelName);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity(MessageSourceService.getMessage("130"), HttpStatus.OK);
	}
/********************************************************************************************************/
	/**
	 * @param request
	 * @param labelName
	 *<p><b>To take deletelabel url and perform operations on given requirement and send back the appropriate response to view</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PutMapping("/deletelabel/{labelName}")
	public ResponseEntity<ResponseDTO> deleteLabel(HttpServletRequest request,@PathVariable String labelName) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId =request.getHeader("userId");
		
		noteService.deleteLabel(userId, labelName);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity(MessageSourceService.getMessage("131"), HttpStatus.OK);
	}
	/******************************************************************************************************/
	
	/**
	 * @param request
	 * @param noteId
	 *<p><b>To take addlabel url and request body or request param and also take token from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 * @throws ParseException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PutMapping("/addlabel/{noteId}")
	public ResponseEntity<ResponseDTO> addlabels(HttpServletRequest request,@PathVariable String noteId ,@RequestBody List<String>labels) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId =request.getHeader("userId");
		
		noteService.addlabels(userId, noteId,labels);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity(MessageSourceService.getMessage("133"), HttpStatus.OK);
	}
	
	/******************************************************************************************************/
	/**
	 * @param request
	 * @param currentLabelName
	 * @param newLabelName
	 *<p><b>To take editlabel url and request body or request param and also take token from view and perform operations</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/editlabel")
	public ResponseEntity<ResponseDTO> editLabel(HttpServletRequest request,
			@RequestParam(value="Current Label Name")String currentLabelName ,
			@RequestParam(value="New Label Name")String newLabelName) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId =request.getHeader("userId");

		noteService.editLabel(userId, currentLabelName,newLabelName);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity(MessageSourceService.getMessage("132"), HttpStatus.OK);
	}
	
	/******************************************************************************************************/
	/**
	 * @param request
	 *<p><b>To take getalllabels url and perform operations on given requirement and send back the Label List to view</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/getalllabels")
	public ResponseEntity<ResponseDTO> getAllLabels(HttpServletRequest request) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId =request.getHeader("userId");

		List<String>listOfLabels = noteService.getAllLabels(userId);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity(listOfLabels, HttpStatus.OK);
	}
	
	/******************************************************************************************************/
	/**
	 * @param request
	 * @param labelName
	 *<p><b>To take searchnotesbylabel url and perform operations on given requirement and send back the notes List to view</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/searchnotesbylabel/{labelName}")
	public ResponseEntity<ResponseDTO> searchNotesByLabelName(HttpServletRequest request,@PathVariable String labelName) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId =request.getHeader("userId");

		List<Note>listOfNotes = noteService.searchNotesByLabelName(userId,labelName);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity(listOfNotes, HttpStatus.OK);
	}
	
	
	/**
	 * @param request
	 * @param noteId
	 * @param labelName
	 *<p><b>To take removelabelfromnote url and perform operations on given requirement and send back the appropriate response to view</b></p>
	 * @return response
	 * @throws ToDoExceptions
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/removelabelfromnote/{noteId}")
	public ResponseEntity<ResponseDTO> removeLabelFromNote(HttpServletRequest request,@PathVariable String noteId , @RequestParam (value="Label Name")String labelName) throws ToDoExceptions {
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId =request.getHeader("userId");

		noteService.removelabelfromnote(userId,noteId,labelName);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity(MessageSourceService.getMessage("134"), HttpStatus.OK);
	}
	
	/**
	 * @param request
	 * @param notedto
	 * @return response using response entity
	 * @throws IOException 
	 * @throws ToDoExceptions 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/contentscrap")
	public ResponseEntity<ResponseDTO>contentScrapping(HttpServletRequest request ,@RequestBody NoteDTO notedto) throws IOException, ToDoExceptions
	{
		logger.info(REQUEST_ID+request.getRequestURI());
		
		String userId =request.getHeader("userId");
		 List<MetaData> metadataList = noteService.getMetaData(userId , notedto);
		logger.info(RESPONSE_ID+request.getRequestURI());	
		return new ResponseEntity(metadataList, HttpStatus.OK);
	}
	
	@GetMapping("/getuserbyemailid/{emailId}")
	public ResponseEntity<?>findUserById(@PathVariable ("emailId") String emailId) throws ToDoExceptions{
		System.out.println("inside method in note-controller");
		
		return feignClient.getUserByEmailId(emailId);
	}
}
