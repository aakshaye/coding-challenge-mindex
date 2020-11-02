package com.mindex.challenge.dao;

import com.mindex.challenge.data.Compensation;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {
	// Get Compensation for employee with Employee ID from Mongo
	Compensation findCompensationByEmployeeEmployeeId(String employeeId);
}
