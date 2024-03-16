package spiratest;


import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.swing.text.DateFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RESTServiceClient {

	private static final String SPIRA_URL = "https://demo-eu.spiraservice.net/xxxxxxxx/Services/v6_0/RestService.svc";

	private static final Map<String, String> connectionParameters = new HashMap<String, String>();


	public static SpiraTeamAPI spiraApi;

	public static void main(String[] args) {
		connectionParameters.put("Content-Type", "application/json");
		connectionParameters.put("Accept", "application/json");
		connectionParameters.put("username", "administrator");
		connectionParameters.put("api-key", "{xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx}");

		try {

			JSONArray allProjects = spiraApi.getAllProjects(SPIRA_URL, "GET", connectionParameters); 

			String projectName = "Sample API Project";
			String appName = "Application 1";

			String appPropertyName = "Application";
			String appListName = "Application List";

			Map<String, String> projectProperties = getProjectId(allProjects, projectName);
			//System.out.println(projectProperties);

			//JSONObject project = spiraApi.getProject(SPIRA_URL, "GET", projectId.intValue(), connectionParameters); 
			//System.out.println(project);

			int projectTemplateId = Integer.parseInt(projectProperties.get("ProjectTemplateId"));
			int projectId = Integer.parseInt(projectProperties.get("ProjectId"));

			JSONArray customList = spiraApi.getAllCustomListOfProjectTemplate(SPIRA_URL, "GET", projectTemplateId, connectionParameters);
			Long appListId = getApplicationListId(customList, appListName);
			//System.out.println(appListId);

			JSONObject apps = spiraApi.getAllApplications(SPIRA_URL, "GET", projectTemplateId, appListId.intValue(), connectionParameters);
			//System.out.println(apps);

			Map<String, Long> appIdMapping = getAppIdMapping(apps);
			Long appIdSpira = appIdMapping.get(appName);
			//System.out.println(appIdSpira);



			JSONArray releaseList = spiraApi.getAllReleasesOfProject(SPIRA_URL, "GET", projectId, connectionParameters);
			Long propertyId = getAppPropertyId(releaseList, appPropertyName);
			//System.out.println(propertyId);
			List<Release> releases =  getFilteredReleaseDetails(releaseList, appIdSpira, propertyId.intValue());			
			List<Long> releaseIDs = getReleaseID(releases);


			Long reqCount = spiraApi.getAllRequirementsCountOfProject(SPIRA_URL, "GET", projectId, connectionParameters); 
			//System.out.println(reqCount);

			JSONArray allReqofProjects = spiraApi.getAllRequirementsOfProject(SPIRA_URL, "GET", projectId, 1, reqCount.intValue(), connectionParameters); 
			propertyId = getAppPropertyId(allReqofProjects, appPropertyName);
			//System.out.println(propertyId);			
			List<Requirement> requirements = getFilteredRequirementDetails(allReqofProjects, appIdSpira, propertyId.intValue());

			
			
			
			JSONArray foldersList = spiraApi.getAllTestsetFoldersOfProject(SPIRA_URL, "GET", projectId, connectionParameters);
			//System.out.println(foldersList);
			Map<Long, String> folderDetails =  getFilteredFolderList(foldersList);		
			
			Long testsetCount = spiraApi.getAllTestsetsCountOfProject(SPIRA_URL, "GET", projectId, connectionParameters); 
			//System.out.println(testsetCount);

			JSONArray allTestset = spiraApi.getAllTestsetOfProject(SPIRA_URL, "GET", projectId, 1, testsetCount.intValue(), connectionParameters); 
			propertyId = getAppPropertyId(allTestset, appPropertyName);
			//System.out.println(allTestset);
				
			List<Testset> testSets = getFilteredTestSets(allTestset, appIdSpira, propertyId.intValue(), folderDetails);
			//System.out.println(testSets);
			
			

			JSONArray componentsList = spiraApi.getAllComponentsOfProject(SPIRA_URL, "GET", projectId, connectionParameters);
			Map<Long, String> componentDetails = getFilteredComponentDetails(componentsList);
			Long testcaseCount = spiraApi.getAllTestcaseCountOfProject(SPIRA_URL, "GET", projectId, connectionParameters); 
			//System.out.println(testcaseCount);

			JSONArray allTestCases = spiraApi.getAllTestcaseOfProject(SPIRA_URL, "GET", projectId, 1, testcaseCount.intValue(), connectionParameters); 
			//System.out.println(allTestCases);
			propertyId = getAppPropertyId(allTestCases, appPropertyName);
			List<Testcase> testCases = getFilteredTestCases(allTestCases, projectId, appIdSpira, propertyId.intValue(), componentDetails, appPropertyName);
			//for(Testcase tc : testCases) {
			//	System.out.println(tc);
			//}

			
			Long testExecCount = spiraApi.getAllTestExecutionCountOfProject(SPIRA_URL, "GET", projectId, connectionParameters); 
			//System.out.println(testExecCount);
			JSONArray allTestExec = spiraApi.getAllTestExecutionOfProject(SPIRA_URL, "GET", projectId, 1, testExecCount.intValue(), connectionParameters); 
			//System.out.println(allTestExec);
			propertyId = getAppPropertyId(allTestCases, appPropertyName);
			List<TestExecution> testExecution = getFilteredTestExecution(allTestExec, appIdSpira, propertyId);
			
			/*
			JSONArray allProjects = spiraApi.getAllDefectsOfProject(SPIRA_URL, "GET", 1, connectionParameters);
			for(int i=0; i<allProjects.size(); i++){ 
				JSONObject eachProject = (JSONObject) allProjects.get(i);
				System.out.println(eachProject); 
			}
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<TestExecution> getFilteredTestExecution(JSONArray allTestExec, Long appIdSpira, Long propertyId) {
		List<TestExecution> listTestExec = new ArrayList<TestExecution>();
		Map<Long, String> testExectionStatuses = getTestExecutionStatuses();
		
		JSONParser parser = new JSONParser();
		for(int i=0; i<allTestExec.size(); i++){
			JSONObject eachTestExec = (JSONObject) allTestExec.get(i);
			//System.out.println(eachReq);

			JSONArray customProperties = (JSONArray) parser.parse(eachTestExec.get("CustomProperties").toString());

			for(int j=0; j<customProperties.size(); j++) {
				JSONObject eachProperty = (JSONObject) customProperties.get(j);
				Long propNo = (Long)eachProperty.get("PropertyNumber");
				if(propNo.intValue() == propertyId) {
					Long appIdCheck = (Long)eachProperty.get("IntegerValue");
					if(appIdCheck.intValue() == appIdSpira.intValue()) {
						//System.out.println(eachReq);
						Long testExecId = (Long) eachTestExec.get("TestRunId");
						String testCaseId = (String) eachTestExec.get("TestCaseId");
						String testSetId = (String) eachTestExec.get("TestSetId");
						String testExeStatus = getTestExecutionStatus((Long)eachTestExec.get("ExecutionStatusId"));
						String testExePriority = "Medium";
						Long relTestExecId = (Long) eachTestExec.get("ReleaseId");
						
						
						String reqStatus = (String) eachReq.get("StatusName");
						String reqPriority = (String) eachReq.get("ImportanceName");
						Long reqRelId = (Long) eachReq.get("ReleaseId");

						Requirement req = new Requirement(reqId, reqName, reqDesc, reqStatus, reqPriority, reqRelId);
						listRequirement.add(req);
					}
				}
			}

		}
		return listRequirement;
	}

	private static String getTestExecutionStatus(Long execId) {
		Map<Long, String> testExecutionStatuses = new HashMap<Long, String>();
		//Failed = 1; Passed = 2; NotRun = 3; NotApplicable = 4; Blocked = 5; Caution = 6
		testExecutionStatuses.put(1L, "Failed");
		testExecutionStatuses.put(2L, "Passed");
		testExecutionStatuses.put(3L, "NotRun");
		testExecutionStatuses.put(4L, "NotApplicable");
		testExecutionStatuses.put(5L, "Blocked");
		testExecutionStatuses.put(6L, "Caution");		
		
		return testExecutionStatuses.get(execId);
	}

	private static Map<Long, String> getFilteredComponentDetails(JSONArray componentsList) {
		Map<Long, String> componentDetails = new HashMap<Long, String>();
		
		for(int i=0; i<componentsList.size(); i++) {
			JSONObject eachComp = (JSONObject) componentsList.get(i);
			componentDetails.put((Long) eachComp.get("ComponentId"), eachComp.get("Name").toString());
		}
		return componentDetails;
	}

	private static List<Testcase> getFilteredTestCases(JSONArray allTestCases, int projectId, Long appIdSpira, int propertyId, Map<Long, String> componentDetails, String appPropertyName) throws IOException, URISyntaxException, ParseException {
		List<Testcase> listTestCase = new ArrayList<Testcase>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		/**********Requirement Fetch Code**********/
		Long reqCount = spiraApi.getAllRequirementsCountOfProject(SPIRA_URL, "GET", projectId, connectionParameters); 
		JSONArray allReqofProjects = spiraApi.getAllRequirementsOfProject(SPIRA_URL, "GET", projectId, 1, reqCount.intValue(), connectionParameters); 
		Long reqPropertyId = getAppPropertyId(allReqofProjects, appPropertyName);
		List<Requirement> requirements = getFilteredRequirementDetails(allReqofProjects, appIdSpira, reqPropertyId.intValue());
		/******************************************/
		
		Map<Long, List<Long>> reqTestcaseMapping = getReqTestcaseMapping(projectId, requirements);
		Map<Long, List<Long>> testcaseReqMapping = getTestCaseReqMapping(reqTestcaseMapping);
		//System.out.println(testcaseReqMapping);
		
		/**********Testset Fetch*******************/	
		JSONArray foldersList = spiraApi.getAllTestsetFoldersOfProject(SPIRA_URL, "GET", projectId, connectionParameters);
		Map<Long, String> folderDetails =  getFilteredFolderList(foldersList);			
		Long testsetCount = spiraApi.getAllTestsetsCountOfProject(SPIRA_URL, "GET", projectId, connectionParameters); 
		JSONArray allTestset = spiraApi.getAllTestsetOfProject(SPIRA_URL, "GET", projectId, 1, testsetCount.intValue(), connectionParameters); 
		Long tsPropsId = getAppPropertyId(allTestset, appPropertyName);
		List<Testset> testSets = getFilteredTestSets(allTestset, appIdSpira, tsPropsId.intValue(), folderDetails);
		/******************************************/

		Map<Long, List<Long>> testSetTestCaseMapping = getTestsetTestCaseMapping(projectId, testSets);
		Map<Long, List<Long>> testcaseSetMapping = getTestCaseTestsetMapping(testSetTestCaseMapping);
		//System.out.println(testcaseSetMapping);

		JSONParser parser = new JSONParser();
		for(int i=0; i<allTestCases.size(); i++){
			JSONObject eachTestcase = (JSONObject) allTestCases.get(i);
			//System.out.println(eachTestset);
			
			JSONArray customProperties = (JSONArray) parser.parse(eachTestcase.get("CustomProperties").toString());

			for(int j=0; j<customProperties.size(); j++) {
				JSONObject eachProperty = (JSONObject) customProperties.get(j);
				Long propNo = (Long)eachProperty.get("PropertyNumber");
				if(propNo.intValue() == propertyId) {					
					Long appIdCheck = (Long)eachProperty.get("IntegerValue");
					if(appIdCheck.intValue() == appIdSpira.intValue()) {
						Long testCaseId = (Long) eachTestcase.get("TestCaseId");
						String testCaseName = (String) eachTestcase.get("Name");
						String createdUnDate = (String) eachTestcase.get("CreationDate");
						String createdDate = formatter.format(Date.from(Instant.parse(createdUnDate)));						
						String testCaseType = (String) eachTestcase.get("TestCaseTypeName");
						String testCaseSummary = (String) eachTestcase.get("Description");
						String testCaseDescription = (String) eachTestcase.get("Description");
						String testSteps = getTestStepsOfTestcase(projectId, testCaseId.intValue());					
						String createdBy = (String) eachTestcase.get("AuthorName");						
						String testCasePriority = (String) eachTestcase.get("TestCasePriorityName");
						String testCaseStatus = (String) eachTestcase.get("TestCaseStatusName");
						String testCaseModule = "";
						if(componentDetails.size()>0) {
							List<Long> moduleList =  (List<Long>) eachTestcase.get("ComponentIds");
							testCaseModule = componentDetails.get(moduleList.get(moduleList.size()-1));
						}
						String comments = getTestCaseComments(projectId, testCaseId.intValue());
						
						Long linkedReq = null;
						if(testcaseReqMapping.get(testCaseId) != null && testcaseReqMapping.get(testCaseId).size()>0) {
							List<Long> sortedList = testcaseReqMapping.get(testCaseId).stream().sorted().collect(Collectors.toList());
							linkedReq = sortedList.get(0);
						}
						
						Long linkedTestset = null;
						if(testcaseSetMapping.get(testCaseId) != null && testcaseSetMapping.get(testCaseId).size()>0) {
							List<Long> sortedList = testcaseSetMapping.get(testCaseId).stream().sorted().collect(Collectors.toList());
							linkedTestset = sortedList.get(0);
						}			
						
						Testcase testcase = new Testcase(testCaseId, testCaseName, createdDate, testCaseType, testCaseSummary, testCaseDescription, testSteps, createdBy, testCasePriority, testCaseStatus, testCaseModule, comments, linkedReq, linkedTestset);
						listTestCase.add(testcase);
					}
				}
			}			
		}		
		return listTestCase;
	}

	private static Map<Long, List<Long>> getTestCaseTestsetMapping(Map<Long, List<Long>> testSetTestCaseMapping) {
		Map<Long, List<Long>> testcaseSetMap = new HashMap<Long, List<Long>>();
		for(Long reqId : testSetTestCaseMapping.keySet()) {
			List<Long> testCaseList = testSetTestCaseMapping.get(reqId);
			for(Long testCaseId : testCaseList) {
				if(testcaseSetMap.get(testCaseId)!=null) {
					testcaseSetMap.get(testCaseId).add(reqId);
				}
				else {
					List<Long> tsTempList = new ArrayList<Long>();
					tsTempList.add(reqId);
					testcaseSetMap.put(testCaseId, tsTempList);
				}
			}
		}
		return testcaseSetMap;
	}

	private static Map<Long, List<Long>> getTestsetTestCaseMapping(int projectId, List<Testset> testSets) throws IOException, URISyntaxException, ParseException {
		Map<Long, List<Long>> testSetTestCaseMapping = new HashMap<Long, List<Long>>();
		for(Testset tes : testSets) {
			JSONArray allTestcases = spiraApi.getAllTestcasesOfTestSet(SPIRA_URL, "GET", projectId, tes.getTestSetId(), connectionParameters);
			List<Long> testCases = new ArrayList<Long>();
			if(allTestcases != null && allTestcases.size()>0) {
				for(int i=0; i<allTestcases.size(); i++){
					JSONObject eachTestObj = (JSONObject) allTestcases.get(i);
					testCases.add((Long)eachTestObj.get("TestCaseId"));
				}
				testSetTestCaseMapping.put(tes.getTestSetId(), testCases);
			}
		}
		return testSetTestCaseMapping;
	}

	private static Map<Long, List<Long>> getTestCaseReqMapping(Map<Long, List<Long>> reqTestcaseMapping) {
		Map<Long, List<Long>> testcaseReqMap = new HashMap<Long, List<Long>>();
		for(Long reqId : reqTestcaseMapping.keySet()) {
			List<Long> testCaseList = reqTestcaseMapping.get(reqId);
			for(Long testCaseId : testCaseList) {
				if(testcaseReqMap.get(testCaseId)!=null) {
					testcaseReqMap.get(testCaseId).add(reqId);
				}
				else {
					List<Long> reqTempList = new ArrayList<Long>();
					reqTempList.add(reqId);
					testcaseReqMap.put(testCaseId, reqTempList);
				}
			}
		}
		return testcaseReqMap;
	}

	private static Map<Long, List<Long>> getReqTestcaseMapping(int projectId, List<Requirement> requirements) throws IOException, URISyntaxException, ParseException {
		Map<Long, List<Long>> reqTestcaseMapping = new HashMap<Long, List<Long>>();
		for(Requirement req : requirements) {
			JSONArray allTestcases = spiraApi.getAllTestcasesOfReq(SPIRA_URL, "GET", projectId, req.getReqId(), connectionParameters);
			List<Long> testCases = new ArrayList<Long>();
			if(allTestcases != null && allTestcases.size()>0) {
				for(int i=0; i<allTestcases.size(); i++){
					JSONObject eachTestObj = (JSONObject) allTestcases.get(i);
					testCases.add((Long)eachTestObj.get("TestCaseId"));
				}
				reqTestcaseMapping.put(req.getReqId(), testCases);
			}
		}
		return reqTestcaseMapping;
	}

	private static String getTestCaseComments(int projectId, int testCaseId) throws IOException, URISyntaxException, ParseException {
		JSONArray allComments = spiraApi.getAllCommentsOfTestCase(SPIRA_URL, "GET", projectId, testCaseId, connectionParameters);
		String testStep = "";
		for(int i=0; i<allComments.size(); i++){
			JSONObject eachComment = (JSONObject) allComments.get(i);
			testStep = testStep + (String) eachComment.get("UserName")+" : "+(String)eachComment.get("Text")+"\n";
		}
		return testStep;
	}

	private static String getTestStepsOfTestcase(int projectId, int testCaseId) throws IOException, URISyntaxException, ParseException {
		JSONArray allTestSteps = spiraApi.getAllTestStepsOfTestCase(SPIRA_URL, "GET", projectId, testCaseId, connectionParameters);
		String testStep = "";
		for(int i=0; i<allTestSteps.size(); i++){
			JSONObject eachStep = (JSONObject) allTestSteps.get(i);
			testStep = testStep + (String)eachStep.get("Description")+"\n";
		}
		return testStep;
	}

	private static Map<Long, String> getFilteredFolderList(JSONArray foldersList) {
		Map<Long, String> folderDetails = new HashMap<Long, String>();
		for(int i=0; i<foldersList.size(); i++) {
			JSONObject eachFolder = (JSONObject) foldersList.get(i);
			folderDetails.put((Long) eachFolder.get("TestSetFolderId"), eachFolder.get("Name").toString());
		}
		return folderDetails;
	}

	private static List<Testset> getFilteredTestSets(JSONArray allTestset, Long appIdSpira, int propertyId, Map<Long, String> folderDetails) throws ParseException {
		List<Testset> listTestset = new ArrayList<Testset>();

		JSONParser parser = new JSONParser();
		for(int i=0; i<allTestset.size(); i++){
			JSONObject eachTestset = (JSONObject) allTestset.get(i);
			//System.out.println(eachTestset);
			
			JSONArray customProperties = (JSONArray) parser.parse(eachTestset.get("CustomProperties").toString());

			for(int j=0; j<customProperties.size(); j++) {
				JSONObject eachProperty = (JSONObject) customProperties.get(j);
				Long propNo = (Long)eachProperty.get("PropertyNumber");
				if(propNo.intValue() == propertyId) {					
					Long appIdCheck = (Long)eachProperty.get("IntegerValue");
					if(appIdCheck.intValue() == appIdSpira.intValue()) {
						Long testSetId = (Long) eachTestset.get("TestSetId");
						String testSetName = (String) eachTestset.get("Name");
						String testSetFolder = null;
						String targetCycle = null;
						String testsetType = "Regression Testing";
						if((Long) eachTestset.get("TestSetFolderId") != null) {
							testSetFolder = folderDetails.get((Long) eachTestset.get("TestSetFolderId"));
							targetCycle = folderDetails.get((Long) eachTestset.get("TestSetFolderId"));
							testsetType = getTestingType(testSetFolder);
						}
						Long countNR = (Long) eachTestset.get("CountNotRun");
						Long countPass = (Long) eachTestset.get("CountPassed");
						Long countFail = (Long) eachTestset.get("CountFailed");
						Long countBlocked = (Long) eachTestset.get("CountBlocked");
						Long countCaution = (Long) eachTestset.get("CountCaution");
						Long countNA = (Long) eachTestset.get("CountFailed");
						
						Long totalTCCount = countNR+countPass+countFail+countBlocked+countCaution+countNA;							
						
						Testset testset = new Testset(testSetId, testSetName, testSetFolder, targetCycle, testsetType, totalTCCount);
						listTestset.add(testset);
					}
				}
			}			
		}		
		return listTestset;
	}

	private static List<Long> getReleaseID(List<Release> releases) {
		List<Long> relIDs = new ArrayList<Long>();
		for(int i=0; i<releases.size(); i++){
			Long relId =  releases.get(i).getRelId();
			//System.out.println(eachApp);
			relIDs.add(relId);				
		}
		return relIDs;
	}

	private static List<Requirement> getFilteredRequirementDetails(JSONArray allReqofProjects, Long appIdSpira,
			int propertyId) throws ParseException {
		List<Requirement> listRequirement = new ArrayList<Requirement>();

		JSONParser parser = new JSONParser();
		for(int i=0; i<allReqofProjects.size(); i++){
			JSONObject eachReq = (JSONObject) allReqofProjects.get(i);
			//System.out.println(eachReq);

			JSONArray customProperties = (JSONArray) parser.parse(eachReq.get("CustomProperties").toString());

			for(int j=0; j<customProperties.size(); j++) {
				JSONObject eachProperty = (JSONObject) customProperties.get(j);
				Long propNo = (Long)eachProperty.get("PropertyNumber");
				if(propNo.intValue() == propertyId) {
					Long appIdCheck = (Long)eachProperty.get("IntegerValue");
					if(appIdCheck.intValue() == appIdSpira.intValue()) {
						//System.out.println(eachReq);						

						Long reqId = (Long) eachReq.get("RequirementId");
						String reqName = (String) eachReq.get("Name");
						String reqDesc = (String) eachReq.get("Description"); 
						String reqStatus = (String) eachReq.get("StatusName");
						String reqPriority = (String) eachReq.get("ImportanceName");
						Long reqRelId = (Long) eachReq.get("ReleaseId");

						Requirement req = new Requirement(reqId, reqName, reqDesc, reqStatus, reqPriority, reqRelId);
						listRequirement.add(req);
					}
				}
			}

		}
		return listRequirement;
	}

	private static Long getAppPropertyId(JSONArray releaseList, String appPropertyName) throws ParseException {
		Long propertyID = 0L;

		JSONParser parser = new JSONParser();
		JSONObject singleRelease = (JSONObject) releaseList.get(0);
		//System.out.println(singleRelease);
		JSONArray customProperties = (JSONArray) parser.parse(singleRelease.get("CustomProperties").toString());
		//System.out.println(customProperties);
		//System.out.println(customProperties.size());
		for(int i=0; i<customProperties.size(); i++) {
			JSONObject eachProperty = (JSONObject) customProperties.get(i);
			//System.out.println(eachProperty);
			JSONObject defVal = (JSONObject) parser.parse(eachProperty.get("Definition").toString());
			if(defVal.get("Name").toString().equals(appPropertyName)) {
				propertyID = (Long) eachProperty.get("PropertyNumber");
				//System.out.println(eachProperty.get("PropertyNumber"));
				break;
			}

		}
		return propertyID;
	}

	private static Map<String, Long> getAppIdMapping(JSONObject apps) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONArray appDetails = (JSONArray) parser.parse(apps.get("Values").toString());

		Map<String, Long> appIdMapping = new HashMap<String, Long>();		
		for(int i=0; i<appDetails.size(); i++){
			JSONObject eachApp = (JSONObject) appDetails.get(i);
			//System.out.println(eachApp);
			appIdMapping.put((eachApp.get("Name")).toString(), (Long) eachApp.get("CustomPropertyValueId"));				
		}
		return appIdMapping;
	}

	private static Long getApplicationListId(JSONArray customList, String appListName) {
		for(int i=0; i<customList.size(); i++){
			JSONObject eachList = (JSONObject) customList.get(i);
			//System.out.println(eachList);
			String projectName = (String) eachList.get("Name");
			if(projectName.equals(appListName)) {
				return (Long) eachList.get("CustomPropertyListId");
			}
		}
		return 0L;
	}

	public static Map<String, String> getProjectId(JSONArray allProjects, String pName) throws ParseException {
		Map<String, String> projectProperties = new HashMap<String, String>();		
		for(int i=0; i<allProjects.size(); i++){
			JSONObject eachProject = (JSONObject) allProjects.get(i);
			//System.out.println(eachProject);
			String projectName = (String)eachProject.get("Name");
			if(projectName.equals(pName)) {
				projectProperties.put("ProjectId", ((Long)eachProject.get("ProjectId")).toString());
				projectProperties.put("ProjectTemplateId", ((Long)eachProject.get("ProjectTemplateId")).toString());				
				break;
			}
		}
		return projectProperties;
	}

	public static List<Release> getFilteredReleaseDetails(JSONArray releaseInfo, Long appIdSpira, int propertyId) throws IOException, ParseException, URISyntaxException, java.text.ParseException {

		List<Release> listRelease = new ArrayList<Release>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		JSONParser parser = new JSONParser();
		for(int i=0; i<releaseInfo.size(); i++){
			JSONObject eachRelease = (JSONObject) releaseInfo.get(i);
			//System.out.println(eachRelease);

			JSONArray customProperties = (JSONArray) parser.parse(eachRelease.get("CustomProperties").toString());
			for(int j=0; j<customProperties.size(); j++) {
				JSONObject eachProperty = (JSONObject) customProperties.get(j);
				Long propNo = (Long)eachProperty.get("PropertyNumber");
				if(propNo.intValue() == propertyId) {
					Long appIdCheck = (Long)eachProperty.get("IntegerValue");
					if(appIdCheck.intValue() == appIdSpira.intValue()) {
						Long relId = (Long) eachRelease.get("ReleaseId");
						String relName = (String) eachRelease.get("Name");
						String startUnDate = (String) eachRelease.get("StartDate");
						String startDate = formatter.format(Date.from(Instant.parse(startUnDate)));

						Release release = new Release(relId, relName, startDate);
						listRelease.add(release);
						//System.out.println(String.join(" ",relId.toString(), relName,formattedDate));
					}
				}
				//System.out.println(eachProperty);
			}
		}
		return listRelease;
	}

	public static void getRequirementDetails() {

	}

	public static void getTestsetDetails() {

	}

	public static void getTestcaseDetails() {

	}

	public static void getTestexecutionDetails() {

	}

	public static void getDefectDetails() {

	}

	public static void getReleaseData() {

	}
	public static String getTestingType(String testingType) {
		String testType = "Regression Testing";
		if(testingType.toLowerCase().contains("unit test")){
			testType = "Unit Testing";
		}
		if(testingType.toLowerCase().contains("integration")){
			testType = "Integration Testing";
		}
		if(testingType.toLowerCase().contains("system test")){
			testType = "System Testing";
		}
		if(testingType.toLowerCase().contains("smoke")){
			testType = "Smoke Testing";
		}
		if(testingType.toLowerCase().contains("sanity")){
			testType = "Sanity Testing";
		}
		if(testingType.toLowerCase().contains("interface test")){
			testType = "Interface Testing";
		}
		if(testingType.toLowerCase().contains("regression")){
			testType = "Regression Testing";
		}
		if(testingType.toLowerCase().contains("acceptance")){
			testType = "Acceptance Testing";
		}
		if(testingType.toLowerCase().contains("beta test")){
			testType = "Beta Testing";
		}
		if(testingType.toLowerCase().contains("functional")){
			testType = "Functional Testing";
		}
		return testType;
	}
}

