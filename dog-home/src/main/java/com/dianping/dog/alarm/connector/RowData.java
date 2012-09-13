package com.dianping.dog.alarm.connector;

import java.util.HashMap;
import java.util.Map;

import com.dianping.dog.event.EventType;

public class RowData {

	private long timeStamp;

	private Map<String, Object> datas = new HashMap<String, Object>();

	public void addData(String key, Object data) {
		datas.put(key, data);
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(String key) {
		try {
			T data = (T) datas.get(key);
			return data;
		} catch (Exception ex) {
			return null;
		}
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public EventType getType() {
		String type = this.getData("report");
		if (type.equals("problem")) {
			return EventType.ProblemDataEvent;
		}
		return null;
	}

	public RowData copy() {
		RowData rowData = new RowData();
		rowData.setTimeStamp(timeStamp);
		for (Map.Entry<String, Object> data : datas.entrySet()) {
			rowData.addData(data.getKey(), data.getValue());
		}
		return rowData;
	}

}
