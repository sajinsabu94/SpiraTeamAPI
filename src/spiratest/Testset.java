package spiratest;

public class Testset {
	Long testSetId;
	String testSetName;
	String testSetFolder;
	String targetCycle;
	String testsetType;
	Long totalTCCount;
	public Testset(Long testSetId, String testSetName, String testSetFolder, String targetCycle, String testsetType,
			Long totalTCCount) {
		super();
		this.testSetId = testSetId;
		this.testSetName = testSetName;
		this.testSetFolder = testSetFolder;
		this.targetCycle = targetCycle;
		this.testsetType = testsetType;
		this.totalTCCount = totalTCCount;
	}
	public Long getTestSetId() {
		return testSetId;
	}
	public void setTestSetId(Long testSetId) {
		this.testSetId = testSetId;
	}
	public String getTestSetName() {
		return testSetName;
	}
	public void setTestSetName(String testSetName) {
		this.testSetName = testSetName;
	}
	public String getTestSetFolder() {
		return testSetFolder;
	}
	public void setTestSetFolder(String testSetFolder) {
		this.testSetFolder = testSetFolder;
	}
	public String getTargetCycle() {
		return targetCycle;
	}
	public void setTargetCycle(String targetCycle) {
		this.targetCycle = targetCycle;
	}
	public String getTestsetType() {
		return testsetType;
	}
	public void setTestsetType(String testsetType) {
		this.testsetType = testsetType;
	}
	public Long getTotalTCCount() {
		return totalTCCount;
	}
	public void setTotalTCCount(Long totalTCCount) {
		this.totalTCCount = totalTCCount;
	}
	
}
