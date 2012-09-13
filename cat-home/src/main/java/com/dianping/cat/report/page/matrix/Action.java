package com.dianping.cat.report.page.matrix;

public enum Action implements com.site.web.mvc.Action {

	HISTORY_REPORT("history"),

	HOURLY_REPORT("view");

	public static Action getByName(String name, Action defaultAction) {
		for (Action action : Action.values()) {
			if (action.getName().equals(name)) {
				return action;
			}
		}

		return defaultAction;
	}

	private String m_name;

	private Action(String name) {
		m_name = name;
	}

	@Override
	public String getName() {
		return m_name;
	}
}
