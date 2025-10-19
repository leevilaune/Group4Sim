package fi.group4.project.datasource;

import jakarta.persistence.*;

/**
 * Singleton provider for JPA {@link EntityManager} connected to a MariaDB database.
 * <p>
 * This class manages the creation and lifecycle of a single {@link EntityManagerFactory}
 * and {@link EntityManager} instance to facilitate database operations using JPA.
 * It ensures that only one {@link EntityManager} is created and reused throughout the application.
 * <p>
 * Usage:
 * <pre>
 *     EntityManager em = MariaDbJpaConnection.getInstance();
 * </pre>
 * <p>
 * The {@link EntityManagerFactory} is created lazily upon the first request for an {@link EntityManager}.
 * If any exception occurs during the creation of the {@link EntityManager}, {@code null} is returned.
 * <p>
 * Note: This implementation is not thread-safe. If used in a multi-threaded environment,
 * additional synchronization may be necessary.
 */
public class MariaDbJpaConnection {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    public static EntityManager getInstance() {
        try{
            if (em==null) {
                if (emf==null) {
                    emf = Persistence.createEntityManagerFactory("CompanyMariaDbUnit");
                }
                em = emf.createEntityManager();
            }
        }catch (Exception e){
            return null;
        }
        return em;
    }
}