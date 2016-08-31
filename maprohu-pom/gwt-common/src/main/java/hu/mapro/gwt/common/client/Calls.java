package hu.mapro.gwt.common.client;

import com.google.web.bindery.requestfactory.shared.Request;

public class Calls {

	public static <T> Call<T> of() {
		return new Call<T>();
	}
	
	public static <T> Call<T> of(final Request<T> request) {
		return new Call<T>() {
			{
				request.to(new AbstractReceiver<T>() {
					@Override
					public void onSuccess(T response) {
						onResponse(response);
					}
				});
			}
		};
	}
	
	public static <T> Call<T> ofInstance(T instance) {
		Call<T> call = of();
		call.onResponse(instance);
		return call;
	}
	
}
