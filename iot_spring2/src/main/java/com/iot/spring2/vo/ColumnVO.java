package com.iot.spring2.vo;

public class ColumnVO {

	private String id;
	private String columnName;
	private String isNull;
	private String dataType;
	private String maxLength;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getIsNull() {
		return isNull;
	}
	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}
	@Override
	public String toString() {
		return "ColumnVO [id=" + id + ", columnName=" + columnName + ", isNull=" + isNull + ", dataType=" + dataType
				+ ", maxLength=" + maxLength + "]";
	}
	
	

}
