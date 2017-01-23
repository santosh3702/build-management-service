package com.build.model;

public class BuildServiceResponse {
	
	
	private String status;
	private String log;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	@Override
	public String toString() {
		return "BuildServiceResponse [status=" + status + ", log=" + log + ", getStatus()=" + getStatus()
				+ ", getLog()=" + getLog() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
}
