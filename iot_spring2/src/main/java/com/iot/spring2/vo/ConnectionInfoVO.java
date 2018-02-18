package com.iot.spring2.vo;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

public class ConnectionInfoVO {

	private int ciNo;
	private String ciName;
	private String ciUrl;
	private int ciPort;
	private String ciDatabase;
	private String ciUser;
	private String ciPwd;
	private String ciEtc;	
	private String uID;
	private String text;	
	private Object[] items;
	private String id;
	public int getCiNo() {
		return ciNo;
	}
	public void setCiNo(int ciNo) {
		this.ciNo = ciNo;
	}
	public String getCiName() {
		return ciName;
	}
	public void setCiName(String ciName) {
		this.ciName = ciName;
	}
	public String getCiUrl() {
		return ciUrl;
	}
	public void setCiUrl(String ciUrl) {
		this.ciUrl = ciUrl;
	}
	public int getCiPort() {
		return ciPort;
	}
	public void setCiPort(int ciPort) {
		this.ciPort = ciPort;
	}
	public String getCiDatabase() {
		return ciDatabase;
	}
	public void setCiDatabase(String ciDatabase) {
		this.ciDatabase = ciDatabase;
	}
	public String getCiUser() {
		return ciUser;
	}
	public void setCiUser(String ciUser) {
		this.ciUser = ciUser;
	}
	public String getCiPwd() {
		return ciPwd;
	}
	public void setCiPwd(String ciPwd) {
		this.ciPwd = ciPwd;
	}
	public String getCiEtc() {
		return ciEtc;
	}
	public void setCiEtc(String ciEtc) {
		this.ciEtc = ciEtc;
	}
	public String getuID() {
		return uID;
	}
	public void setuID(String uID) {
		this.uID = uID;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Object[] getItems() {
		return items;
	}
	public void setItems(Object[] items) {
		this.items = items;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "ConnectionInfoVO [ciNo=" + ciNo + ", ciName=" + ciName + ", ciUrl=" + ciUrl + ", ciPort=" + ciPort
				+ ", ciDatabase=" + ciDatabase + ", ciUser=" + ciUser + ", ciPwd=" + ciPwd + ", ciEtc=" + ciEtc
				+ ", uID=" + uID + ", text=" + text + ", items=" + Arrays.toString(items) + ", id=" + id + "]";
	}
	
	
	
}
