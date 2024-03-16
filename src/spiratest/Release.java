package spiratest;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Release {
	Long relId;
	String relName;
	String startDate;
	public Release(Long relId, String relName, String startDate) {
		super();
		this.relId = relId;
		this.relName = relName;
		this.startDate = startDate;
	}
	public Long getRelId() {
		return relId;
	}
	public void setRelId(Long relId) {
		this.relId = relId;
	}
	public String getRelName() {
		return relName;
	}
	public void setRelName(String relName) {
		this.relName = relName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
}
