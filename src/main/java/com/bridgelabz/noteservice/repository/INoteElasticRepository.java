package com.bridgelabz.noteservice.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.bridgelabz.noteservice.model.Note;


/**
 * <p><b>Note repository extended Elastic repository which is helpful for searching data </b></p>
 * @author yuga
 *
 */
public interface INoteElasticRepository extends ElasticsearchRepository<Note, String> {

	/**
	 * @param userId
	 * @return
	 */
	List<Note> findAllByUserId(String userId);

	/**
	 * @param userId
	 * @param status
	 * <p>find all notes of the particular user</p>
	 * @return list of notes
	 */
	List<Note> findAllByUserIdAndTrashStatus(String userId, boolean status);

}
