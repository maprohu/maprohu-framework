package com.google.web.bindery.requestfactory.server;

@SuppressWarnings("serial")
public class MaproReportableException extends ReportableException {

	public MaproReportableException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MaproReportableException(String msg) {
		super(msg);
	}

	public MaproReportableException(Throwable cause) {
		super(cause);
	}
	
}
