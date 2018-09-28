package com.bridgelabz.noteservice;

import java.util.Map;

public class NoteTestCase {
	private int id;
	private String type;
	private String category;
	private String urlPath;
	private String purpose;
	private int statusCode;
	private int validErrorCode;
	private String testCaseModal;
	private String responseMessage;
	private String validErrorMessage;
	private String useToken;
	private Map<String, String> params;


	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getCategory() {
		return category;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public String getPurpose() {
		return purpose;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public int getValidErrorCode() {
		return validErrorCode;
	}

	public String getTestCaseModal() {
		return testCaseModal;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public String getValidErrorMessage() {
		return validErrorMessage;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setValidErrorCode(int validErrorCode) {
		this.validErrorCode = validErrorCode;
	}

	public void setTestCaseModal(String testCaseModal) {
		this.testCaseModal = testCaseModal;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getUseToken() {
		return useToken;
	}

	public void setUseToken(String useToken) {
		this.useToken = useToken;
	}

	public void setValidErrorMessage(String validErrorMessage) {
		this.validErrorMessage = validErrorMessage;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
