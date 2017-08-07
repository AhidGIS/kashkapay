/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.services;

import com.ahid.kashkapay.entities.LearnType;
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
public class SpecializationService {
    
    public static List<Specialization> getAll() {
        Query query = EntityManagerFactoryHolder.createEntityManager().createNamedQuery("Specialization.findAll");
        return query.getResultList();
    }
    
    public static void save(Specialization spec) throws DuplicateException {
        if (spec.getId() == null) {
            int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                    .createNamedQuery("Specialization.existedCountByName").setParameter("name", spec.getName()).getSingleResult().toString());
            if (existedCount > 0) {
                throw new DuplicateException("Специальность уже существует.  \nНельзя дублировать");
            }
            spec.setId(uniqueString());
        } else {
            int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                    .createNamedQuery("Specialization.existedCountByNotIdAndName").setParameter("id", spec.getId()).
                    setParameter("name", spec.getName()).getSingleResult().toString());
            if (existedCount > 0) {
                throw new DuplicateException("Специальность уже существует.  \nНельзя дублировать");
            }
        }

        persist(spec);
    }

    public static void delete(Specialization spec) throws ReferencesExistsException {
        int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                .createNamedQuery("Protocol.countBySpecialization").setParameter("id", spec.getId()).getSingleResult().toString());
        if (existedCount > 0) {
            throw new ReferencesExistsException("Невозможно удалить специальность. \nЕсть зависимые данные в таблице Протокола");
        }
        remove(spec);
    }

    private static void persist(Specialization spec) {
        EntityManager em = EntityManagerFactoryHolder.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(spec);
        tx.commit();
    }

    private static void remove(Specialization spec) {
        EntityManager em = EntityManagerFactoryHolder.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        spec = em.find(spec.getClass(), spec.getId());
        em.remove(spec);
        tx.commit();
    }
}
