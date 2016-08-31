/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package hu.mapro.gwt.client.widget;

import hu.mapro.gwt.client.widget.CloseTabEvent.CloseTabHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * A custom event test class.
 * 
 * @param <T>
 *            the type associated with the event
 */
public class CloseTabEvent extends GwtEvent<CloseTabEvent.CloseTabHandler> {

	/**
	 * The handler for the custom event.
	 */
	public interface CloseTabHandler extends EventHandler {
		void onCloseTab(CloseTabEvent event);
	}

	public static Type<CloseTabHandler> type;

	static {
		type = new Type<CloseTabHandler>();
	}

	private CloseableTab tab;

	public CloseableTab getTab() {
		return tab;
	}

	@Override
	public Type<CloseTabHandler> getAssociatedType() {
		return (Type<CloseTabHandler>) type;
	}

	@Override
	protected void dispatch(CloseTabHandler handler) {
		handler.onCloseTab(this);
	}

	public CloseTabEvent(CloseableTab tab) {
		super();
		this.tab = tab;
	}

}
