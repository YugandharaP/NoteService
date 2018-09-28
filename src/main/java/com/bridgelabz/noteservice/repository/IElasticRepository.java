package com.bridgelabz.noteservice.repository;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IElasticRepository {
	public <T> void save(T object, String id) throws JsonProcessingException;
}
