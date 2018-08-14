package com.bridgelabz.noteservice.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @since 23/07/2018
 * <p><b></b></p>
 * @author yuga
 *
 */
@Document(indexName = "labelindex",type="label")
public class Label {

	@Id
	private String labelId;
	private String labelName;
	private String userId;
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

}
