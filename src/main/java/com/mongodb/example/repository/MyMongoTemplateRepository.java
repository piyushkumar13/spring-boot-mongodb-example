/*
 *  Copyright (c) 2022 LogMeIn
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO LOGMEIN
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */
package com.mongodb.example.repository;

import com.mongodb.example.domain.Student;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
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

    public void findStudentsWithMoreCriterias(){

//        Query query = new Query();
//        query.addCriteria(Criteria.where("college").regex("BVP", "i"));
//        query.addCriteria(Criteria.where("age").gte(32));
//        query.addCriteria(Criteria.where("address.buildingName").is("A4"));
//
//        List<Student> students = mongoTemplate.find(query, Student.class);
//
//        System.out.println("The mongo template student with more criterias ::: " + students);


        //================================ Another way of defining criterias =============================


        Query query = new Query();

        List<Criteria> criteriaList = new ArrayList<>();

        criteriaList.add(Criteria.where("college").regex("BVP", "i"));
        criteriaList.add(Criteria.where("age").gte(32));
        criteriaList.add(Criteria.where("address.buildingName").is("A4"));

        query.addCriteria(new Criteria().andOperator(criteriaList)); // you could also use orOperator if want to concat criterias with or.

        List<Student> students = mongoTemplate.find(query, Student.class);

        System.out.println("The mongo template student with more criterias ::: " + students);

    }

    public void findStudentsWithMoreCriteriasAndPagination(){

        Query query = new Query();
        query.addCriteria(Criteria.where("college").regex("BVP", "i"));
        query.addCriteria(Criteria.where("age").gte(32));
        query.addCriteria(Criteria.where("address.buildingName").is("A4"));

        query.with(PageRequest.of(0, 2, Sort.by("name").descending()));
        List<Student> studentsPage0 = mongoTemplate.find(query, Student.class);
        System.out.println("The mongo template student with more criterias and page 0 ::: " + studentsPage0);

        query.with(PageRequest.of(1, 2, Sort.by("name").descending()));
        List<Student> studentsPage1 = mongoTemplate.find(query, Student.class);
        System.out.println("The mongo template student with more criterias and page 1 ::: " + studentsPage1);

        query.with(PageRequest.of(2, 2, Sort.by("name").descending()));
        List<Student> studentsPage2 = mongoTemplate.find(query, Student.class);
        System.out.println("The mongo template student with more criterias and page 2 ::: " + studentsPage2);

    }

    public void aggregationOperation(){

        MatchOperation ageMatch = Aggregation.match(Criteria.where("age").gte(32));
        GroupOperation groupByBuildingName = Aggregation.group("address.buildingName").count().as("addressCount");
//        ProjectionOperation projection = Aggregation.project("addressCount").and("_id").as("addressBuildingName");
//        ProjectionOperation projection = Aggregation.project().and("addressCount").as("addressCount").and("_id").as("addressBuildingName");
        ProjectionOperation projection = Aggregation.project().andExpression("addressCount").as("addressCount").andExpression("_id").as("addressBuildingName");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "addressCount");

        Aggregation aggregation = Aggregation.newAggregation(ageMatch, groupByBuildingName, projection,sortOperation);

        AggregationResults<AggregationBuildingCountClass> aggregateResult = mongoTemplate.aggregate(aggregation, Student.class, AggregationBuildingCountClass.class);

        List<AggregationBuildingCountClass> mappedResults = aggregateResult.getMappedResults();
        System.out.println("Aggregation result :::: " + mappedResults);

    }


    private static class AggregationBuildingCountClass{
        private String addressBuildingName;
        private int addressCount;


        public String getAddressBuildingName() {
            return addressBuildingName;
        }

        public void setAddressBuildingName(String addressBuildingName) {
            this.addressBuildingName = addressBuildingName;
        }

        public int getAddressCount() {
            return addressCount;
        }

        public void setAddressCount(int addressCount) {
            this.addressCount = addressCount;
        }

        @Override
        public String toString() {
            return "AggregationBuildingCountClass{" +
                    "buildingName='" + addressBuildingName + '\'' +
                    ", addressCount=" + addressCount +
                    '}';
        }
    }
}
