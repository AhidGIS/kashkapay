/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.services;

import com.ahid.kashkapay.entities.Protocol;
import com.ahid.kashkapay.entities.Specialization;
import com.ahid.kashkapay.exceptions.DuplicateException;
import com.ahid.kashkapay.exceptions.ReferencesExistsException;
import static com.ahid.kashkapay.utils.CommonUtil.uniqueString;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author cccc
 */
public class ProtocolService {

    public static List<Protocol> getAll() {
        Query query = EntityManagerFactoryHolder.createEntityManager().createNamedQuery("Protocol.findAll");
        return query.getResultList();
    }

    public static List<Protocol> getFiltered(Map<String, String> filters) {
        Query query = EntityManagerFactoryHolder.createEntityManager()
                .createNativeQuery("select * from protocols p where strftime('%Y', p.protocol_date) = '" + 
                        filters.get("current_year") + "'", Protocol.class);
        return (List<Protocol>) query.getResultList();
    }

    public static void save(Protocol protocol) throws DuplicateException {
        if (protocol.getId() == null) {
            int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                    .createNamedQuery("Protocol.existedCountByName").setParameter("protocolNumber", protocol.getProtocolNumber())
                    .setParameter("protocolDate", protocol.getProtocolDate()).getSingleResult().toString());
            if (existedCount > 0) {
                throw new DuplicateException("Протокол с таким номером и датой уже существует.  \nНельзя дублировать");
            }
            protocol.setId(uniqueString());
        } else {
            int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                    .createNamedQuery("Protocol.existedCountByNotIdAndName")
                    .setParameter("id", protocol.getId())
                    .setParameter("protocolNumber", protocol.getProtocolNumber())
                    .setParameter("protocolDate", protocol.getProtocolDate())
                    .getSingleResult().toString());
            if (existedCount > 0) {
                throw new DuplicateException("Протокол с таким номером и датой уже существует.  \nНельзя дублировать");
            }
        }

        persist(protocol);
    }

    private static void persist(Protocol protocol) {
        EntityManager em = EntityManagerFactoryHolder.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(protocol);
        tx.commit();
    }

    public static void delete(Protocol protocol) throws ReferencesExistsException {
        int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                .createNamedQuery("Certificate.countByProtocol").setParameter("id", protocol.getId()).getSingleResult().toString());
        if (existedCount > 0) {
            throw new ReferencesExistsException("Невозможно удалить протокол. \nЕсть зависимые данные в таблице Удостоверения");
        }
        remove(protocol);
    }

    private static void remove(Protocol protocol) {
        EntityManager em = EntityManagerFactoryHolder.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        protocol = em.find(protocol.getClass(), protocol.getId());
        em.remove(protocol);
        tx.commit();
    }
}
