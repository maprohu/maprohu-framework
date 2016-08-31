package hu.mapro.gwt.data.shared;

import com.google.gwt.http.client.RequestBuilder;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;

@Singleton
public class GwtDataRequestTransport extends DefaultRequestTransport /*implements SessionControl*/ {

	private static final PropertyTransportFactory propertyTransportFactory = new DefaultPropertyTransportFactory();
	
	Long windowId;
	
	public GwtDataRequestTransport(Long windowId) {
		super();
		this.windowId = windowId;
	}


	public enum Header {
		
		MAPROHU_WINDOW_ID,
//		MAPROHU_SESSION_COMMAND,
//		MAPROHU_SESSION_ID,
		
	}
	
//	public enum Command {
//		
//		NO_SESSION,
//		BEGIN_SESSION,
//		USE_SESSION,
//		DEFAULT_SESSION,
//		
//	}
	
//	interface Processor {
//		void headers(PropertyTransport builder);
//		//void send(String payload, TransportReceiver receiver);
//		RequestCallback createRequestCallback(TransportReceiver receiver);
//	}
//	
//	class NoProcessor implements Processor {
//		@Override
//		public void headers(PropertyTransport builder) {
//		}
////		@Override
////		public void send(String payload, TransportReceiver receiver) {
////			superSend(payload, receiver);
////		}
//
//		@Override
//		public RequestCallback createRequestCallback(TransportReceiver receiver) {
//			return superCreateRequestCallback(receiver);
//		}
//	}
//	
//	class SessionProcessor extends NoProcessor {
//		final Command command;
//		
//		public SessionProcessor(Command command) {
//			super();
//			this.command = command;
//		}
//		
//		@Override
//		public void headers(PropertyTransport builder) {
//			builder.setProperty(Header.MAPROHU_SESSION_COMMAND.name(), command.name());
//		}
//	}
//	class NoSessionProcessor extends SessionProcessor {
//		
//		public NoSessionProcessor() {
//			super(Command.NO_SESSION);
//		}
//		
//	}
//	class DefaultSessionProcessor extends SessionProcessor {
//		
//		public DefaultSessionProcessor() {
//			super(Command.DEFAULT_SESSION);
//		}
//		
//	}
//	class UseSessionProcessor extends SessionProcessor {
//		
//		final SessionId sessionId;
//
//		public UseSessionProcessor(SessionId sessionId) {
//			super(Command.USE_SESSION);
//			this.sessionId = sessionId;
//		}
//		
//		@Override
//		public void headers(PropertyTransport builder) {
//			super.headers(builder);
//			builder.setProperty(Header.MAPROHU_SESSION_ID.name(), sessionId.getSessionId().toString());
//		}
//		
//	}
//	class BeginSessionProcessor extends SessionProcessor {
//		final Callback<SessionId> sessionIdCallback;
//		
//		public BeginSessionProcessor(
//				Callback<SessionId> sessionIdCallback) {
//			super(Command.BEGIN_SESSION);
//			this.sessionIdCallback = sessionIdCallback;
//		}
//
//		@Override
//		public RequestCallback createRequestCallback(TransportReceiver receiver) {
//			final RequestCallback superCallback = superCreateRequestCallback(receiver);
//			
//			return new RequestCallback() {
//				
//				@Override
//				public void onResponseReceived(Request request, final Response response) {
//					final PropertyMessage message = propertyTransportFactory.parse(response.getText());
//					
//					sessionIdCallback.onResponse(SessionId.fromString(message.getValue(Header.MAPROHU_SESSION_ID.name())));
//					
//					superCallback.onResponseReceived(request, new Response() {
//						
//						@Override
//						public String getText() {
//							return message.getPayload();
//						}
//						
//						@Override
//						public String getStatusText() {
//							return response.getStatusText();
//						}
//						
//						@Override
//						public int getStatusCode() {
//							return response.getStatusCode();
//						}
//						
//						@Override
//						public String getHeadersAsString() {
//							return response.getHeadersAsString();
//						}
//						
//						@Override
//						public com.google.gwt.http.client.Header[] getHeaders() {
//							return response.getHeaders();
//						}
//						
//						@Override
//						public String getHeader(String header) {
//							return response.getHeader(header);
//						}
//					});
//				}
//				
//				@Override
//				public void onError(Request request, Throwable exception) {
//					superCallback.onError(request, exception);
//				}
//			};
//		}
//		
//	}
//
//	
//	private final Processor NO_PROCESSOR = new NoProcessor();
//	
//	Processor processor = NO_PROCESSOR;
	
	@Override
	protected RequestBuilder createRequestBuilder() {
		return new RequestBuilder(RequestBuilder.POST, getRequestUrl()) {
			@Override
			public void setRequestData(String requestData) {
				PropertyTransport transport = propertyTransportFactory.createTransport();
				transport.setProperty(Header.MAPROHU_WINDOW_ID.name(), windowId.toString());
				//processor.headers(transport);
				
				if (transport.hasValues()) {
				    setHeader("Content-Type", propertyTransportFactory.getContentType());
				    transport.setPayload(requestData);
				    super.setRequestData(transport.getMessage());
				} else {
					super.setRequestData(requestData);
				}
				
			}
		};
	}
	
	@Override
	protected void configureRequestBuilder(RequestBuilder builder) {
		super.configureRequestBuilder(builder);
		
		
	}
	
//	@Override
//	public void send(String payload, TransportReceiver receiver) {
//		processor.send(payload, receiver);
//	}

//	@Override
//	protected RequestCallback createRequestCallback(TransportReceiver receiver) {
//		return processor.createRequestCallback(receiver);
//	}
//
//	private RequestCallback superCreateRequestCallback(
//			TransportReceiver receiver) {
//		return super.createRequestCallback(receiver);
//	}
//	
////	private void superSend(String payload, TransportReceiver receiver) {
////		super.send(payload, receiver);
////	}
//	
//	@Override
//	public SessionFire useSession(SessionId sessionId) {
//		return new SessionFireImpl(new UseSessionProcessor(sessionId));
//	}
//
//	@Override
//	public SessionFire useDefaultSession() {
//		return new SessionFireImpl(new DefaultSessionProcessor());
//	}
//
//	@Override
//	public SessionFire beginSession(Callback<SessionId> newSessionId) {
//		return new SessionFireImpl(new BeginSessionProcessor(newSessionId));
//	}
//
//	@Override
//	public SessionFire noSession() {
//		return new SessionFireImpl(new NoSessionProcessor());
//	}
//	
//	
//	class SessionFireImpl implements SessionFire {
//
//		Processor processor;
//		
//		SessionFireImpl(Processor processor) {
//			super();
//			this.processor = processor;
//		}
//
//		private void fire(Action action) {
//			try {
//				GwtDataRequestTransport.this.processor = SessionFireImpl.this.processor;
//				
//				action.perform();
//			} finally {
//				GwtDataRequestTransport.this.processor = NO_PROCESSOR;
//			}
//		}
//		
//		@Override
//		public void fire(final RequestContext requestContext) {
//			fire(new Action() {
//				@Override
//				public void perform() {
//					requestContext.fire();
//				}
//			});
//		}
//
//		@Override
//		public void fire(final RequestContext requestContext, final Receiver<Void> receiver) {
//			fire(new Action() {
//				@Override
//				public void perform() {
//					requestContext.fire(receiver);
//				}
//			});
//		}
//
//		@Override
//		public <T> void fire(
//				final com.google.web.bindery.requestfactory.shared.Request<T> request) {
//			fire(new Action() {
//				@Override
//				public void perform() {
//					request.fire();
//				}
//			});
//		}
//
//		@Override
//		public <T> void fire(
//				final com.google.web.bindery.requestfactory.shared.Request<T> request,
//				final Receiver<T> receiver) {
//			fire(new Action() {
//				@Override
//				public void perform() {
//					request.fire(receiver);
//				}
//			});
//		}
//		
//	}
	
}