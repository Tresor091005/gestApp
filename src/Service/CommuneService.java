/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Entities.Commune;
import Helper.JPAUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class CommuneService {
    private EntityManager entityManager;

    public CommuneService() {
        this.entityManager = JPAUtil.getEntityManager();
    }
    
    public List<Commune> getAllCommunes() {
        try {
            return entityManager.createQuery("SELECT c FROM Commune c", Commune.class)
                     .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } 
    }
    
    public Commune getCommuneById(Integer communeId) {
        try {
            return entityManager.find(Commune.class, communeId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean addCommune(Commune commune) {
        // Start a transaction
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(commune);
            transaction.commit();
            return true;
        } catch (Exception e) {
            // If there is an error, rollback the transaction
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateCommune(Commune commune) {
        try {
            entityManager.getTransaction().begin();
            Commune existingCommune = entityManager.find(Commune.class, commune.getId());
            if (existingCommune != null) {
                existingCommune.setNom(commune.getNom());
                existingCommune.setDepartement(commune.getDepartement());
                entityManager.getTransaction().commit();
                return true;
            } else {
                entityManager.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }
    
    public boolean deleteCommuneById(Integer communeId) {
        try {
            entityManager.getTransaction().begin();
            Commune commune = entityManager.find(Commune.class, communeId);
            if (commune != null) {
                entityManager.remove(commune);
                entityManager.getTransaction().commit();
                return true;
            } else {
                entityManager.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }


    public void closeEntityManager() {
        this.entityManager.close();
    }
}
