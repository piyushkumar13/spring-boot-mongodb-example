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
import org.springframework.data.repository.query.Param;

/**
 * @author Piyush Kumar.
 * @since 01/01/23.
 */
public interface StudentRepository extends MongoRepository<Student, Integer> {

    List<Student> findByAddressCityAndCollege(String city, String college);

    List<Student> findByNameStartsWith(String startWithName);

    @Query(value = "{age: {$gt: ?0, $lt: ?1}}", fields = "{address: 0}" )
    List<Student> findStudentsBtwAge(int min, int max);

    @Query(value = "{'address.buildingName': ?0}") // we could also just use spring data findByAddressBuildingName method. This is just another example.
    Page<Student> findStudentsByBuildingNamePaginated(String buildingName, Pageable pageable);

    //===================================== count =================================//

    long countByCourse(String course);

    @Query(value = "{course: '?0'}", count = true)
    long countByCourseQuery(String course);

    //===================================== exists =================================//


    boolean existsStudentByCollege(String college);

    @Query(value = "{college: '?0'}", exists = true)
    boolean existsStudentByCollegeQuery(String college);

    //===================================== sort =================================//

    @Query(value = "{}", sort = "{name: 1}") // for descending use -1
    List<Student> findStudentInSortingQuery();

    //===================================== delete =================================//

    long deleteStudentByName(String name);

    @Query(value = "{name: ?0}", delete = true)
    long deleteStudentByNameQuery(String name);

    //===================================== regex =================================//


    List<Student> findStudentByNameRegex(String name);

    @Query("{name: { $regex: ?0 } })")
    List<Student> findStudentByNameRegExQuery(String name);

    //===================================== using params =================================//

    @Query(value = "{course: :#{#courseName}, 'address.buildingName': :#{#building}}")
    List<Student> findStudentByCourseAndBuildingNameQueryUsingParams(@Param("courseName") String course, @Param("building") String buildingName);
}
