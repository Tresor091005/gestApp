/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Entities.Departement;
import Helper.JPAUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public class DepartementService {
    private EntityManager entityManager;

    public DepartementService() {
        this.entityManager = JPAUtil.getEntityManager();
    }
    
    public List<Departement> getAllDepartements() {
        try {
            return entityManager.createQuery("SELECT d FROM Departement d", Departement.class)
                     .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } 
    }
    
    public Departement getDepartementById(Integer departementId) {
        try {
            return entityManager.find(Departement.class, departementId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean addDepartement(Departement departement) {
        // Start a transaction
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(departement);
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
    
    public boolean updateDepartement(Departement departement) {
        try {
            entityManager.getTransaction().begin();
            Departement existingDepartement = entityManager.find(Departement.class, departement.getId());
            if (existingDepartement != null) {
                existingDepartement.setNom(departement.getNom());
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
    
    public boolean deleteDepartementById(Integer departementId) {
        try {
            entityManager.getTransaction().begin();
            Departement departement = entityManager.find(Departement.class, departementId);
            if (departement != null) {
                entityManager.remove(departement);
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
