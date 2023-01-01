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
import com.mongodb.example.repository.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Piyush Kumar.
 * @since 01/01/23.
 */

@RestController
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

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

        Student student = Student.builder().id(studentDto.getId())
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

    }
}