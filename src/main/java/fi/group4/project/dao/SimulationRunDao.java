package fi.group4.project.dao;

import fi.group4.project.datasource.MariaDbJpaConnection;
import fi.group4.project.entity.SimulationRun;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data Access Object (DAO) class for managing SimulationRun entities.
 * Provides methods to persist, find, update, and delete SimulationRun records
 * using JPA EntityManager connected to a MariaDB database.
 */
public class SimulationRunDao {

    /**
     * Persists a new SimulationRun entity to the database.
     * Begins a transaction, persists the entity, and commits the transaction.
     *
     * @param curr the SimulationRun entity to be persisted
     */
    public void persist(SimulationRun curr) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(curr);
        em.getTransaction().commit();
    }

    /**
     * Finds and returns a SimulationRun entity by its primary key ID.
     * Returns null if the EntityManager is not available or the entity is not found.
     *
     * @param id the primary key ID of the SimulationRun to find
     * @return the SimulationRun entity with the specified ID, or null if not found
     */
    public SimulationRun find(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if(em == null){
            return null;
        }
        return em.find(SimulationRun.class, id);
    }

    /**
     * Retrieves all SimulationRun entities from the database.
     * Returns null if the EntityManager is not available.
     *
     * @return a List of all SimulationRun entities, or null if EntityManager is unavailable
     */
    public List<SimulationRun> findAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if(em == null){
            return null;
        }
        List<SimulationRun> runs = em.createQuery("SELECT c FROM SimulationRun c", SimulationRun.class).getResultList();
        return runs ;
    }

    /**
     * Updates an existing SimulationRun entity in the database.
     * Begins a transaction, merges the entity, and commits the transaction.
     * Does nothing if the EntityManager is not available.
     *
     * @param curr the SimulationRun entity to be updated
     */
    public void update(SimulationRun curr) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if(em == null){
            return;
        }
        em.getTransaction().begin();
        em.merge(curr);
        em.getTransaction().commit();
    }

    /**
     * Deletes a SimulationRun entity from the database.
     * Begins a transaction, removes the entity, and commits the transaction.
     * Does nothing if the EntityManager is not available.
     *
     * @param curr the SimulationRun entity to be deleted
     */
    public void delete(SimulationRun curr) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if(em == null){
            return;
        }
        em.getTransaction().begin();
        em.remove(curr);
        em.getTransaction().commit();
    }
}
