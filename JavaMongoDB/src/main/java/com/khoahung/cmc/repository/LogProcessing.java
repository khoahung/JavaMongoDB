package com.khoahung.cmc.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.khoahung.cmc.entity.LogData;
import com.khoahung.cmc.repository.JPALogRepository;


@Repository
public interface LogProcessing{
	public void save(LogData longData);
}
