package spiratest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SpiraTeamAPI {

	private static String connectionURl;
	private static Map<String, String> connParameters;
	private static String connMethod;

	public static Map<Long, String> getAllProjects() {

		return null;
	}

	public static String createQueryParam(String baseUrl, Map<String, String> queryParams)
			throws UnsupportedEncodingException {

		ArrayList<String> queryValues = new ArrayList<String>();
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			queryValues.add(entry.getKey() + "=" + entry.getValue());
		}

		String joinedValues = String.join("&", queryValues);
		// joinedValues = URLEncoder.encode(joinedValues, "UTF-8");
		baseUrl = baseUrl + "?" + joinedValues;

		return baseUrl;
	}

	private static HttpsURLConnection createConnection() throws IOException, URISyntaxException {
		URI uri = new URI(connectionURl);
		System.out.println(uri.toURL());

		HttpsURLConnection connection = (HttpsURLConnection) uri.toURL().openConnection();
		connection.setRequestMethod(connMethod);
		for (Map.Entry<String, String> eachParam : connParameters.entrySet()) {
			connection.setRequestProperty(eachParam.getKey(), eachParam.getValue());
		}

		return connection;
	}

	public static JSONArray getJsonArray(HttpsURLConnection connection) throws IOException, ParseException {
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray) parser.parse(response.toString());

		return jsonArray;
	}

	private static JSONObject getJsonData(HttpsURLConnection connection) throws IOException, ParseException {
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(response.toString());

		return jsonObject;
	}

	public static Long getLongData(HttpsURLConnection connection) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		Long longData = Long.parseLong(response.toString());

		return longData;
	}

	public static JSONArray getAllProjects(String baseUrl, String agentMethod, Map<String, String> connectionParameters)
			throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects");

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		JSONArray projectData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			projectData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return projectData;
	}

	public static JSONObject getProject(String baseUrl, String agentMethod, int projectId,
			Map<String, String> connectionParameters) throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId));

//	connectionParameters.put("project_id", String.valueOf(projectId));

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		JSONObject projectData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			projectData = getJsonData(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return projectData;
	}

	public static JSONArray getAllReleasesOfProject(String baseUrl, String agentMethod, int projectId,
			Map<String, String> connectionParameters) throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "releases");

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		JSONArray releaseData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			releaseData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		connection.disconnect();
		return releaseData;
	}

	public static JSONObject getReleaseDetails(String baseUrl, String agentMethod, int projectId, int relId,
			Map<String, String> connectionParameters) throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "releases",
				String.valueOf(relId));

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		JSONObject releaseData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			releaseData = getJsonData(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return releaseData;
	}

	public static Long getAllRequirementsCountOfProject(String baseUrl, String agentMethod, int projectId,
			Map<String, String> connectionParameters) throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "requirements",
				"count");

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		Long requirementCount = 0L;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			requirementCount = getLongData(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return requirementCount;
	}

	public static JSONArray getAllRequirementsOfProject(String baseUrl, String agentMethod, int projectId,
			int startIndex, int count, Map<String, String> connectionParameters)
			throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "requirements");

		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("starting_row", String.valueOf(startIndex));
		queryParams.put("number_of_rows", String.valueOf(count));
		projectUrlString = createQueryParam(projectUrlString, queryParams);

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		JSONArray projectData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			projectData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return projectData;
	}

	public static Long getAllTestsetsCountOfProject(String baseUrl, String agentMethod, int projectId,
			Map<String, String> connectionParameters) throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "test-sets",
				"count");

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		Long requirementCount = 0L;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			requirementCount = getLongData(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return requirementCount;
	}

	public static JSONArray getAllTestsetOfProject(String baseUrl, String agentMethod, int projectId, int startIndex,
			int count, Map<String, String> connectionParameters)
			throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "test-sets");

		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("starting_row", String.valueOf(startIndex));
		queryParams.put("number_of_rows", String.valueOf(count));
		queryParams.put("sort_field", "TestSetId");
		queryParams.put("sort_direction", "ASC");

		projectUrlString = createQueryParam(projectUrlString, queryParams);

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		JSONArray projectData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			projectData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return projectData;
	}

	public static Long getAllTestcaseCountOfProject(String baseUrl, String agentMethod, int projectId,
			Map<String, String> connectionParameters) throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "test-cases",
				"count");

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		Long requirementCount = 0L;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			requirementCount = getLongData(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return requirementCount;
	}

	public static JSONArray getAllTestcaseOfProject(String baseUrl, String agentMethod, int projectId, int startIndex,
			int count, Map<String, String> connectionParameters)
			throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "test-cases");

		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("starting_row", String.valueOf(startIndex));
		queryParams.put("number_of_rows", String.valueOf(count));
		queryParams.put("sort_field", "TestSetId");
		queryParams.put("sort_direction", "ASC");

		projectUrlString = createQueryParam(projectUrlString, queryParams);

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		JSONArray projectData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			projectData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return projectData;
	}

	public static Long getAllTestExecutionCountOfProject(String baseUrl, String agentMethod, int projectId,
			Map<String, String> connectionParameters) throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "test-runs",
				"count");

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		Long requirementCount = 0L;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			requirementCount = getLongData(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return requirementCount;
	}

	public static JSONArray getAllTestExecutionOfProject(String baseUrl, String agentMethod, int projectId,
			int startIndex, int count, Map<String, String> connectionParameters)
			throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "test-runs");

		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("starting_row", String.valueOf(startIndex));
		queryParams.put("number_of_rows", String.valueOf(count));
		queryParams.put("sort_field", "TestRunId");
		queryParams.put("sort_direction", "ASC");
		projectUrlString = createQueryParam(projectUrlString, queryParams);

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		JSONArray projectData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			projectData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return projectData;
	}

	public static JSONArray getAllDefectsOfProject(String baseUrl, String agentMethod, int projectId,
			Map<String, String> connectionParameters) throws IOException, ParseException, URISyntaxException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "incidents");

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		JSONArray releaseData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			releaseData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return releaseData;
	}

	public static JSONArray getAllCustomListOfProjectTemplate(String baseUrl, String agentMethod, int projectTemplateId,
			Map<String, String> connectionParameters) throws IOException, URISyntaxException, ParseException {

		String projectUrlString = String.join("/", baseUrl, "project-templates", String.valueOf(projectTemplateId),
				"custom-lists");
		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		JSONArray customListValues = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			customListValues = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return customListValues;
	}

	public static void getAllApplicationID() {

	}

	public static JSONObject getAllApplications(String baseUrl, String agentMethod, int projectTemplateId,
			int appListId, Map<String, String> connectionparameters)
			throws IOException, URISyntaxException, ParseException {

		String projectUrlString = String.join("/", baseUrl, "project-templates", String.valueOf(projectTemplateId),
				"custom-lists", String.valueOf(appListId));
		connectionURl = projectUrlString;
		connParameters = connectionparameters;
		connMethod = agentMethod;

		JSONObject customListValues = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			customListValues = getJsonData(connection);
		} else {
			System.out.println("GET request not worked");
		}
		return customListValues;
	}

	public static JSONArray getAllTestsetFoldersOfProject(String baseUrl, String agentMethod, int projectId,
			Map<String, String> connectionparameters) throws IOException, URISyntaxException, ParseException {
		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "test-set-folders");

		connectionURl = projectUrlString;
		connParameters = connectionparameters;
		connMethod = agentMethod;

		JSONArray folderData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			folderData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		connection.disconnect();
		return folderData;
	}

	public static JSONArray getAllTestStepsOfTestCase(String baseUrl, String agentMethod, int projectId, int testCaseId,
			Map<String, String> connectionparameters) throws IOException, URISyntaxException, ParseException {
		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "test-cases",
				String.valueOf(testCaseId), "test-steps");

		connectionURl = projectUrlString;
		connParameters = connectionparameters;
		connMethod = agentMethod;

		JSONArray folderData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			folderData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		connection.disconnect();
		return folderData;

	}

	public static JSONArray getAllComponentsOfProject(String baseUrl, String agentMethod, int projectId,
			Map<String, String> connectionparameters) throws IOException, URISyntaxException, ParseException {
		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "components");

		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("active_only", "false");
		queryParams.put("include_deleted", "false");
		projectUrlString = createQueryParam(projectUrlString, queryParams);

		connectionURl = projectUrlString;
		connParameters = connectionparameters;
		connMethod = agentMethod;

		JSONArray componentData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			componentData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		connection.disconnect();
		return componentData;
	}

	public static JSONArray getAllCommentsOfTestCase(String baseUrl, String agentMethod, int projectId, int testCaseId,
			Map<String, String> connectionparameters) throws IOException, URISyntaxException, ParseException {
		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "test-cases",
				String.valueOf(testCaseId), "comments");

		connectionURl = projectUrlString;
		connParameters = connectionparameters;
		connMethod = agentMethod;

		JSONArray commentData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			commentData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		connection.disconnect();
		return commentData;
	}

	public static JSONArray getAllTestcasesOfReq(String baseUrl, String agentMethod, int projectId, Long reqId,
			Map<String, String> connectionparameters) throws IOException, URISyntaxException, ParseException {

		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "requirements",
				String.valueOf(reqId), "test-cases");

		connectionURl = projectUrlString;
		connParameters = connectionparameters;
		connMethod = agentMethod;

		JSONArray mappedData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			mappedData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		connection.disconnect();
		return mappedData;
	}

	public static JSONArray getAllTestcasesOfTestSet(String baseUrl, String agentMethod, int projectId, Long testSetId,
			Map<String, String> connectionParameters) throws IOException, URISyntaxException, ParseException {
		String projectUrlString = String.join("/", baseUrl, "projects", String.valueOf(projectId), "test-sets",
				String.valueOf(testSetId), "test-cases");

		connectionURl = projectUrlString;
		connParameters = connectionParameters;
		connMethod = agentMethod;

		JSONArray mappedData = null;

		HttpsURLConnection connection = createConnection();

		int responseCode = connection.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			mappedData = getJsonArray(connection);
		} else {
			System.out.println("GET request not worked");
		}
		connection.disconnect();
		return mappedData;
	}
}
