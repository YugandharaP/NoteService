package com.bridgelabz.noteservice.repository;


import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ElasticRepositoryImplementation implements IElasticRepository{
	private final String INDEX = "fundooapp";
	private final String TYPE = "notes";

	@Autowired
	private RestHighLevelClient restHighLevelClient;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public <T> void save(T object, String id) throws JsonProcessingException {
		String string = objectMapper.writeValueAsString(object);
		// Map dataMap = objectMapper.convertValue(object, Map.class);
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, id).source(string, XContentType.JSON);
		try {
			IndexResponse response = restHighLevelClient.index(indexRequest);
		} catch (ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}
	}

}
