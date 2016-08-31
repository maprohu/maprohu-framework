package hu.mapro.gwt.common.shared;

import hu.mapro.gwt.common.client.HandlerRegistrations;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.HandlerRegistration;

public class HandlerSupport<H> {
	
	final List<H> handlers = Lists.newArrayList();
	
	public HandlerRegistration addHandler(H handler) {
		return HandlerRegistrations.addItem(handlers, handler);
	}
	
	public void fire(Callback<H> event) {
		for (H handler : ImmutableList.copyOf(handlers)) {
			event.onResponse(handler);
		}
	}

}
