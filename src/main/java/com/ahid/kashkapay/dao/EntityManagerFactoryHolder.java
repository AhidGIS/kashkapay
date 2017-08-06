/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.dao;

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author cccc
 */
public class EntityManagerFactoryHolder {
    
    private static EntityManagerFactory emf = null;
    
    public synchronized static void init(Map props) {
        emf = Persistence.createEntityManagerFactory("sqlite_db", props);
    }
    
    public synchronized static void destroy() {
        emf.close();
    }
    
    public synchronized static EntityManager createEntityManager() {
        return emf.createEntityManager();    
    }
}
