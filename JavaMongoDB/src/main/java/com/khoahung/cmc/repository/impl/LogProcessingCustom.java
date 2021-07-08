package com.khoahung.cmc.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.khoahung.cmc.entity.LogData;
import com.khoahung.cmc.repository.LogProcessing;

@Repository
public class LogProcessingCustom implements LogProcessing{
	@PersistenceContext
    private EntityManager em;
	@Override
    @Transactional
    public void save(LogData longData) {
          em.createNativeQuery("INSERT INTO LogData(id, recordId, updateTime)" +
                "VALUES (?,?,?,?,?)")
                .setParameter(1, longData.getId())
                .setParameter(2, longData.getRecordId())
                .setParameter(3, longData.getUpdateTime())
                .executeUpdate();
    }
}
