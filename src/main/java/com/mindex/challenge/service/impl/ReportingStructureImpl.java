package com.mindex.challenge.service.impl;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureImpl implements ReportingStructureService {
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	@Autowired
    private EmployeeRepository employeeRepository;
	
	@Override
    public ReportingStructure read(String id) {
        LOG.debug("Reading reporting structure with id [{}]", id);
        int numberOfReports = getAllReportsFor(id);
        Employee employee = employeeRepository.findByEmployeeId(id);
                
        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(numberOfReports);
        
        return reportingStructure;
    }
	
	/**
	 * Do a Breadth first search for direct reports of each employee
	 * Using a set to avoid cyclic searching and getting only distinct reports
	 * @param 	id EmployeeID
	 * @return  Count of all distinct reports
	 */
	private int getAllReportsFor(String id) {
				
		Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
		Queue<Employee> queue = new ArrayDeque<>(); // BFS queue
		Set<Employee> distinctReports = new HashSet<>(); // Keep track of distinct reports
		
		queue.add(employee); // add employee to queue to start searching
		
		// Keep adding direct reports to queue and in turn check their direct reports and so on
		while (!queue.isEmpty()) {
			Employee current = queue.remove();
			current = employeeRepository.findByEmployeeId(current.getEmployeeId());
			List<Employee> directReports = current.getDirectReports();
			if (directReports != null && directReports.size() != 0) {
				for ( Employee directReport : directReports) {
					if ( !distinctReports.contains(directReport)) {
						queue.add(directReport);
						distinctReports.add(directReport);
					}
				}
			}	
		}
		
		return distinctReports.size(); // size of set will be total number of distinct reports
	}
}
