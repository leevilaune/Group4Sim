package fi.group4.project.datasource;

import jakarta.persistence.*;

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