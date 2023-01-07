/*
 *  Copyright (c) 2022 LogMeIn
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO LOGMEIN
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */
package com.mongodb.example.domain;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author Piyush Kumar.
 * @since 07/01/23.
 */
public class AggregationBuildingCountClass{
    @Field("_id")
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
