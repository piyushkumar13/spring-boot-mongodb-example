/*
 *  Copyright (c) 2022 LogMeIn
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO LOGMEIN
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */
package com.mongodb.example.repository;

import com.mongodb.example.domain.Student;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @author Piyush Kumar.
 * @since 01/01/23.
 */
public interface StudentRepository extends MongoRepository<Student, Integer> {

    List<Student> findByAddressCityAndCollege(String city, String college);

    List<Student> findByNameStartsWith(String startWithName);

    @Query(value = "{age: {$gt: ?0, $lt: ?1}}", fields = "{address: 0}" )
    List<Student> findStudentsBtwAge(int min, int max);

    @Query(value = "{'address.buildingName': ?0}") // we could also just use spring data findByCollege method. This is just another example.
    Page<Student> findStudentsByBuildingNamePaginated(String buildingName, Pageable pageable);
}
