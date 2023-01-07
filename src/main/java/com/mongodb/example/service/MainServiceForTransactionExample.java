/*
 *  Copyright (c) 2022 LogMeIn
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO LOGMEIN
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */
package com.mongodb.example.service;

import static java.util.Arrays.asList;

import com.mongodb.example.domain.entity.Employee;
import com.mongodb.example.domain.entity.Student;
import com.mongodb.example.repository.EmployeeRepository;
import com.mongodb.example.repository.StudentRepository;
import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piyush Kumar.
 * @since 07/01/23.
 */
@Service
public class MainServiceForTransactionExample {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    /* Transactional annotation will rollback in case of any of the runtime exception. But to rollback in case of checked exception we need to explicitly define them in
     * rollbackFor attribute of @Transactional annotation. */
    @Transactional(rollbackFor = FileNotFoundException.class)
    public void transactionExample() throws InterruptedException{

        studentRepository.save(Student.builder().id(9).name("Sandeep").age(32).college("BVP").course("Engineering").build());
        employeeRepository.save(Employee.builder().id(1).name("Satish").age(37).department("Engineering").technologies(asList("Spring", "Java")).org("Goto").build());

        throw new InterruptedException("Throwing my own Interrupted exception");
    }

}
