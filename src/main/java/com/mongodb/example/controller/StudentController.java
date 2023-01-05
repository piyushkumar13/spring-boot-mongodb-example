/*
 *  Copyright (c) 2022 LogMeIn
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO LOGMEIN
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */
package com.mongodb.example.controller;

import com.mongodb.example.controller.dto.StudentDto;
import com.mongodb.example.controller.dto.SubjectDto;
import com.mongodb.example.domain.Address;
import com.mongodb.example.domain.Student;
import com.mongodb.example.domain.Subject;
import com.mongodb.example.repository.MyMongoTemplateRepository;
import com.mongodb.example.repository.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Piyush Kumar.
 * @since 01/01/23.
 */

@RestController
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MyMongoTemplateRepository myMongoTemplateRepository;

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudents() {

        List<Student> students = studentRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudents(@PathVariable int id) {

        Optional<Student> studentOptional = studentRepository.findById(id);
        Student student = studentOptional.orElse(new Student());

        return ResponseEntity.status(HttpStatus.OK).body(student);
    }

    @RequestMapping(value = "/students", method = RequestMethod.POST)
    public void addStudent(@RequestBody StudentDto studentDto) {


        List<Subject> subjects = new ArrayList<>();

        for (SubjectDto subjectDto : studentDto.getSubjectDtos()) {
            subjects.add(Subject.builder().subjectNumber(subjectDto.getSubjectNumber()).subjectName(subjectDto.getSubjectName()).build());
        }

        Student student = Student.builder()
                .id(studentDto.getId())
                .name(studentDto.getName())
                .college(studentDto.getCollege())
                .course(studentDto.getCourse())
                .age(studentDto.getAge())
                .address(Address.builder()
                        .city(studentDto.getCity())
                        .buildingName(studentDto.getBuildingName())
                        .state(studentDto.getState())
                        .country(studentDto.getCountry())
                        .build())
                .subjects(subjects)
                .build();

        studentRepository.save(student);
    }

    @RequestMapping(value = "/test")
    public void testMethods(){

        List<Student> byAddressCityAndCollege = studentRepository.findByAddressCityAndCollege("New Delhi", "BVP");

        System.out.println("Found by address city and student college ::: " + byAddressCityAndCollege);

        List<Student> studentPiyush = studentRepository.findByNameStartsWith("Piyush");
        System.out.println("Name starting with piyush" + studentPiyush);

        List<Student> studentsBtwAge = studentRepository.findStudentsBtwAge(20, 35);

        System.out.println("The students age between :::: " + studentsBtwAge);

        Page<Student> studentPage0 = studentRepository.findStudentsByBuildingNamePaginated("A4", PageRequest.of(0, 2));
        System.out.println("Page 0 ::: " + studentPage0.getContent());

        Page<Student> studentPage1 = studentRepository.findStudentsByBuildingNamePaginated("A4", PageRequest.of(1, 2));
        System.out.println("Page 1 ::: " + studentPage1.getContent());

        Page<Student> studentPage2 = studentRepository.findStudentsByBuildingNamePaginated("A4", PageRequest.of(2, 2));
        System.out.println("Page 2 ::: " + studentPage2.getContent());

        Page<Student> studentPage3 = studentRepository.findStudentsByBuildingNamePaginated("A4", PageRequest.of(3, 2));
        System.out.println("Page 3 ::: " + studentPage3.getContent());
    }


    @GetMapping(value = "/students/pagination")
    public ResponseEntity<List<Student>> getStudentsWithPagination(@RequestParam("page") int page, @RequestParam("size") int size){

        Pageable pageable = PageRequest.of(page, size);

        Page<Student> studentPages = studentRepository.findAll(pageable);
        List<Student> students = studentPages.getContent();

        return ResponseEntity.ok(students);
    }

    @GetMapping(value = "/students/paginationsort")
    public ResponseEntity<List<Student>> getStudentsWithPaginationAndSorting(@RequestParam("page") int page, @RequestParam("size") int size){

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").descending());

        Page<Student> studentPages = studentRepository.findAll(pageable);
        List<Student> students = studentPages.getContent();

        return ResponseEntity.ok(students);
    }

    @GetMapping(value = "/students/page/paginationsort")
    public ResponseEntity<Page<Student>> getStudentsPageWithPaginationAndSorting(@RequestParam("page") int page, @RequestParam("size") int size){

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").descending());

        Page<Student> studentPages = studentRepository.findAll(pageable);

        return ResponseEntity.ok(studentPages);
    }

    @GetMapping(value = "/students/sort")
    public ResponseEntity<List<Student>> getStudentsSorted(){

        Sort sort = Sort.by("name").descending();

        List<Student> students = studentRepository.findAll(sort);

        return ResponseEntity.ok(students);
    }

    @GetMapping(value = "/test/mongotemplate")
    public void testMongoTemplate(){

        myMongoTemplateRepository.findStudent();

        myMongoTemplateRepository.findStudentsWithMoreCriterias();

        myMongoTemplateRepository.findStudentsWithMoreCriteriasAndPagination();

        myMongoTemplateRepository.aggregationOperation();
    }

    @GetMapping(value = "/test/query")
    public void testMongoQueries(){

        //===================================== count =================================//

        long countByCourse = studentRepository.countByCourse("Engineering");
        System.out.println("The count is ::: " + countByCourse);

        long countByCourseQuery = studentRepository.countByCourseQuery("Engineering");
        System.out.println("The count query is ::: " + countByCourseQuery);

        //===================================== exists =================================//

        boolean isExists = studentRepository.existsStudentByCollege("BVP");
        System.out.println("The existsStudentByCollege ::: " + isExists);

        boolean isExistsQuery = studentRepository.existsStudentByCollegeQuery("BVP");
        System.out.println("The existsStudentByCollegeQuery ::: " + isExistsQuery);

        //===================================== sort =================================//

        List<Student> studentInSortingQuery = studentRepository.findStudentInSortingQuery();
        System.out.println("studentInSortingQuery ::: " + studentInSortingQuery);

        //===================================== delete =================================//

        long deleteStudentByName = studentRepository.deleteStudentByName("Abhishek");
        System.out.println("deleteStudentByName :::: " + deleteStudentByName);

        long deleteStudentByNameQuery = studentRepository.deleteStudentByNameQuery("Sumit");
        System.out.println("deleteStudentByNameQuery :::: " + deleteStudentByNameQuery);

        //===================================== regex =================================//

        List<Student> findStudentByNameRegex = studentRepository.findStudentByNameRegex("Piyush");
        System.out.println("findStudentByNameRegex ::: " + findStudentByNameRegex);

        List<Student> findStudentByNameRegExQuery = studentRepository.findStudentByNameRegExQuery("Piyush");
        System.out.println("findStudentByNameRegExQuery ::: " + findStudentByNameRegExQuery);

        //===================================== using params =================================//

        List<Student> findStudentByCourseAndBuildingNameQueryUsingParams = studentRepository.findStudentByCourseAndBuildingNameQueryUsingParams("Engineering", "A4");
        System.out.println("findStudentByCourseAndBuildingNameQueryUsingParams :::: " + findStudentByCourseAndBuildingNameQueryUsingParams);
    }
}
