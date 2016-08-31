package hu.mapro.gwt.common.client;

import com.google.web.bindery.requestfactory.shared.ServerFailure;

@SuppressWarnings("serial")
public class ServerFailureException extends RuntimeException {
	
	final ServerFailure serverFailure;

	public ServerFailureException(ServerFailure serverFailure) {
		super(serverFailure.getMessage());
		this.serverFailure = serverFailure;
	}

	public ServerFailure getServerFailure() {
		return serverFailure;
	}
	
}
