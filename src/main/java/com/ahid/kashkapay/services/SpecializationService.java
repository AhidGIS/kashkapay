/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.services;

import com.ahid.kashkapay.entities.Specialization;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author cccc
 */
public class SpecializationService {
    
    public static List<Specialization> getAll() {
        Query query = EntityManagerFactoryHolder.createEntityManager().createNamedQuery("Specialization.findAll");
        return query.getResultList();
    }
}
