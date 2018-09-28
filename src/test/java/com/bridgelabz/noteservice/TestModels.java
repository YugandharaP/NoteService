package com.bridgelabz.noteservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class TestModels {
	private List<NoteModel> noteModels;
	private List<NoteTestCase> noteTestCases;
	private transient Map<String, ArrayList<Integer>> mCategoryTests;
	private transient Map<String, NoteModel> mNoteModels;
	private transient Map<Integer, NoteTestCase> mNoteTestCases;

	public List<NoteModel> getNoteModels() {
		return noteModels;
	}

	public List<NoteTestCase> getNoteTestCases() {
		return noteTestCases;
	}

	public void setNoteModels(List<NoteModel> noteModels) {
		this.noteModels = noteModels;
	}

	public void setNoteTestCases(List<NoteTestCase> noteTestCases) {
		this.noteTestCases = noteTestCases;
	}

	public void constructTestCases() {
		if (mNoteTestCases == null || mCategoryTests == null) {
			mNoteTestCases = new HashMap<Integer, NoteTestCase>();
			mCategoryTests = new HashMap<String, ArrayList<Integer>>();
			for (NoteTestCase testCases : noteTestCases) {
				mNoteTestCases.put(testCases.getId(), testCases);
				ArrayList<Integer> testIds = mCategoryTests.get(testCases.getCategory());
				if (testIds == null) {
					testIds = new ArrayList<Integer>();
					mCategoryTests.put(testCases.getCategory(), testIds);
				}
				testIds.add(testCases.getId());
			}
		}
		if (mNoteModels == null) {
			mNoteModels = new HashMap<String, NoteModel>();
			for (NoteModel noteModel : noteModels) {
				mNoteModels.put(noteModel.getType(), noteModel);
			}
		}
	}

	/**
	 * Retrieves the UserTestCase Object for the Test IS
	 * 
	 * @param testId
	 * @return UserTestCase Object or Exception
	 */
	public NoteTestCase getTestCase(int testId) {
		NoteTestCase testCase = mNoteTestCases.get(testId);
		if (testCase != null)
			return testCase;
		throw new RuntimeException("Test Case Id is Invalid");
	}

	/**
	 * Retrieves the UserTestCase Object for the Test IS
	 * 
	 * @param type
	 *            Type of User Test being done. Need to be unique
	 * @return UserTestCase Object or Exception
	 */
	public NoteModel getNoteModel(String type) {
		NoteModel noteModel = mNoteModels.get(type);
		return noteModel;
	}

	/**
	 * This method return ArrayList of test ids for the Category
	 * 
	 * @param category
	 *            for the test cases
	 * @return ArrayList of test ids
	 */
	public ArrayList<Integer> getTestIds(String category) {
		return mCategoryTests.get(category);
	}
}
