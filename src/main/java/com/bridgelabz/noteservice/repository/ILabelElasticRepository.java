package com.bridgelabz.noteservice.repository;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.noteservice.model.Label;


@Repository
public interface ILabelElasticRepository extends ElasticsearchRepository<Label, String> {

	Optional<Label> findByLabelName(String lableName);

	Label findByUserIdAndLabelName(String userId, String string);

	void deleteByLabelName(String labelName);

}
