/*
 *  Copyright (c) 2022 LogMeIn
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO LOGMEIN
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */
package com.mongodb.example.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Piyush Kumar.
 * @since 01/01/23.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("students")
public class Student {

    @Id
    private int id;
    private int age;
    @Indexed(unique = true)
    private String name;
    private String college;
    private String course;
    private Address address;
    private List<Subject> subjects;
}
