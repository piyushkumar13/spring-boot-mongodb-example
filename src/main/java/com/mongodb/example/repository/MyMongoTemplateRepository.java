/*
 *  Copyright (c) 2022 LogMeIn
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO LOGMEIN
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */
package com.mongodb.example.repository;

import static java.util.Arrays.asList;

import com.mongodb.example.domain.Address;
import com.mongodb.example.domain.Subject;
import com.mongodb.example.domain.entity.Employee;
import com.mongodb.example.domain.entity.Student;
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
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;
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
//        ProjectionOperation projection = Aggregation.project("addressCount").and("_id").as("addressBuildingName"); // we can do it like this
//        ProjectionOperation projection = Aggregation.project().and("addressCount").as("addressCount").and("_id").as("addressBuildingName"); // we can do it like this as well
        ProjectionOperation projection = Aggregation.project().andExpression("addressCount").as("addressCount").andExpression("_id").as("addressBuildingName"); //// we can do it like this as well
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "addressCount");

        Aggregation aggregation = Aggregation.newAggregation(ageMatch, groupByBuildingName, projection,sortOperation);

        AggregationResults<AggregationBuildingCountClass> aggregateResult = mongoTemplate.aggregate(aggregation, Student.class, AggregationBuildingCountClass.class);

        List<AggregationBuildingCountClass> mappedResults = aggregateResult.getMappedResults();
        System.out.println("Aggregation result :::: " + mappedResults);

    }


    public void insertStudent(){

        Student student = Student.builder()
                .id(7)
                .name("Sumit")
                .age(37)
                .subjects(asList(Subject.builder().subjectNumber("P-101").subjectName("Physics").build(), Subject.builder().subjectNumber("P-102").subjectName("Chemistry").build()))
                .college("SVC")
                .course("Engineering")
                .address(Address.builder().city("New Delhi").state("Delhi").country("India").buildingName("Dilshad").build()).build();

        mongoTemplate.insert(student);
    }

    /* The save operation has save-or-update semantics: if an id is present, it performs an update, and if not, it does an insert. */
    public void saveStudent(){

        Student student = Student.builder()
                .id(8)
                .name("Anoop")
                .age(40)
                .subjects(asList(Subject.builder().subjectNumber("P-101").subjectName("Physics").build(), Subject.builder().subjectNumber("P-102").subjectName("Chemistry").build()))
                .college("ABC College")
                .course("Engineering")
                .address(Address.builder().city("Indore").state("MP").country("India").buildingName("SomeBuilding").build()).build();

        mongoTemplate.save(student);
    }


    public void updateStudent(){

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Sumit"));

        Update update = new Update();
        update.set("college", "SVC College");

        mongoTemplate.updateFirst(query, update, Student.class);
    }


    public void existsStudent(){

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Anoop"));

        boolean exists = mongoTemplate.exists(query, "students");

        System.out.println("Student exists :::: " + exists);
    }

    public void findAndModify(){
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("Anoop"));

        Update update = new Update();
        update.set("address.buildingName", "SBR");

        Student existingStudent = mongoTemplate.findAndModify(query, update, Student.class);
        System.out.println("Existing student document :::  " + existingStudent);
    }


    //========================================  Text Search Operations on Employee ====================================

    /**
     * It will be searched by exact term passed against textIndexes field and also will consider weight in account which decides score.
     * So, here it will search the term against department and name and returns matching OR result of department and name
     * */
    public void textSearchOnEmployeesByMatchingTerm(String term){
        Query query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matching(term)).sortByScore().with(Sort.by(Sort.Direction.DESC, "score"));
        List<Employee> searchByMatchingTerm = mongoTemplate.find(query, Employee.class);
        System.out.println("textSearchOnEmployeesByMatchingTerm ::: " + searchByMatchingTerm);
    }

    /**
     * It will be searched by exact list of terms passed against textIndexes field and also will consider weight in account which decides score.
     * So, here it will search the list of terms against department and name and returns matching OR result of department and name
     * */
    public void textSearchOnEmployeesByMatchingAny(String... any){
        Query query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matchingAny(any)).sortByScore().with(Sort.by(Sort.Direction.DESC, "score"));
        List<Employee> searchByMatchingAny = mongoTemplate.find(query, Employee.class);
        System.out.println("textSearchOnEmployeesByMatchingAny ::: " + searchByMatchingAny);

    }

    /**
     * It will be searched by a phrase passed against textIndexes field and also will consider weight in account which decides score.
     * So, here it will search the phrase against department and name and returns matching OR result of department and name.
     * Phrase is like a substring inside a string.
     * */
    public void textSearchOnEmployeesByMatchingPhrase(String phrase){
        Query query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matchingPhrase(phrase)).sortByScore().with(Sort.by(Sort.Direction.DESC, "score"));
        List<Employee> searchByMatchingPhrase = mongoTemplate.find(query, Employee.class);
        System.out.println("textSearchOnEmployeesByMatchingPhrase ::: " + searchByMatchingPhrase);

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
