package hu.mapro.jpa;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class JpaUtils {
	
	public static interface QuerySetup<T> {
		<R> void setup(
				AbstractQuery<R> query,
				Root<T> root
		);
	}
	
	public static interface CriteriaQuerySetup<T> {
		void setup(
				CriteriaBuilder criteriaBuilder,
				CriteriaQuery<T> query,
				Root<T> root
		);
	}
	
	public static <T> boolean isEmpty(FetchGraph<T> fetchGraph) {
		return 
				(fetchGraph.getManyToOne()==null || fetchGraph.getManyToOne().isEmpty())
				&&
				(fetchGraph.getOneToMany()==null || fetchGraph.getOneToMany().isEmpty());
	}
	
	public static <T> T fetchSingle(
			final EntityManager entityManager,
			final FetchGraph<T> fetchGraph,
			final Long id
	) {
		if (isEmpty(fetchGraph)) {
			return entityManager.find(fetchGraph.getEntityClass(), id);
		}
		
		return Iterables.getOnlyElement(list(entityManager, fetchGraph, new QuerySetup<T>() {
			@Override
			public <R> void setup(AbstractQuery<R> query, Root<T> root) {
				CriteriaBuilder cb = entityManager.getCriteriaBuilder();
				
				query.where(cb.equal(root.get("id"), id));
				
			}
		}), null);
	}
	
	public static <T> void fetchSingle(
			final EntityManager entityManager,
			final FetchGraph<T> fetchGraph,
			final T object
	) {
		
		Fetcher fetch = new Fetcher(entityManager);
		
		FetchContext<T> fetchContext = createFetchContext(fetchGraph, new QuerySetup<T>() {

			@Override
			public <R> void setup(AbstractQuery<R> query, Root<T> root) {
				CriteriaBuilder cb = entityManager.getCriteriaBuilder();
				query.where(cb.equal(root, object));
			}
		});
		
		if (fetchGraph.getManyToOne().isEmpty()) {
			fetch.addOneToManyFetch(fetchContext, fetchGraph);
		} else {
			fetch.addFetch(fetchContext, fetchGraph);
		}
	}	

	public static <T> List<T> list(
			final EntityManager entityManager,
			final FetchGraph<T> fetchGraph
	) {
		return list(entityManager, fetchGraph, new QuerySetup<T>() {
			@Override
			public <R> void setup(AbstractQuery<R> query, Root<T> root) {
			}
		});
	}

	public static <T> List<T> list(
			final EntityManager entityManager,
			final TransactionTemplate transactionTemplate,
			final FetchGraph<T> fetchGraph,
			final QuerySetup<T> querySetup
	) {
		return transactionTemplate.execute(new TransactionCallback<List<T>>() {
			@Override
			public List<T> doInTransaction(TransactionStatus status) {
				return list(entityManager, fetchGraph, querySetup);
			}
		});
	}
	
	public static <T> List<T> list(
			final EntityManager entityManager,
			final FetchGraph<T> fetchGraph,
			final QuerySetup<T> querySetup
	) {
		Fetcher fetch = new Fetcher(entityManager);
		
		return fetch.addFetch(createFetchContext(fetchGraph, querySetup), fetchGraph);
	}

	public static <T> List<T> list(
			final EntityManager entityManager,
			final FetchGraph<T> fetchGraph,
			final CriteriaQuerySetup<T> criteriaQuerySetup,
			Integer firstResult, 
			Integer maxResults
	) {
		Fetcher fetch = new Fetcher(entityManager);
		
		return fetch.addFetch(fetchGraph, criteriaQuerySetup, firstResult,  maxResults);
	}
	
	private static <T> FetchContext<T> createFetchContext(
			final FetchGraph<T> fetchGraph 
	) {
		return createFetchContext(fetchGraph, new QuerySetup<T>() {
			@Override
			public <R> void setup(AbstractQuery<R> query, Root<T> root) {
			}
		});
	}
	
	
	private static <T> FetchContext<T> createFetchContext(
			final FetchGraph<T> fetchGraph, 
			final QuerySetup<T> querySetup
	) {
		return new FetchContext<T>() {

			@Override
			public From<T, T> createQuery(AbstractQuery<?> parentQuery) {
				final Root<T> root = parentQuery.from(fetchGraph.getEntityClass());
				
				querySetup.setup(parentQuery, root);
				
				return root;
			}

			@Override
			public Class<T> getEntityClass() {
				return fetchGraph.getEntityClass();
			}

		};
	}	
	
	static interface FetchContext<T> {
		From<?, T> createQuery(AbstractQuery<?> query);
		Class<T> getEntityClass();
	}
	
	static class Fetcher {
		
		EntityManager entityManager;
		
		public Fetcher(EntityManager entityManager) {
			this.entityManager = entityManager;
		}

		private <T> void addOneToManyFetch(FetchContext<T> parent, FetchGraph<T> fetchGraph) {
			for (OneToManyFetch<T, ?> f : fetchGraph.getOneToMany()) {
				addOneToManyFetch(parent, f);
			}
			
			for (ManyToOneFetch<T, ?> f : fetchGraph.getManyToOne()) {
				addOneToManyFetch(parent, f);
			}
		}

		private <T, P> void addOneToManyFetch(final FetchContext<T> parent, final ManyToOneFetch<T, P> fetch) {
			addOneToManyFetch(new FetchContext<P>() {

				@Override
				public From<?, P> createQuery(AbstractQuery<?> query) {
					
					final From<?, T> pq = parent.createQuery(query);
					final From<T, P> from = pq.join(fetch.getProperty().getName(), JoinType.LEFT);
					
					return from;
				}

				@Override
				public Class<P> getEntityClass() {
					return fetch.getFetchGraph().getEntityClass();
				}
			}, fetch.getFetchGraph());
		}
		
		private <T, F> void addOneToManyFetch(
				final FetchContext<T> parent,
				final OneToManyFetch<T, F> f
		) {
			CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(parent.getEntityClass());
			From<?, T> pq = parent.createQuery(query);
			
			Fetch<T, F> subFetch = pq.fetch(f.getProperty().getName(), JoinType.LEFT);
			
			final FetchGraph<F> fg = f.getFetchGraph();
			
			if (f.isManyToOneDirect()) {
				addManyToOneFetch(subFetch, fg);
			}
			
			query.select(pq);
			
			TypedQuery<T> typedQuery = entityManager.createQuery(query);
			final List<T> records = typedQuery.getResultList();
			records.size(); // TODO ugly hack-like. any proper solution to initialize proxies?
			
			FetchContext<F> subParent = new FetchContext<F>() {
				
				private Set<T> set;

				@Override
				public From<?, F> createQuery(AbstractQuery<?> query) {
					Root<F> root = query.from(fg.getEntityClass());
					Join<F, T> parentJoin = root.join(f.getProperty().getInverse().getName(), JoinType.LEFT);

					switch (f.getFetchType()) {
					case SUBQUERY:
						Subquery<T> parentQuery = query.subquery(parent.getEntityClass());
						From<?, T> sq = parent.createQuery(parentQuery);
						parentQuery.select(sq);
						query.where(parentJoin.in(parentQuery));
						break;
					case PARAMS:
						if (set==null) {
							set = Sets.newHashSet();
							for (T value : records) {
								if (value!=null) {
									set.add(value);
								}
							}
						}
						query.where(parentJoin.in(set));
						break;
					}
					
					return root;
				}
				
				@Override
				public Class<F> getEntityClass() {
					return fg.getEntityClass();
				}
			};
			
			if (f.isManyToOneDirect() || fg.getManyToOne().isEmpty()) {
				addOneToManyFetch(subParent, f.getFetchGraph());
			} else {
				addFetch(subParent, f.getFetchGraph());
			}

			
		}

		private <T> List<T> addFetch(
				FetchGraph<T> fetchGraph,
				CriteriaQuerySetup<T> criteriaQuerySetup,
				Integer firstResult,
				Integer maxResults
		) {
			final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(fetchGraph.getEntityClass());
			
			Root<T> root = query.from(fetchGraph.getEntityClass());
			
			addManyToOneFetch(root, fetchGraph);
			
			criteriaQuerySetup.setup(builder, query, root);
			
			TypedQuery<T> typedQuery = entityManager.createQuery(query);
			
			if (firstResult!=null) {
				typedQuery.setFirstResult(firstResult);
			}
			
			if (maxResults!=null) {
				typedQuery.setMaxResults(maxResults);
			}
			
			List<T> records = typedQuery.getResultList();
			
			if (!records.isEmpty()) {
				addOneToManyFetch(createFetchContext(fetchGraph), fetchGraph);
			}
			
			return records;
		}
		
		private <T> List<T> addFetch(
				FetchContext<T> parent, 
				FetchGraph<T> fetchGraph
		) {
			final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<T> query = builder.createQuery(parent.getEntityClass());
			
			From<?, T> root = parent.createQuery(query);
			
			addManyToOneFetch(root, fetchGraph);

			TypedQuery<T> typedQuery = entityManager.createQuery(query);
			
			List<T> records = typedQuery.getResultList();

			if (!records.isEmpty()) {
				addOneToManyFetch(parent, fetchGraph);
			}
			
			return records;
		}
		
		private <T, F> void addManyToOneFetch(FetchParent<?, T> root, FetchGraph<T> fetchGraph) {
			for (ManyToOneFetch<T, ?> f : fetchGraph.getManyToOne()) {
				addManyToOneFetch(root, f);
			}
		}
		
		private <T, F> void addManyToOneFetch(FetchParent<?, T> root, ManyToOneFetch<T, F> fetch) {
			Fetch<T, F> f = root.fetch(fetch.getProperty().getName(), JoinType.LEFT);
			
			addManyToOneFetch(f, fetch.getFetchGraph());
		}
	}
	
	
	


	
}
