package com.bridgelabz.noteservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.noteservice.model.Label;



/**@since 23/07/2018
 * <p><b></b></p>
 * @author yuga
 * 
 *
 */

public interface ILabelRepository extends MongoRepository<Label, String> {

	/**
	 * @param labelName
	 * <p><b>To find by label name which is already present in database or not</b></p>
	 * @return 
	 */
	Optional<Label> findByLabelName(String labelName);

	/**
	 * @param userId
	 * @param string
	 * <p><b>To find by userId and name of label from note list</b></p>
	 * @return list
	 */
	Label findByUserIdAndLabelName(String userId, String string);

	void deleteByLabelName(String labelName);

}
