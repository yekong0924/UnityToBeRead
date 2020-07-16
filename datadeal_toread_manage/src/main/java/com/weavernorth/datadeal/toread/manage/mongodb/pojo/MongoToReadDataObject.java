package com.weavernorth.datadeal.toread.manage.mongodb.pojo;

import java.io.Serializable;
import java.util.Map;
// 如果要使用dubbo 发布分布式调用 需要实现implements Serializable 默认已实现
public class MongoToReadDataObject implements Serializable {
	/**
	 * 待阅流程数据实体信息
	 * @param mapData
	 * 	{
	 * 		"dataID":"1asdfasdf43223423411",
	 * 		"dateType":"Ofs_toRead",
	 * 		"sysCode":"0001",
	 * 		"flowId":"A00000000007",
	 * 		"sysShortName":"OA系统",
	 * 		"sysFullName":"泛微OA系统",
	 * 		"requestName":">&#x5458;&#x5DE5;&#x8BF7;&#x5047;&#x6D41;&#x7A0B;_0001_-1_A00000000007_luffy",
	 *		"workflowName":"&#x5458;&#x5DE5;&#x8BF7;&#x5047;&#x6D41;&#x7A0B;",
	 * 		"nodeName":"&#x67E5;&#x770B;&#x8282;&#x70B9;",
	 * 		"pcUrl":"/showtask.aspx?id=A00000000007",
	 * 		"appUrl":"/showtask.aspx?id=A00000000007",
	 * 		"creator":"wld",
	 * 		"createDateTime":"2019-3-10 12:00:00",
	 * 		"receiver":"luffy",
	 * 	 	"isRemark":"0",
	 * 		"receiveDateTime":"2019-3-10 12:00:00"
	 * 		"operateDateTime":"2019-3-10 12:00:00"
	 * 	}
	 * @return
	 */

	// 数据ID（数据产生时创建，与存储中的id不同）
	private String dataID;
	//数据类型(可暂时不适用)
	private String dateType;
	//系统标识
	private String sysCode;
	//系统简称
	private String sysShortName;
	//系统全称
	private String sysFullName;
	//第三方数据id
	private String flowId;
	//流程名称
	private String requestName;
	//流程类型名称
	private String workflowName;
	//节点名称
	private String nodeName;
	//pcurl
	private String pcUrl;
	//appurl
	private String appUrl;
	//创建人
	private String creator;
	//创建时间
	private String createDateTime;
	//接收人
	private String receiver;
	//接收时间
	private String receiveDateTime;
	//数据状态 0:待阅 2：已阅 9:删除
	private String isRemark;
	//操作时间
	private String operateDateTime;
	//mongodb数据库主键
	private String _id;

	public String getDataID() {
		return dataID;
	}

	public void setDataID(String dataID) {
		this.dataID = dataID;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getSysShortName() {
		return sysShortName;
	}

	public void setSysShortName(String sysShortName) {
		this.sysShortName = sysShortName;
	}

	public String getSysFullName() {
		return sysFullName;
	}

	public void setSysFullName(String sysFullName) {
		this.sysFullName = sysFullName;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getPcUrl() {
		return pcUrl;
	}

	public void setPcUrl(String pcUrl) {
		this.pcUrl = pcUrl;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiveDateTime() {
		return receiveDateTime;
	}

	public void setReceiveDateTime(String receiveDateTime) {
		this.receiveDateTime = receiveDateTime;
	}

	public String getIsRemark() {
		return isRemark;
	}

	public void setIsRemark(String isRemark) {
		this.isRemark = isRemark;
	}

	public String getOperateDateTime() {
		return operateDateTime;
	}

	public void setOperateDateTime(String operateDateTime) {
		this.operateDateTime = operateDateTime;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	@Override
	public String toString() {
		return "MongoToReadDataObject{" +
				"dataID='" + dataID + '\'' +
				", dateType='" + dateType + '\'' +
				", sysCode='" + sysCode + '\'' +
				", sysShortName='" + sysShortName + '\'' +
				", sysFullName='" + sysFullName + '\'' +
				", flowId='" + flowId + '\'' +
				", requestName='" + requestName + '\'' +
				", workflowName='" + workflowName + '\'' +
				", nodeName='" + nodeName + '\'' +
				", pcUrl='" + pcUrl + '\'' +
				", appUrl='" + appUrl + '\'' +
				", creator='" + creator + '\'' +
				", createDateTime='" + createDateTime + '\'' +
				", receiver='" + receiver + '\'' +
				", receiveDateTime='" + receiveDateTime + '\'' +
				", isRemark='" + isRemark + '\'' +
				", operateDateTime='" + operateDateTime + '\'' +
				", _id='" + _id + '\'' +
				'}';
	}
}
