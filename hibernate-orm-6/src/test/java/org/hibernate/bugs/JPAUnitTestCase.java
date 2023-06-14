package org.hibernate.bugs;

import entities.Module;
import entities.System;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	/**
	 *
	 * This is a unit test for the bug report https://discourse.hibernate.org/t/hibernate-6-npes-on-lazy-loaded-entities-in-collection/7661
	 */
	@Test
	public void npesOnLazyLoadedEntitiesTest() throws Exception {
		UUID idToLookFor = setUpEntities();

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();


		var q = entityManager.createQuery(
				"SELECT system FROM System system WHERE system.uuid = :uuid", System.class);
		q.setParameter("uuid", idToLookFor);
		var foundSystem =  q.getSingleResult();

		assertEquals(2, foundSystem.getModules().size());
		assertNotNull(foundSystem.getModules().get(0).getModuleUuid());
		assertNotNull(foundSystem.getModules().get(1).getModuleUuid());
		// Do stuff...
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public UUID setUpEntities(){
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		UUID idToLookFor = UUID.randomUUID();

		System firstSystem = new System();
		firstSystem.setUuid(idToLookFor);
		firstSystem.setVersion("v1");

		System secondSystem = new System();
		secondSystem.setUuid(UUID.randomUUID());
		secondSystem.setVersion("v1");

		entityManager.persist(firstSystem);
		entityManager.persist(secondSystem);

		Module firstModule = new Module();
		firstModule.setModuleUuid(UUID.randomUUID());
		firstModule.setVersion(1L);


		Module secondModule = new Module();
		secondModule.setModuleUuid(UUID.randomUUID());
		secondModule.setVersion(1L);

		entityManager.persist(secondModule);
		entityManager.persist(firstModule);

		firstSystem.addModule(firstModule);
		firstSystem.addModule(secondModule);


		// Do stuff...
		entityManager.getTransaction().commit();
		entityManager.close();

		return idToLookFor;
	}

}
