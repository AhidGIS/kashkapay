/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.services;

import com.ahid.kashkapay.entities.Certificate;
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
public class CertificateService {
    
    public static List<Certificate> getAll() {
        Query query = EntityManagerFactoryHolder.createEntityManager().createNamedQuery("Certificate.findAll");
        return query.getResultList();
    }

    public static List<Certificate> getFiltered(Map<String, String> filters) {
        String sqlText = "select * from certificates where strftime('%Y', certificate_date) = '" + 
                        filters.get("current_year") + "'";
        
        if (filters.containsKey("certificate_number")) {
            sqlText += " and certificate_number like '%" + filters.get("certificate_number") + "%'";
        }
        
        if (filters.containsKey("fullname")) {
            sqlText += " and fullname like '%" + filters.get("fullname") + "%'";
        }
        
        if (filters.containsKey("birth_date")) {
            sqlText += " and birth_date like '%" + filters.get("birth_date") + "%'";
        }
        
        if (filters.containsKey("protocol_id")) {
            sqlText += " and protocol_id = '" + filters.get("protocol_id") + "'";
        }
        
        if (filters.containsKey("organization_id")) {
            sqlText += " and organization_id = '" + filters.get("organization_id") + "'";
        }
        
        boolean isSubqueryNeeded = false;
        String protocolSubquery = "select id from protocols where strftime('%Y', protocol_date) = '" + 
                        filters.get("current_year") + "'";
        
        if (filters.containsKey("learn_type_id")) {
            protocolSubquery += " and learn_type = '" + filters.get("learn_type_id") + "'";
            isSubqueryNeeded = true;
        }
        
        if (filters.containsKey("specialization_id")) {
            protocolSubquery += " and specialization = '" + filters.get("specialization_id") + "'";
            isSubqueryNeeded = true;
        }
        
        if (isSubqueryNeeded) {
            sqlText = sqlText + " and protocol_id in (" + protocolSubquery + ")";
        }
        
        sqlText += " order by certificate_date, certificate_number";
        
        Query query = EntityManagerFactoryHolder.createEntityManager()
                .createNativeQuery(sqlText, Certificate.class);
        return (List<Certificate>) query.getResultList();
    }

    public static void save(Certificate certificate) throws DuplicateException {
        if (certificate.getId() == null) {
            int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                    .createNamedQuery("Certificate.existedCountByName").setParameter("certificateNumber", certificate.getCertificateNumber())
                    .setParameter("certificateDate", certificate.getCertificateDate()).getSingleResult().toString());
            if (existedCount > 0) {
                throw new DuplicateException("Сертификат с таким номером и датой уже существует.  \nНельзя дублировать");
            }
            certificate.setId(uniqueString());
        } else {
            int existedCount = Integer.parseInt(EntityManagerFactoryHolder.createEntityManager()
                    .createNamedQuery("Certificate.existedCountByNotIdAndName")
                    .setParameter("id", certificate.getId())
                    .setParameter("certificateNumber", certificate.getCertificateNumber())
                    .setParameter("certificateDate", certificate.getCertificateDate())
                    .getSingleResult().toString());
            if (existedCount > 0) {
                throw new DuplicateException("Сертификат с таким номером и датой уже существует.  \nНельзя дублировать");
            }
        }

        persist(certificate);
    }

    private static void persist(Certificate certificate) {
        EntityManager em = EntityManagerFactoryHolder.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(certificate);
        tx.commit();
    }

    public static void delete(Certificate certificate) throws ReferencesExistsException {
        remove(certificate);
    }

    private static void remove(Certificate certificate) {
        EntityManager em = EntityManagerFactoryHolder.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        certificate = em.find(certificate.getClass(), certificate.getId());
        em.remove(certificate);
        tx.commit();
    }
}
