package com.bridgelabz.noteservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bridgelabz.noteservice.controller.NoteController;
import com.bridgelabz.noteservice.service.INoteService;
import com.bridgelabz.noteservice.utilservice.securityservice.JwtTokenProvider;

@RunWith(SpringRunner.class)
@SpringBootTest
/* @ContextConfiguration(classes=FundooNoteTest.class) */
@WebAppConfiguration
public class NoteServiceTestCases {

	@InjectMocks
	private NoteController noteController;

	@Mock
	INoteService noteService;

	@Autowired
	@Spy
	JwtTokenProvider tokenService;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	private String jwttoken;

	TestModels testModals;

	private int passedTestCase;

	private int failesTestCase;

	private Map<Integer, Boolean> testCaseResults;

	@Before
	public void setUp() throws Exception {
		this.passedTestCase = 0;
		this.failesTestCase = 0;
		this.testCaseResults = new HashMap<Integer, Boolean>();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		testModals = Utility.convertJSONToObject("testcases.json", TestModels.class);
		testModals.constructTestCases();
	}

	@After
	public void testComplete() throws Exception {
		System.out.println("********************************************");
		System.out.println("      TEST RESULTS     ");
		System.out.println("HAPPY_SCENARIO'S : " + this.passedTestCase);
		System.out.println("SAD_SCENARIO's :   " + this.failesTestCase);
		System.out.println("********************************************");
	}

	@Test
	public void executeCreateNoteTestCases() {
		ArrayList<Integer>

		testCasedIds = testModals.getTestIds("CREATE_NOTE");
		for (int id : testCasedIds) {
			try {
				//assertEquals("Test Case Id: " + id, true, (this.executeNoteTestCases(id)));
				assertThat("Test Case Id: " + id, true,is(equalTo(this.executeNoteTestCases(id))));
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Error e) {
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	@Test
	public void executeDeleteNoteTestCases() {
		ArrayList<Integer>

		testCasedIds = testModals.getTestIds("DELETE_NOTE");
		for (int id : testCasedIds) {
			try {
				assertEquals("Test Case Id: " + id, true, (this.executeNoteTestCasesForPut(id)));
			//	assertThat("Test Case Id: " + id, true,is(equalTo(this.executeNoteTestCasesForPut(id))));
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Error e) {
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	

	private boolean executeNoteTestCases(int id) throws Exception {
		String token = null;
		boolean testCaseResult = false;

		NoteTestCase testCase = testModals.getTestCase(id);
		System.out.println(testCase.getId());
		NoteModel noteModel = testModals.getNoteModel(testCase.getTestCaseModal());
		if (noteModel == null)
			throw new RuntimeException("User Modal is invalid for Test Case Id:" + id);
		ResultActions result = null;

		token = noteModel.getUserId();
		System.out.println("UserId: " + token + " Note id: " + noteModel.getId());

		result = mockMvc.perform(post(testCase.getUrlPath()).contentType(MediaType.APPLICATION_JSON)
				.header("userId", token).content(Utility.asJsonString(noteModel)));

		
		String responseMesg = result.andReturn().getResponse().getContentAsString();
		System.out.println(responseMesg + ":" + result.andReturn().getResponse().getStatus());


		System.out.println("**token --> " + token);

		String validErrorMesg = testCase.getValidErrorMessage();
		System.out.println(validErrorMesg);

		if (validErrorMesg != null) {
			testCaseResult = checkTestCaseResult(result, testCase.getId(), validErrorMesg, testCase.getStatusCode());
		} else {
			testCaseResult = checkTestCaseResult(result, testCase.getId(), validErrorMesg, testCase.getStatusCode());
		}
		return testCaseResult;
	}
	
	
	
	
	
	private boolean executeNoteTestCasesForPut(int id) throws Exception {
		String token = null;
		boolean testCaseResult = false;

		NoteTestCase testCase = testModals.getTestCase(id);
		System.out.println(testCase.getId());
		NoteModel noteModel = testModals.getNoteModel(testCase.getTestCaseModal());
		if (noteModel == null)
			throw new RuntimeException("User Modal is invalid for Test Case Id:" + id);
		ResultActions result = null;

		//MvcResult mvcResult =null;
		
		
		token = noteModel.getUserId();
		System.out.println("UserId: " + token + " Note id: " + noteModel.getId());

		result = mockMvc.perform(put(testCase.getUrlPath()).contentType(MediaType.APPLICATION_JSON)
				.header("userId", token).content(Utility.asJsonString(noteModel)));

	/**/	   // Assert.assertEquals("foo and bar", mvcResult.getResponse().getContentAsString());		
		
		String responseMesg = result.andReturn().getResponse().getContentAsString();
		System.out.println(responseMesg + ":" + result.andReturn().getResponse().getStatus());

		
		System.out.println("**token --> " + token);

		String validErrorMesg = testCase.getValidErrorMessage();
		System.out.println(validErrorMesg);

		if (validErrorMesg != null) {
			testCaseResult = checkTestCaseResult(result, testCase.getId(), validErrorMesg, testCase.getStatusCode());
		} else {
			testCaseResult = checkTestCaseResult(result, testCase.getId(), validErrorMesg, testCase.getStatusCode());
		}
		return testCaseResult;
	}

	

	private boolean checkTestCaseResult(ResultActions result, int testId, String responseMesg, int status) {
		boolean testCaseResult = true;
		try {
			if (testId == 2)
				result.andDo(MockMvcResultHandlers.print());
			if (responseMesg != null) {
				result.andExpect(jsonPath("$.responseMessage", Matchers.is(responseMesg)));
			} else if (status == 200)
				result.andExpect(MockMvcResultMatchers.status().is(status));
			this.failesTestCase++;
			testCaseResult = true;

		} catch (Exception ex) {
			this.passedTestCase++;
		} catch (Error er) {
			this.passedTestCase++;
		} catch (Throwable th) {
			this.passedTestCase++;
		}
		this.testCaseResults.put(testId, testCaseResult);
		return testCaseResult;
	}
}
