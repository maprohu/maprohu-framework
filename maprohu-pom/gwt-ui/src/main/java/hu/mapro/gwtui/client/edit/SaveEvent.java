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
package hu.mapro.gwtui.client.edit;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * A custom event test class.
 * 
 * @param <T>
 *            the type associated with the event
 */
public class SaveEvent extends GwtEvent<SaveEvent.SaveHandler> {

	/**
	 * The handler for the custom event.
	 */
	public interface SaveHandler extends EventHandler {
		void onSave(SaveEvent event);
	}

	public static Type<SaveHandler> type = new Type<SaveHandler>();

	@Override
	public Type<SaveHandler> getAssociatedType() {
		return (Type<SaveHandler>) type;
	}

	@Override
	protected void dispatch(SaveHandler handler) {
		handler.onSave(this);
	}


}
