package com.mindex.challenge.service.impl;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.service.ReportingStructureService;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

@Service
public class ReportingStructureImpl implements ReportingStructureService {
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	@Autowired
    private EmployeeRepository employeeRepository;
	
	@Override
    public ReportingStructure read(String id) {
        LOG.debug("Reading employee with id [{}]", id);
        int numberOfReports = getAllReportsFor(id);
        Employee employee = employeeRepository.findByEmployeeId(id);
                
        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(numberOfReports);
        
        return reportingStructure;
    }
	
	private int getAllReportsFor(String id) {
				
		Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
		Queue<Employee> queue = new ArrayDeque<>();
		Set<Employee> allReports = new HashSet<>();
		
		queue.add(employee);
		
		while (!queue.isEmpty()) {
			Employee current = queue.remove();
			List<Employee> directReports = current.getDirectReports();
			if (directReports != null && directReports.size() != 0) {
				for ( Employee directReport : directReports) {
					if ( !allReports.contains(directReport)) {
						queue.add(directReport);
						allReports.add(directReport);
					}
				}
			}	
		}
		
		return allReports.size();
	}
}
