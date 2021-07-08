package com.khoahung.cmc.repository;



import org.springframework.data.repository.CrudRepository;

import com.khoahung.cmc.entity.LogData;


public interface JPALogRepository extends CrudRepository<LogData, Integer> {
}
