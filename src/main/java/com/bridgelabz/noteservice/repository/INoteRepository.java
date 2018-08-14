package com.bridgelabz.noteservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.noteservice.model.Label;
import com.bridgelabz.noteservice.model.Note;


/**
 *@author yuga
 *@since 17/07/2018
 *<p><b>To deal with MongoDB database this note repository interface is required</b></p>
 *
 */

public interface INoteRepository extends MongoRepository<Note,String>{
	/**
	 * @param userId
	 * <p><b>Finding the user id into the repository ,to get the list of notes</b></p>
	 *  @return list
	 */
	List<Note> findAllByUserId(String userId);

	/**
	 * @param listOfLabels
	 * <p><b>Finding list of labels</b></p>
	 * @return list
	 */
	List<Label> findAllByListOfLabels(List<Label> listOfLabels);

}
