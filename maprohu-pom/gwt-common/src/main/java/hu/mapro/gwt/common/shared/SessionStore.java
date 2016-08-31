package hu.mapro.gwt.common.shared;


public interface SessionStore<S> extends Closeable {

	Long put(S session);

	S get(Long sessionId);

	S remove(Long sessionId);

}
