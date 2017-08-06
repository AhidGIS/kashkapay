/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.dao;

import com.ahid.kashkapay.entities.LearnType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author cccc
 */
public class LearnTypeDao {
    
    public static List<LearnType> getAll() {
        Query query = EntityManagerFactoryHolder.createEntityManager().createNamedQuery("LearnType.findAll");
        return query.getResultList();
    }
    
    public static void create(LearnType lt) {
        EntityManager em = EntityManagerFactoryHolder.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(lt);
        tx.commit();
    }
}
