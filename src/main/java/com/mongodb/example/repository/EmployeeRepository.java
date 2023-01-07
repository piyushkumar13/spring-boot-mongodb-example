/*
 *  Copyright (c) 2022 LogMeIn
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO LOGMEIN
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */
package com.mongodb.example.repository;

import com.mongodb.example.domain.entity.Employee;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Piyush Kumar.
 * @since 07/01/23.
 */
public interface EmployeeRepository extends MongoRepository<Employee, Integer> {

    List<Employee> findEmployeesByAgeAndDepartment(int age, String department);
}
