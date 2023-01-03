/*
 *  Copyright (c) 2022 LogMeIn
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO LOGMEIN
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */
package com.mongodb.example.repository;

import com.mongodb.example.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Piyush Kumar.
 * @since 03/01/23.
 */
@Repository
public class MyMongoTemplateRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    public void findStudent(){

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Piyush Kumar Kirad"));
//        Query query = Query.query(Criteria.where("name").is("Piyush Kumar Kirad"));

        Student student = mongoTemplate.findOne(query, Student.class);

        System.out.println("The mongo template student ::: " + student);

    }
}
