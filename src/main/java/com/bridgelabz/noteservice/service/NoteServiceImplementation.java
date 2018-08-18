package com.bridgelabz.noteservice.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bridgelabz.noteservice.model.Label;
import com.bridgelabz.noteservice.model.MetaData;
import com.bridgelabz.noteservice.model.Note;
import com.bridgelabz.noteservice.model.NoteDTO;
import com.bridgelabz.noteservice.repository.ILabelElasticRepository;
import com.bridgelabz.noteservice.repository.ILabelRepository;
import com.bridgelabz.noteservice.repository.INoteElasticRepository;
import com.bridgelabz.noteservice.repository.INoteRepository;
import com.bridgelabz.noteservice.utilservice.exceptions.RestPreconditions;
import com.bridgelabz.noteservice.utilservice.exceptions.ToDoExceptions;
import com.bridgelabz.noteservice.utilservice.mapperservice.ModelMapperService;
import com.bridgelabz.noteservice.utilservice.messageservice.MessageSourceService;

/**
 * @author yuga
 * @since 17/07/2018
 *        <p>
 *        <b>To connect controller and MongoRepository and provides
 *        implementation of the service methods </b>
 *        </p>
 */
@Service
public class NoteServiceImplementation implements INoteService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImplementation.class);
	@Autowired
	private INoteRepository noteRepository;
	
	@Autowired
	private ModelMapperService modelMapper;

	@Autowired
	private ILabelRepository labelRepository;
	
	@Autowired
	private	INoteElasticRepository noteElasticRepository;
	
	@Autowired
	private ILabelElasticRepository labelElasticRepository;
	
	@Value("${patternString}")
	private String patternString;

	/**
	 * @param note
	 * @param token
	 * <p><b>To create note for particular user id in todo application </b></p>
	 * @throws ToDoExceptions
	 * @throws ParseException
	 */
	@Override
	public String createNote(NoteDTO notedto, String userId) throws ToDoExceptions, ParseException {
		System.out.println(userId);
		RestPreconditions.checkNotNull(notedto.getTitle(), MessageSourceService.getMessage("135"));
		RestPreconditions.checkNotNull(notedto.getDiscription(),MessageSourceService.getMessage("136"));

		RestPreconditions.checkNotNull(userId,MessageSourceService.getMessage("141"));
		Note note = modelMapper.map(notedto, Note.class);
		if (notedto.getRemainder() != "" && notedto.getRemainder() != null) {
			Date date = new SimpleDateFormat("dd/MM/yyyy").parse(notedto.getRemainder());
			RestPreconditions.checkArgument(date.before(new Date()),MessageSourceService.getMessage("152"));
			note.setReminder(date);
		}
		note.setUserId(userId);
		note.setCreatedDate(new Date());
		note.setLastModifiedDate(new Date());
		
		
		List<Label> list = new ArrayList<Label>();
		if(!notedto.getLabelNameList().isEmpty())//check labelList is empty or not
		{
			for (int i = 0; i < notedto.getLabelNameList().size(); i++) {
				Label labelOfUser = labelRepository.findByUserIdAndLabelName(userId, notedto.getLabelNameList().get(i));
				if (labelOfUser == null || !labelOfUser.getLabelName().equals("")) {
					Label label = new Label();
					label.setUserId(userId);
					label.setLabelName(notedto.getLabelNameList().get(i));
					labelRepository.insert(label);//save in label repository
				
				list.add(label);
				note.setListOfLabels(list);//set labels into the notes
			}else
			{
				
				if(!labelOfUser.getLabelName().equals("")){
					list.add(labelOfUser);
					note.setListOfLabels(list);
				}
			}
		}
	}
	noteRepository.insert(note);
	noteElasticRepository.save(note);
	return note.getId();
}

	/**
	 * @param noteId
	 * @param token
	 * <p><b>To delete note from todo application based on note Id which is given by the user </b></p>
	 * @throws ToDoExceptions
	 */
	@Override
	public String deleteNote(String noteId, String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId,MessageSourceService.getMessage("140"));
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));

		Optional<Note> optionalNote = noteElasticRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				MessageSourceService.getMessage("142"));

		Note note = optionalNote.get();
		note.setTrashStatus(true);
		noteRepository.save(optionalNote.get());
		noteElasticRepository.save(optionalNote.get());
		return optionalNote.get().getId();
	}

	/**
	 * @param note
	 * @param token
	 * <p><b>To read note from todo application based on note id which isenterd by user </b></p>
	 * @throws ToDoExceptions
	 */
	@Override
	public Note readNote(String noteId, String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, MessageSourceService.getMessage("140"));
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));

		Optional<Note> optionalNote = noteElasticRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				MessageSourceService.getMessage("142"));

		RestPreconditions.checkArgument(optionalNote.get().isTrashStatus(), MessageSourceService.getMessage("143"));

		Note note = optionalNote.get();
		return note;
	}

	/**
	 * @param token
	 *            <p>
	 *            <b>To read all notes from todo application based on user id </b>
	 *            </p>
	 * @throws ToDoExceptions
	 */
	@Override
	public List<Note> readAllNotes(String userId, String choiceOfSorting, String ascendingOrdescending) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));

		List<Note> noteList = noteElasticRepository.findAllByUserIdAndTrashStatus(userId,false);
		if(choiceOfSorting.equals(null)||choiceOfSorting.equalsIgnoreCase("sortByTitle")) {
			if(ascendingOrdescending==null||ascendingOrdescending.equalsIgnoreCase("ascending"))
			{
				return noteList.stream().sorted(Comparator.comparing(Note::getTitle)).collect(Collectors.toList());
			}
			return noteList.stream().sorted(Comparator.comparing(Note::getTitle).reversed()).collect(Collectors.toList());
		}
		
		if(ascendingOrdescending==null||ascendingOrdescending.equalsIgnoreCase("ascending"))
		{
			return noteList.stream().sorted(Comparator.comparing(Note::getCreatedDate)).collect(Collectors.toList());
		}
		return noteList.stream().sorted(Comparator.comparing(Note::getCreatedDate).reversed()).collect(Collectors.toList());
	}

	/**
	 * @param updatedto
	 * @param token
	 * <p> <b>To update note from todo application based on note id which is enterd by user </b> </p>
	 * @throws ToDoExceptions
	 */
	@Override
	public void updateNote(NoteDTO notedto, String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(notedto,MessageSourceService.getMessage("140"));
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));

		Optional<Note> optionalNote = noteElasticRepository.findById(notedto.getNoteId());
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				MessageSourceService.getMessage("142"));

		RestPreconditions.checkArgument(optionalNote.get().isTrashStatus(), MessageSourceService.getMessage("143"));

		Note note = optionalNote.get();
		if (!notedto.getTitle().equals("")) {
			note.setTitle(notedto.getTitle());
		}
		if (!notedto.getDiscription().equals("")) {
			note.setDiscription(notedto.getDiscription());
		}
		note.setLastModifiedDate(new Date());
		noteRepository.save(note);
		noteElasticRepository.save(note);
	}

	/**
	 * @param noteId
	 * @param token
	 *            <p>
	 *            <b>To delete note from trash</b>
	 *            </p>
	 * @throws ToDoExceptions
	 **/

	@Override
	public void deleteNoteFromTrash(String noteId, String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, MessageSourceService.getMessage("140"));
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		Optional<Note> optionalNote = noteElasticRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				MessageSourceService.getMessage("142"));

		RestPreconditions.checkArgument(!optionalNote.get().isTrashStatus(),
				MessageSourceService.getMessage("144"));

		Note note = optionalNote.get();
		noteElasticRepository.delete(note);
		noteRepository.delete(note);
	}

	/**
	 * @param noteId
	 * @param token
	 *            <p>
	 *            <b>To restore the note from trash</b>
	 *            </p>
	 * @throws ToDoExceptions
	 **/

	@Override
	public void restoreNoteFromTrash(String noteId, String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, MessageSourceService.getMessage("140"));
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));

		Optional<Note> optionalNote = noteElasticRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				MessageSourceService.getMessage("142"));

		RestPreconditions.checkArgument(!optionalNote.get().isTrashStatus(), MessageSourceService.getMessage("144"));

		Note note = optionalNote.get();
		note.setTrashStatus(false);
		note.setLastModifiedDate(new Date());
		noteRepository.save(note);
		noteElasticRepository.save(note);
	}

	/**
	 * @param token
	 * @param noteId
	 *            <p>
	 *            <b>To pin the particular note</b>
	 *            </p>
	 * @throws ToDoExceptions
	 **/
	@Override
	public boolean pinNote(String userId, String noteId,boolean value) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId,MessageSourceService.getMessage("140"));
		RestPreconditions.checkNotNull(userId,MessageSourceService.getMessage("141"));

		Optional<Note> optionalNote = noteElasticRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				MessageSourceService.getMessage("142"));

		Note note = optionalNote.get();
		RestPreconditions.checkArgument(optionalNote.get().isTrashStatus(), MessageSourceService.getMessage("143"));
		if(value) {
			if (note.isArchiveNOte()) {
				note.setArchiveNOte(false);
			}else {
				note.setPinNote(true);
			}
			noteRepository.save(note);
			noteElasticRepository.save(note);
			return true;
		}else {
			note.setPinNote(false);
			noteRepository.save(note);
			noteElasticRepository.save(note);
			return false;
		}
	}

	/**
	 * @param token
	 * @param noteId
	 * <p><b>To archive particular note</b></p>
	 * @throws ToDoExceptions
	 */
	@Override
	public boolean archieveNote(String userId, String noteId,boolean condition) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, MessageSourceService.getMessage("140"));
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));

		Optional<Note> optionalNote = noteElasticRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				MessageSourceService.getMessage("142"));

		RestPreconditions.checkArgument(optionalNote.get().isTrashStatus(), MessageSourceService.getMessage("143"));

		Note note = optionalNote.get();
		if(condition) {
			note.setArchiveNOte(true);
			noteRepository.save(note);
			noteElasticRepository.save(note);
			return true;
		}
		else {
			note.setArchiveNOte(false); 
			noteRepository.save(note);
			noteElasticRepository.save(note);
			return false;
		}
		
	}

	/**
	 * @throws ToDoExceptions
	 * 
	 */
	@Override
	public void removeNoteFromArcheive(String userId, String noteId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, MessageSourceService.getMessage("140"));
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));

		Optional<Note> optionalNote = noteElasticRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				MessageSourceService.getMessage("142"));

		RestPreconditions.checkArgument(optionalNote.get().isTrashStatus(), MessageSourceService.getMessage("143"));

		Note note = optionalNote.get();
		RestPreconditions.checkArgument(!note.isArchiveNOte(), MessageSourceService.getMessage("146"));

		note.setArchiveNOte(false);
		noteRepository.save(note);
		noteElasticRepository.save(note);
	}

	/**
	 **/
	@Override
	public List<Note> getAllNotesFromArchive(String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		List<Note> noteList = noteElasticRepository.findAllByUserId(userId);
		List<Note> finalNoteList = new ArrayList<Note>();
		
		
		finalNoteList = noteList.stream().filter(streamNote->streamNote.isArchiveNOte()).collect(Collectors.toList());
		
		RestPreconditions.checkNotNull(finalNoteList, MessageSourceService.getMessage("142"));
		return finalNoteList;
	}
	/**
	 * @param token
	 * @param noteId
	 * @param reminderDate
	 * <p>To set reminder date for particular note</p>
	 * @throws ToDoExceptions
	 * @throws ParseException
	 */

	@Override
	public void reminderNote(String userId, String noteId, String reminderDate) throws ToDoExceptions, ParseException {
		RestPreconditions.checkNotNull(noteId, MessageSourceService.getMessage("140"));
		RestPreconditions.checkNotNull(reminderDate, MessageSourceService.getMessage("148"));

		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));

		Optional<Note> optionalNote = noteElasticRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				MessageSourceService.getMessage("142"));
		Date dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(reminderDate);
		RestPreconditions.checkArgument(dateFormat.before(new Date()),MessageSourceService.getMessage("152"));
		optionalNote.get().setReminder(dateFormat);
		noteRepository.save(optionalNote.get());
		noteElasticRepository.save(optionalNote.get());
	}

	/**
	 * @throws ToDoExceptions
	 * 
	 */
	@Override
	public void removeReminder(String userId, String noteId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(noteId, MessageSourceService.getMessage("140"));
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		Optional<Note> optionalNote = noteElasticRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				MessageSourceService.getMessage("142"));
		optionalNote.get().setReminder(null);
		noteRepository.save(optionalNote.get());
		noteElasticRepository.save(optionalNote.get());
	}

	/**
	 * @throws ToDoExceptions
	 * 
	 */
	@Override
	public void createLabels(String userId, String lableName) throws ToDoExceptions {
		RestPreconditions.checkNotNull(lableName, MessageSourceService.getMessage("137"));
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		Optional<Label> optionalLabel = labelElasticRepository.findByLabelName(lableName);

		if (!optionalLabel.isPresent()) {
			Label label = new Label();
			label.setLabelName(lableName);
			label.setUserId(userId);
			labelRepository.insert(label);// new label created
			labelElasticRepository.save(label);
		} else {
			throw new ToDoExceptions(MessageSourceService.getMessage("151"));
		}
	}

	@Override
	public void addColor(String userId, String noteId, String color) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		RestPreconditions.checkNotNull(noteId, MessageSourceService.getMessage("142"));
		RestPreconditions.checkNotNull(color, MessageSourceService.getMessage("147"));

		Optional<Note> optionalNote = noteElasticRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				MessageSourceService.getMessage("142"));
		optionalNote.get().setColorCode(color);
		noteRepository.save(optionalNote.get());
		noteElasticRepository.save(optionalNote.get());
	}

	/**
	 * @throws ToDoExceptions
	 */
	@Override
	public void addlabels(String userId, String noteId, List<String> labelNames) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		RestPreconditions.checkNotNull(noteId, MessageSourceService.getMessage("140"));
		Optional<Note> optionalNote = noteElasticRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),
				MessageSourceService.getMessage("142"));
		for (int i = 0; i < labelNames.size(); i++) {
			Label labelOfUser = labelElasticRepository.findByUserIdAndLabelName(userId, labelNames.get(i));
			if (labelOfUser == null) {
				Label label = new Label();
				label.setUserId(userId);
				label.setLabelName(labelNames.get(i));
				labelRepository.insert(label);
				labelElasticRepository.save(label);
				List<Label> labelListInNote = optionalNote.get().getListOfLabels();
				if (labelListInNote != null) {
					optionalNote.get().getListOfLabels().add(label);
				} else {
					List<Label> list = new ArrayList<Label>();
					list.add(label);
					optionalNote.get().setListOfLabels(list);
				}
				noteRepository.save(optionalNote.get());// confused out of loop or inside
				noteElasticRepository.save(optionalNote.get());
			} else {
				List<Label> labelListInNote = optionalNote.get().getListOfLabels();

				List<String> labelStringNamesInNote;// contains only names of the labels

				if (labelListInNote != null) {
					labelStringNamesInNote = new ArrayList<String>();
					for (int k = 0; k < labelListInNote.size(); k++) {
						labelStringNamesInNote.add(labelListInNote.get(k).getLabelName());
					}
					if (!labelStringNamesInNote.contains(labelOfUser.getLabelName())) {
						optionalNote.get().getListOfLabels().add(labelOfUser);
					}
				} else {
					List<Label> list = new ArrayList<Label>();
					list.add(labelOfUser);
					optionalNote.get().setListOfLabels(list);
				}
				noteRepository.save(optionalNote.get());
				noteElasticRepository.save(optionalNote.get());
			}
		}
	}

	/**
	 * @param userId-String
	 *            type of user id which extract from token
	 *            <p>
	 *            <b>To read all notes from trash</b>
	 *            </p>
	 * @throws ToDoException
	 **/
	@Override
	public List<Note> readAllFromTrash(String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		List<Note> noteList = noteElasticRepository.findAllByUserIdAndTrashStatus(userId, true);
		RestPreconditions.checkNotNull(noteList, MessageSourceService.getMessage("144"));
		return noteList;
	}
	
	/**
	 * @param userId-String type of user id which extract from token
	 * <p><b>To empty all notes from trash</b></p>
	 * @throws ToDoExceptions 
	 **/
	@Override
	public void emptyTrash(String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));

		List<Note> noteList = noteElasticRepository.findAllByUserId(userId);
		RestPreconditions.checkNotNull(noteList, MessageSourceService.getMessage("144"));
		Stream<Note> trashNoteList = noteList.stream().filter(streamNoteList->streamNoteList.isTrashStatus()==true);
		trashNoteList.forEach(finalNoteList->noteRepository.delete(finalNoteList));
		trashNoteList.forEach(finalNoteList->noteElasticRepository.delete(finalNoteList));

	}
	/**
	 * **/
	@Override
	public void deleteLabel(String userId, String labelName) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		RestPreconditions.checkNotNull(labelName, MessageSourceService.getMessage("137"));
		Optional<Label> labelFromRepository = labelElasticRepository.findByLabelName(labelName);
		RestPreconditions.checkArgument(!labelFromRepository.isPresent(),
				MessageSourceService.getMessage("150"));
		Label label = labelFromRepository.get();
		labelRepository.deleteByLabelName(label.getLabelName());
		labelElasticRepository.deleteByLabelName(label.getLabelName());
		LOGGER.info(MessageSourceService.getMessage("131"));
		List<Note> listOfNotes = noteElasticRepository.findAllByUserId(userId);
		if (listOfNotes.size() == 0) {
			throw new ToDoExceptions(MessageSourceService.getMessage("153"));
		}
		for (int i = 0; i < listOfNotes.size(); i++) {
			if (listOfNotes.get(i).getListOfLabels() != null) {
				for (int j = 0; j < listOfNotes.get(i).getListOfLabels().size(); j++) {
					if (listOfNotes.get(i).getListOfLabels().get(j).getLabelName().equals(labelName)) {
						listOfNotes.get(i).getListOfLabels().remove(j);
						LOGGER.info("delete label from list");
						noteRepository.save(listOfNotes.get(i));
						noteElasticRepository.save(listOfNotes.get(i));
						LOGGER.info("save updated note");						
					}
				}
			}
		}
	}

	/**
	 * @throws ToDoExceptions
	 **/
	@Override
	public void editLabel(String userId, String currentLabelName, String newLabelName) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		RestPreconditions.checkNotNull(currentLabelName, MessageSourceService.getMessage("138"));
		RestPreconditions.checkNotNull(newLabelName, MessageSourceService.getMessage("139"));
		Optional<Label> labelFromRepository = labelElasticRepository.findByLabelName(currentLabelName);
		RestPreconditions.checkArgument(!labelFromRepository.isPresent(),
				MessageSourceService.getMessage("150"));
		labelFromRepository.get().setLabelName(newLabelName);
		labelRepository.save(labelFromRepository.get());
		labelElasticRepository.save(labelFromRepository.get());
		LOGGER.info("label edited from label repository");
		List<Note> listOfNotes = noteElasticRepository.findAllByUserId(userId);
		if (listOfNotes.size() == 0) {
			throw new ToDoExceptions("No notes found");
		}
		for (int i = 0; i < listOfNotes.size(); i++) {
			if (listOfNotes.get(i).getListOfLabels() != null) {
				for (int j = 0; j < listOfNotes.get(i).getListOfLabels().size(); j++) {
					if (listOfNotes.get(i).getListOfLabels().get(j).getLabelName().equals(currentLabelName)) {
						listOfNotes.get(i).getListOfLabels().get(j).setLabelName(newLabelName);
						LOGGER.info("edit label from list");
						noteRepository.save(listOfNotes.get(i));
						noteElasticRepository.save(listOfNotes.get(i));
						LOGGER.info("save edited note");
					}
				}
			}
		}
	}

	/**
	 * @throws ToDoExceptions
	 **/
	@Override
	public List<String> getAllLabels(String userId) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		List<Label> listOfLabels = labelElasticRepository.findAllByUserId(userId);
		if (listOfLabels.size() == 0) {
			throw new ToDoExceptions(MessageSourceService.getMessage("153"));
		}
		List<String> finalLabelList=listOfLabels.stream().map(streamList->streamList.getLabelName()).collect(Collectors.toList());
		return finalLabelList;
	}

	/**
	 * @throws ToDoExceptions
	 **/
	@Override
	public List<Note> searchNotesByLabelName(String userId, String labelName) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		List<Note> listOfNotes = noteElasticRepository.findAllByUserId(userId);
		if (listOfNotes.size() == 0) {
			throw new ToDoExceptions(MessageSourceService.getMessage("153"));
		}
		List<Note> finalNotesList = new ArrayList<Note>();
		for (int i = 0; i < listOfNotes.size(); i++) {
			if (listOfNotes.get(i).getListOfLabels() != null) {
				for (int j = 0; j < listOfNotes.get(i).getListOfLabels().size(); j++) {
					if (listOfNotes.get(i).getListOfLabels().get(j).getLabelName().equals(labelName)) {
						finalNotesList.add(listOfNotes.get(i));
					}
				}
			}
		}
		return finalNotesList;
	}

	/**
	 * @throws ToDoExceptions 
	 * */
	@Override
	public void removelabelfromnote(String userId, String noteId, String labelName) throws ToDoExceptions {
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		RestPreconditions.checkNotNull(noteId, MessageSourceService.getMessage("140"));
		Optional<Note> optionalNote = noteElasticRepository.findById(noteId);
		RestPreconditions.checkArgument(!optionalNote.isPresent(),MessageSourceService.getMessage("142"));
		
		if(optionalNote.get().getListOfLabels().size() == 0)
		{
			throw new ToDoExceptions(MessageSourceService.getMessage("154"));
		}
		for(int i=0;i<optionalNote.get().getListOfLabels().size();i++)
		{
			if(optionalNote.get().getListOfLabels().get(i).getLabelName().equals(labelName))
			{
				optionalNote.get().getListOfLabels().remove(i);
			}
		}
	
		noteRepository.save(optionalNote.get());
		noteElasticRepository.save(optionalNote.get());
	}

	/* (non-Javadoc)
	 * @see com.bridgelabz.todoapplication.noteservice.service.INoteService#getMetaData(java.lang.String, com.bridgelabz.todoapplication.noteservice.model.NoteDTO)
	 */
	@Override
	public List<MetaData> getMetaData(String userId, NoteDTO notedto) throws IOException, ToDoExceptions {
		RestPreconditions.checkNotNull(userId, MessageSourceService.getMessage("141"));
		RestPreconditions.checkNotNull(notedto.getDiscription(),MessageSourceService.getMessage("136"));
		
		Note note=modelMapper.map(notedto, Note.class);
		List <MetaData> metadataList = new ArrayList<>();
		String description = notedto.getDiscription();
		
		Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(description);
        LOGGER.info("group count before while : "+matcher.groupCount());
        String url =notedto.getDiscription();
        MetaData metadata = new MetaData();
        metadata.setUrl(url);
    	Document document = Jsoup.connect(url).get();
        String title = document.title();
    	metadata.setTitle(title);
    	LOGGER.info("title : "+title);
    	Element image = document.select("img").first();
    	String imageUrl = image.absUrl("src");
    	metadata.setImageUrl(imageUrl);
    	LOGGER.info("image url : " + imageUrl);
    	metadataList.add(metadata);
   
        
        note.setMetadata(metadataList);
		note.setCreatedDate(new Date());
		note.setLastModifiedDate(new Date());
		
		noteRepository.insert(note);
		noteElasticRepository.save(note);
		return metadataList;
	}

}
