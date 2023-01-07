/*
 *  Copyright (c) 2022 LogMeIn
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO LOGMEIN
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */
package com.mongodb.example.domain.entity;

import com.mongodb.example.domain.Address;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TextScore;

/**
 * @author Piyush Kumar.
 * @since 07/01/23.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "employees")
@CompoundIndex(name = "department_age_Index", def = "{'age': -1, 'department': -1}") // we can also use @CompoundIndexes if we want to provide more compound indexes.
// Index on age and department will use index on query for age and department as well as on just age but not on just department. So, if we are creating single index on age is not required
// if compound index is defined.
public class Employee {

    @Id
    private int id;
    private int age;

    @Indexed(unique = true)
    @TextIndexed
    private String name;

    @Field(value = "organization")
    private String org;

    @TextIndexed(weight = 2)
    private String department;
    private Address address;
    private List<String> technologies;

    @Transient // It will not save the field in mongodb
    private String employeeType;

    @Version // For optimistic locking
    private long version;

    @TextScore
    private Float score;
}
