/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.services;

import com.ahid.kashkapay.entities.Organization;
import com.ahid.kashkapay.entities.Specialization;
import com.ahid.kashkapay.exceptions.DuplicateException;
import com.ahid.kashkapay.exceptions.ReferencesExistsException;
import static com.ahid.kashkapay.utils.CommonUtil.uniqueString;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
    
    public static void save(Organization org) throws DuplicateException {
        if (org.getId() == null) {
            int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                    .createNamedQuery("Organization.existedCountByName").setParameter("name", org.getName()).getSingleResult().toString());
            if (existedCount > 0) {
                throw new DuplicateException("Организация уже существует.  \nНельзя дублировать");
            }
            org.setId(uniqueString());
        } else {
            int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                    .createNamedQuery("Organization.existedCountByNotIdAndName").setParameter("id", org.getId()).
                    setParameter("name", org.getName()).getSingleResult().toString());
            if (existedCount > 0) {
                throw new DuplicateException("Организация уже существует.  \nНельзя дублировать");
            }
        }

        persist(org);
    }

    public static void delete(Organization org) throws ReferencesExistsException {
        int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                .createNamedQuery("Protocol.countByOrganization").setParameter("id", org.getId()).getSingleResult().toString());
        if (existedCount > 0) {
            throw new ReferencesExistsException("Невозможно удалить организацию. \nЕсть зависимые данные в таблице Протокола");
        }
        remove(org);
    }

    private static void persist(Organization org) {
        EntityManager em = EntityManagerFactoryHolder.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(org);
        tx.commit();
    }

    private static void remove(Organization org) {
        EntityManager em = EntityManagerFactoryHolder.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        org = em.find(org.getClass(), org.getId());
        em.remove(org);
        tx.commit();
    }
}
