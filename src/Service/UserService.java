/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Entities.User;
import Helper.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class UserService {
    private EntityManager entityManager;

    public UserService() {
        this.entityManager = JPAUtil.getEntityManager();
    }

    public User findByEmail(String email) {
        try {
            return entityManager.createNamedQuery("User.findByEmail", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Aucun utilisateur trouvé
        }
    }
    
    public void closeEntityManager() {
        this.entityManager.close();
    }
}
