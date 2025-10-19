package fi.group4.project.dao;

import fi.group4.project.datasource.MariaDbJpaConnection;
import fi.group4.project.entity.SimulationRun;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SimulationRunDao {

    public void persist(SimulationRun curr) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if(em == null){
            return;
        }
        em.getTransaction().begin();
        em.persist(curr);
        em.getTransaction().commit();
    }

    public SimulationRun find(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if(em == null){
            return null;
        }
        return em.find(SimulationRun.class, id);
    }

    public List<SimulationRun> findAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if(em == null){
            return null;
        }
        List<SimulationRun> runs = em.createQuery("SELECT c FROM SimulationRun c", SimulationRun.class).getResultList();
        return runs ;
    }

    public void update(SimulationRun curr) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if(em == null){
            return;
        }
        em.getTransaction().begin();
        em.merge(curr);
        em.getTransaction().commit();
    }

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
