package spiratest;

public class Requirement{
	Long reqId;	
	String reqName;
	String reqDesc;
	String reqStatus;
	String reqPriority;
	Long reqRelId;
	public Requirement(Long reqId, String reqName, String reqDesc, String reqStatus, String reqPriority,
			Long reqRelId) {
		super();
		this.reqId = reqId;
		this.reqName = reqName;
		this.reqDesc = reqDesc;
		this.reqStatus = reqStatus;
		this.reqPriority = reqPriority;
		this.reqRelId = reqRelId;
	}
	public Long getReqId() {
		return reqId;
	}
	public void setReqId(Long reqId) {
		this.reqId = reqId;
	}
	public String getReqName() {
		return reqName;
	}
	public void setReqName(String reqName) {
		this.reqName = reqName;
	}
	public String getReqDesc() {
		return reqDesc;
	}
	public void setReqDesc(String reqDesc) {
		this.reqDesc = reqDesc;
	}
	public String getReqStatus() {
		return reqStatus;
	}
	public void setReqStatus(String reqStatus) {
		this.reqStatus = reqStatus;
	}
	public String getReqPriority() {
		return reqPriority;
	}
	public void setReqPriority(String reqPriority) {
		this.reqPriority = reqPriority;
	}
	public Long getReqRelId() {
		return reqRelId;
	}
	public void setReqRelId(Long reqRelId) {
		this.reqRelId = reqRelId;
	}	
}

