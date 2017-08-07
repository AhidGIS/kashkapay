/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.services;

import com.ahid.kashkapay.entities.LearnType;
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
public class LearnTypeService {

    public static List<LearnType> getAll() {
        Query query = EntityManagerFactoryHolder.createEntityManager().createNamedQuery("LearnType.findAll");
        return query.getResultList();
    }

    public static void save(LearnType lt) throws DuplicateException {
        if (lt.getId() == null) {
            int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                    .createNamedQuery("LearnType.existedCountByName").setParameter("name", lt.getName()).getSingleResult().toString());
            if (existedCount > 0) {
                throw new DuplicateException("Вид обучения уже существует.  \nНельзя дублировать");
            }
            lt.setId(uniqueString());
        } else {
            int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                    .createNamedQuery("LearnType.existedCountByNotIdAndName").setParameter("id", lt.getId()).
                    setParameter("name", lt.getName()).getSingleResult().toString());
            if (existedCount > 0) {
                throw new DuplicateException("Вид обучения уже существует.  \nНельзя дублировать");
            }
        }

        persist(lt);
    }

    public static void delete(LearnType lt) throws ReferencesExistsException {
        int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                .createNamedQuery("Protocol.countByLearnType").setParameter("id", lt.getId()).getSingleResult().toString());
        if (existedCount > 0) {
            throw new ReferencesExistsException("Невозможно удалить вид обучения. \nЕсть зависимые данные в таблице Протокола");
        }
        remove(lt);
    }

    private static void persist(LearnType lt) {
        EntityManager em = EntityManagerFactoryHolder.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(lt);
        tx.commit();
    }

    private static void remove(LearnType lt) {
        EntityManager em = EntityManagerFactoryHolder.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        lt = em.find(lt.getClass(), lt.getId());
        em.remove(lt);
        tx.commit();
    }
}
