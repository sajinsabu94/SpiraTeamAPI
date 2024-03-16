package spiratest;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Testcase {
	Long testCaseId;
	String testCaseName;
	String createdDate;						
	String testCaseType;
	String testCaseSummary;
	String testCaseDescription;
	String testSteps;					
	String createdBy;						
	String testCasePriority;
	String testCaseStatus;
	String testCaseModule;
	String comments;	
	Long linkedReq;	
	Long linkedTestset;
	public Testcase(Long testCaseId, String testCaseName, String createdDate, String testCaseType,
			String testCaseSummary, String testCaseDescription, String testSteps, String createdBy,
			String testCasePriority, String testCaseStatus, String testCaseModule, String comments, Long linkedReq,
			Long linkedTestset) {
		super();
		this.testCaseId = testCaseId;
		this.testCaseName = testCaseName;
		this.createdDate = createdDate;
		this.testCaseType = testCaseType;
		this.testCaseSummary = testCaseSummary;
		this.testCaseDescription = testCaseDescription;
		this.testSteps = testSteps;
		this.createdBy = createdBy;
		this.testCasePriority = testCasePriority;
		this.testCaseStatus = testCaseStatus;
		this.testCaseModule = testCaseModule;
		this.comments = comments;
		this.linkedReq = linkedReq;
		this.linkedTestset = linkedTestset;
	}
	public Long getTestCaseId() {
		return testCaseId;
	}
	public void setTestCaseId(Long testCaseId) {
		this.testCaseId = testCaseId;
	}
	public String getTestCaseName() {
		return testCaseName;
	}
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getTestCaseType() {
		return testCaseType;
	}
	public void setTestCaseType(String testCaseType) {
		this.testCaseType = testCaseType;
	}
	public String getTestCaseSummary() {
		return testCaseSummary;
	}
	public void setTestCaseSummary(String testCaseSummary) {
		this.testCaseSummary = testCaseSummary;
	}
	public String getTestCaseDescription() {
		return testCaseDescription;
	}
	public void setTestCaseDescription(String testCaseDescription) {
		this.testCaseDescription = testCaseDescription;
	}
	public String getTestSteps() {
		return testSteps;
	}
	public void setTestSteps(String testSteps) {
		this.testSteps = testSteps;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getTestCasePriority() {
		return testCasePriority;
	}
	public void setTestCasePriority(String testCasePriority) {
		this.testCasePriority = testCasePriority;
	}
	public String getTestCaseStatus() {
		return testCaseStatus;
	}
	public void setTestCaseStatus(String testCaseStatus) {
		this.testCaseStatus = testCaseStatus;
	}
	public String getTestCaseModule() {
		return testCaseModule;
	}
	public void setTestCaseModule(String testCaseModule) {
		this.testCaseModule = testCaseModule;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Long getLinkedReq() {
		return linkedReq;
	}
	public void setLinkedReq(Long linkedReq) {
		this.linkedReq = linkedReq;
	}
	public Long getLinkedTestset() {
		return linkedTestset;
	}
	public void setLinkedTestset(Long linkedTestset) {
		this.linkedTestset = linkedTestset;
	}
	@Override
	public String toString() {
		return "Testcase [testCaseId=" + testCaseId + ", testCaseName=" + testCaseName + ", createdDate=" + createdDate
				+ ", testCaseType=" + testCaseType + ", testCaseSummary=" + testCaseSummary + ", testCaseDescription="
				+ testCaseDescription + ", testSteps=" + testSteps + ", createdBy=" + createdBy + ", testCasePriority="
				+ testCasePriority + ", testCaseStatus=" + testCaseStatus + ", testCaseModule=" + testCaseModule
				+ ", comments=" + comments + ", linkedReq=" + linkedReq + ", linkedTestset=" + linkedTestset + "]";
	}
	
}
