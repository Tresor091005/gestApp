/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Entities.Student;
import Helper.JPAUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public class StudentService {
    private EntityManager entityManager;

    public StudentService() {
        this.entityManager = JPAUtil.getEntityManager();
    }
    
    public List<Student> getAllStudents() {
        try {
            return entityManager.createQuery("SELECT s FROM Student s", Student.class)
                     .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } 
    }
    
    public Student getStudentById(Integer studentId) {
        try {
            return entityManager.find(Student.class, studentId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean addStudent(Student student) {
        // Start a transaction
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(student);
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
    
    public boolean updateStudent(Student student) {
        try {
            entityManager.getTransaction().begin();
            Student existingStudent = entityManager.find(Student.class, student.getId());
            if (existingStudent != null) {
                // Mettre à jour les champs de l'étudiant
                existingStudent.setMatricule(student.getMatricule());
                existingStudent.setNom(student.getNom());
                existingStudent.setPrenom(student.getPrenom());
                existingStudent.setDateNaissance(student.getDateNaissance());
                existingStudent.setTelephone(student.getTelephone());
                existingStudent.setEmail(student.getEmail());
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
    
    public boolean deleteStudentById(Integer studentId) {
        try {
            entityManager.getTransaction().begin();
            Student student = entityManager.find(Student.class, studentId);
            if (student != null) {
                entityManager.remove(student);
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
