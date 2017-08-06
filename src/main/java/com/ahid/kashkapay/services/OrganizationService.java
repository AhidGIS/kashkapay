/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.services;

import com.ahid.kashkapay.entities.Organization;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author cccc
 */
public class OrganizationService {
    
    public static List<Organization> getAll() {
        Query query = EntityManagerFactoryHolder.createEntityManager().createNamedQuery("Organization.findAll");
        return query.getResultList();
    }
}
