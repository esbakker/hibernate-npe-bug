package org.hibernate.bugs;

import entities.Module;
import entities.ParticipantTask;
import entities.Program;
import entities.Task;
import entities.UserInvite;
import entities.UserParticipant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
    }

    @After
    public void destroy() {
        entityManagerFactory.close();
    }

    /**
     * This is a unit test for the bug report https://discourse.hibernate.org/t/hibernate-6-npes-on-lazy-loaded-entities-in-collection/7661
     */
    @Test
    public void npesOnLazyLoadedEntitiesTest() throws Exception {
        var programToLookFor = setUpPrograms();
        var inviteToLookFor = setUpInvites(programToLookFor);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();


        var q = entityManager.createQuery(
                "select pt from Task pt " +
                        "left join ParticipantTask participantTask on participantTask.programTaskUuid = pt.uuid " +
                        "left join UserParticipant pp on participantTask.userParticipant = pp " +
                        "WHERE pp.userInvite = :userInvite " +
                        "AND pt.program = :program", Task.class);
        q.setParameter("program", programToLookFor);
        q.setParameter("userInvite", inviteToLookFor);
        var foundTasks = q.getResultList();

        assertEquals(1, foundTasks.size());

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Program setUpPrograms() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        UUID idToLookFor = UUID.randomUUID();

        var firstProgram = new Program();
        firstProgram.setProgramUuid(idToLookFor);
        firstProgram.setVersion(1L);

        var secondProgram = new Program();
        secondProgram.setProgramUuid(UUID.randomUUID());
        secondProgram.setVersion(1L);


        Module firstModule = new Module();
        firstModule.setModuleUuid(UUID.randomUUID());
        firstModule.setVersion(1L);


        Module secondModule = new Module();
        secondModule.setModuleUuid(UUID.randomUUID());
        secondModule.setVersion(1L);

        addModuleWithTask(firstProgram, firstModule, entityManager);
        addModuleWithTask(firstProgram, secondModule, entityManager);

        addModuleWithTask(secondProgram, firstModule, entityManager);

        entityManager.persist(firstProgram);
        entityManager.persist(secondProgram);

        entityManager.persist(secondModule);
        entityManager.persist(firstModule);

        entityManager.getTransaction().commit();
        entityManager.close();

        return firstProgram;
    }

    private void addModuleWithTask(Program firstProgram, Module firstModule, EntityManager entityManager) {
        firstProgram.addModule(firstModule);
        var task = new Task();
        task.setUuid(UUID.randomUUID());
        task.setProgram(firstProgram);
        task.setModule(firstModule);

        entityManager.persist(task);
    }

    public UserInvite setUpInvites(Program program) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        var invite = new UserInvite();
        invite.setProgram(program);

        var userParticipant = new UserParticipant();
        userParticipant.setProgram(program);
        userParticipant.setUserInvite(invite);

        var participantTask = new ParticipantTask();
        participantTask.setProgramTaskUuid(program.getTasks().get(0).getUuid());
        participantTask.setUuid(UUID.randomUUID());
        participantTask.setUserParticipant(userParticipant);

        entityManager.persist(invite);
        entityManager.persist(userParticipant);
        entityManager.persist(participantTask);

        entityManager.getTransaction().commit();
        entityManager.close();

        return invite;
    }

}
