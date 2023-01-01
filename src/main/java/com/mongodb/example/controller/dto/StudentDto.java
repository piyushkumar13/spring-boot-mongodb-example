/*
 *  Copyright (c) 2022 LogMeIn
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO LOGMEIN
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */
package com.mongodb.example.controller.dto;

import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author Piyush Kumar.
 * @since 01/01/23.
 */
@Data
public class StudentDto {

    private int id;
    private int age;
    private String name;
    private String college;
    private String course;
    private String city;
    private String state;
    private String buildingName;
    private String country;
    private List<SubjectDto> subjectDtos;
}
