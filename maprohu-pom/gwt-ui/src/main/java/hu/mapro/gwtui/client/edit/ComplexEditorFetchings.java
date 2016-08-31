package hu.mapro.gwtui.client.edit;

import hu.mapro.jpa.model.domain.client.FetchPlanNavigator;
import hu.mapro.jpa.model.domain.client.FetchPlanNavigators;
import hu.mapro.model.meta.ComplexType;

public class ComplexEditorFetchings {

//	public static class SubComplexEditorFetching<T, S extends T> extends DelegatedEditorFieldsCollecting
//			implements ComplexEditorFetching<S> {
//		private final ComplexType<S> subType;
//		private final ComplexEditorFetching<T> fetching;
//
//		public SubComplexEditorFetching(
//				ComplexEditorFetching<T> fetching,
//				ComplexType<S> subType
//		) {
//			this.subType = subType;
//			this.fetching = fetching;
//		}
//
//		@Override
//		public void registerNavigator(FetchPlanNavigator<S> navigator) {
//			fetching.registerNavigator(FetchPlanNavigators.<T, S>subNavigator(navigator, subType));
//		}
//
//		@Override
//		protected EditorFieldsCollecting getDelegate() {
//			return fetching;
//		}
//	}
//
//	public static class SuperComplexEditorFetching<S, T extends S> extends DelegatedEditorFieldsCollecting implements
//			ComplexEditorFetching<S> {
//		private final ComplexEditorFetching<T> fetching;
//
//		public SuperComplexEditorFetching(ComplexEditorFetching<T> fetching) {
//			this.fetching = fetching;
//		}
//
//		@Override
//		public void registerNavigator(FetchPlanNavigator<S> navigator) {
//			fetching.registerNavigator(FetchPlanNavigators.<S, T>superNavigator(navigator));
//		}
//
//		@Override
//		protected EditorFieldsCollecting getDelegate() {
//			return fetching;
//		}
//	}
//
//	public static <S, T extends S> ComplexEditorFetching<S> superFetching(
//			final ComplexEditorFetching<T> fetching
//	) {
//		return new SuperComplexEditorFetching<S, T>(fetching);
//		
//	}
//
//	public static <T, S extends T> ComplexEditorFetching<S> subFetching(
//			final ComplexEditorFetching<T> fetching,
//			final ComplexType<S> subType
//	) {
//		return new SubComplexEditorFetching<T, S>(fetching, subType);
//	}
	
}
