/*
 *  Copyright (c) 2022 LogMeIn
 *  All Rights Reserved Worldwide.
 *
 *  THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO LOGMEIN
 *  AND CONSTITUTES A VALUABLE TRADE SECRET.
 */
package com.mongodb.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

/**
 * @author Piyush Kumar.
 * @since 07/01/23.
 */

@Configuration
public class MongoConfig {


    /* To work with transactions we need mongodb replicasets. For development purpose we can convert the standalone local running mongodb to replicaset.
    * https://stackoverflow.com/questions/51461952/mongodb-v4-0-transaction-mongoerror-transaction-numbers-are-only-allowed-on-a
    * https://www.mongodb.com/community/forums/t/why-replica-set-is-mandatory-for-transactions-in-mongodb/9533/4
    * https://www.mongodb.com/docs/manual/tutorial/convert-standalone-to-replica-set/ - converting standalone to instance to replicaset.
    * */
    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory databaseFactory){

        return  new MongoTransactionManager(databaseFactory);
    }
}
