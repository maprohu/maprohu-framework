package hu.mapro.gwtui.client.grid;

import java.util.List;

import hu.mapro.gwt.data.client.ListResult;
import hu.mapro.gwt.data.client.UncachedClientStore;
import hu.mapro.jpa.model.domain.client.ListConfigBuilder;
import hu.mapro.jpa.model.domain.client.ListConfigBuilders;
import hu.mapro.jpa.model.domain.client.Sorting;

import com.google.common.collect.ImmutableList;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class Pagings {

	public static <T> Paging<T> from(final UncachedClientStore<T> rc, final ListConfigBuilder... builders) {
		return new Paging<T>() {

			@Override
			public void load(int offset, int limit, List<Sorting<T>> sorting,
					Receiver<ListResult<T>> receiver) {
				rc.listCount(
						ListConfigBuilders.multi(
								ImmutableList.<ListConfigBuilder>builder().add(
										builders
								).add(
										ListConfigBuilders.paging(offset, limit, sorting)
								).build()
						),
						receiver
				);
				
			}

		};
	}
	
}
