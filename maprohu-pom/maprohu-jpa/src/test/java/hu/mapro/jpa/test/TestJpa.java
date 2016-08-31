package hu.mapro.jpa.test;

import hu.mapro.jpa.FetchGraph;
import hu.mapro.jpa.JpaUtils;
import hu.mapro.jpa.ManyToOneFetch;
import hu.mapro.jpa.ManyToOneProperty;
import hu.mapro.jpa.OneToManyFetch;
import hu.mapro.jpa.OneToManyFetchType;
import hu.mapro.jpa.OneToManyProperty;
import hu.mapro.jpa.test.model.EntityChild;
import hu.mapro.jpa.test.model.EntityChild2;
import hu.mapro.jpa.test.model.EntityGrandChild;
import hu.mapro.jpa.test.model.EntityReferenced;
import hu.mapro.jpa.test.model.EntityRoot;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;


public class TestJpa {
	
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("HsqldbWithTDD");		

	@Test
	public void test() {
		

		{
			EntityManager em;
			EntityTransaction tx;
			em = emf.createEntityManager();
			tx = em.getTransaction();
			
			tx.begin();
			
			EntityRoot r = new EntityRoot();
			r.setName("root");
			
			EntityReferenced ref = new EntityReferenced();
			ref.setName("ref");
			r.setEntityReferenced(ref);
			
			EntityChild ch = new EntityChild();
			ch.setName("child");
			ch.setParent(r);
			r.getChildren().add(ch);

			EntityGrandChild gch = new EntityGrandChild();
			gch.setName("grandchild");
			gch.setParent(ch);
			ch.getChildren().add(gch);
			
			EntityReferenced ref3 = new EntityReferenced();
			ref3.setName("ref3");
			ch.setEntityReferenced(ref3);
			
			EntityChild2 ch2 = new EntityChild2();
			ch2.setName("child2");
			ch2.setParent(ref);
			ref.getChildren().add(ch2);

			EntityReferenced ref2 = new EntityReferenced();
			ref2.setName("ref2");
			ch2.setEntityReferenced(ref2);
			
			
			em.persist(ref);
			em.persist(ref2);
			em.persist(ref3);
			em.persist(r);
			
			tx.commit();
			em.close();
		}

		runTest();
		
	}

	private void runTest() {
		EntityManager em;
		EntityTransaction tx;
		em = emf.createEntityManager();
		tx = em.getTransaction();
		
		tx.begin();
		
		List<EntityRoot> rl = JpaUtils.list(em, new FetchGraph<EntityRoot>() {

			@Override
			public Class<EntityRoot> getEntityClass() {
				return EntityRoot.class;
			}

			@Override
			public Collection<? extends ManyToOneFetch<EntityRoot, ?>> getManyToOne() {
				return Collections.singleton(new ManyToOneFetch<EntityRoot, EntityReferenced>() {

					@Override
					public ManyToOneProperty<EntityRoot, EntityReferenced> getProperty() {
						return new ManyToOneProperty<EntityRoot, EntityReferenced>() {
							@Override
							public String getName() {
								return "entityReferenced";
							}
						};
					}

					@Override
					public FetchGraph<EntityReferenced> getFetchGraph() {
						return new FetchGraph<EntityReferenced>() {

							@Override
							public Class<EntityReferenced> getEntityClass() {
								return EntityReferenced.class;
							}

							@Override
							public Collection<? extends ManyToOneFetch<EntityReferenced, ?>> getManyToOne() {
								return Collections.emptySet();
							}

							@Override
							public Collection<? extends OneToManyFetch<EntityReferenced, ?>> getOneToMany() {
								return Collections.singleton(new OneToManyFetch<EntityReferenced, EntityChild2>() {

									@Override
									public OneToManyProperty<EntityReferenced, EntityChild2> getProperty() {
										return new OneToManyProperty<EntityReferenced, EntityChild2>() {
											@Override
											public String getName() {
												return "children";
											}

											@Override
											public ManyToOneProperty<EntityChild2, EntityReferenced> getInverse() {
												return new ManyToOneProperty<EntityChild2, EntityReferenced>() {
													@Override
													public String getName() {
														return "parent";
													}
												};
											}
										};
									}

									@Override
									public FetchGraph<EntityChild2> getFetchGraph() {
										return new FetchGraph<EntityChild2>() {

											@Override
											public Class<EntityChild2> getEntityClass() {
												return EntityChild2.class;
											}

											@Override
											public Collection<? extends ManyToOneFetch<EntityChild2, ?>> getManyToOne() {
												return Collections.singleton(new ManyToOneFetch<EntityChild2, EntityReferenced>() {

													@Override
													public ManyToOneProperty<EntityChild2, EntityReferenced> getProperty() {
														return new ManyToOneProperty<EntityChild2, EntityReferenced>() {
															@Override
															public String getName() {
																return "entityReferenced";
															}
														};
													}

													@Override
													public FetchGraph<EntityReferenced> getFetchGraph() {
														return new FetchGraph<EntityReferenced>() {

															@Override
															public Class<EntityReferenced> getEntityClass() {
																return EntityReferenced.class;
															}

															@Override
															public Collection<? extends ManyToOneFetch<EntityReferenced, ?>> getManyToOne() {
																return Collections.emptyList();
															}

															@Override
															public Collection<? extends OneToManyFetch<EntityReferenced, ?>> getOneToMany() {
																return Collections.emptyList();
															}
														};
													}
												});
											}

											@Override
											public Collection<? extends OneToManyFetch<EntityChild2, ?>> getOneToMany() {
												return Collections.emptyList();
											}
										};
									}

									@Override
									public OneToManyFetchType getFetchType() {
										return OneToManyFetchType.SUBQUERY;
									}

									@Override
									public boolean isManyToOneDirect() {
										return false;
									}
									
								});
							}
						};
					}
				});
			}

			@Override
			public Collection<? extends OneToManyFetch<EntityRoot, ?>> getOneToMany() {
				return Collections.singleton(new OneToManyFetch<EntityRoot, EntityChild>() {

					@Override
					public OneToManyProperty<EntityRoot, EntityChild> getProperty() {
						return new OneToManyProperty<EntityRoot, EntityChild>() {

							@Override
							public String getName() {
								return "children";
							}

							@Override
							public ManyToOneProperty<EntityChild, EntityRoot> getInverse() {
								return new ManyToOneProperty<EntityChild, EntityRoot>() {
									@Override
									public String getName() {
										return "parent";
									}
								};
							}
						};
					}

					@Override
					public FetchGraph<EntityChild> getFetchGraph() {
						return new FetchGraph<EntityChild>() {

							@Override
							public Class<EntityChild> getEntityClass() {
								return EntityChild.class;
							}

							@Override
							public Collection<? extends ManyToOneFetch<EntityChild, ?>> getManyToOne() {
								return Collections.singleton(new ManyToOneFetch<EntityChild, EntityReferenced>() {

									@Override
									public ManyToOneProperty<EntityChild, EntityReferenced> getProperty() {
										return new ManyToOneProperty<EntityChild, EntityReferenced>() {
											@Override
											public String getName() {
												return "entityReferenced";
											}
										};
									}

									@Override
									public FetchGraph<EntityReferenced> getFetchGraph() {
										return new FetchGraph<EntityReferenced>() {

											@Override
											public Class<EntityReferenced> getEntityClass() {
												return EntityReferenced.class;
											}

											@Override
											public Collection<? extends ManyToOneFetch<EntityReferenced, ?>> getManyToOne() {
												return Collections.emptyList();
											}

											@Override
											public Collection<? extends OneToManyFetch<EntityReferenced, ?>> getOneToMany() {
												return Collections.emptyList();
											}
										};
									}
								});
							}

							@Override
							public Collection<? extends OneToManyFetch<EntityChild, ?>> getOneToMany() {
								return Collections.singletonList(new OneToManyFetch<EntityChild, EntityGrandChild>() {

									@Override
									public OneToManyProperty<EntityChild, EntityGrandChild> getProperty() {
										return new OneToManyProperty<EntityChild, EntityGrandChild>() {

											@Override
											public String getName() {
												return "children";
											}

											@Override
											public ManyToOneProperty<EntityGrandChild, EntityChild> getInverse() {
												return new ManyToOneProperty<EntityGrandChild, EntityChild>() {
													@Override
													public String getName() {
														return "parent";
													}
												};
											}
										};
									}

									@Override
									public FetchGraph<EntityGrandChild> getFetchGraph() {
										return new FetchGraph<EntityGrandChild>() {

											@Override
											public Class<EntityGrandChild> getEntityClass() {
												return EntityGrandChild.class;
											}

											@Override
											public Collection<? extends ManyToOneFetch<EntityGrandChild, ?>> getManyToOne() {
												return Collections.emptyList();
											}

											@Override
											public Collection<? extends OneToManyFetch<EntityGrandChild, ?>> getOneToMany() {
												return Collections.emptyList();
											}
										};
									}

									@Override
									public OneToManyFetchType getFetchType() {
										return OneToManyFetchType.SUBQUERY;
									}

									@Override
									public boolean isManyToOneDirect() {
										return false;
									}

								});
							}
						};
					}

					@Override
					public OneToManyFetchType getFetchType() {
						return OneToManyFetchType.SUBQUERY;
					}

					@Override
					public boolean isManyToOneDirect() {
						return true;
					}
				});
			}

		});
		
		tx.commit();
		em.close();
		
		EntityRoot entityRoot = rl.get(0);
		System.out.println(entityRoot.getName());
		EntityReferenced ref = entityRoot.getEntityReferenced();
		System.out.println(ref.getName());
		EntityChild child1 = entityRoot.getChildren().iterator().next();
		System.out.println(child1.getName());
		System.out.println(child1.getEntityReferenced().getName());
		System.out.println(child1.getChildren().iterator().next().getName());
		EntityChild2 child2 = ref.getChildren().iterator().next();
		System.out.println(child2.getName());
		EntityReferenced ref2 = child2.getEntityReferenced();
		System.out.println(ref2.getName());
	}
	
}
