package com.musicplatform.backend.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    private static final String URI = "mongodb+srv://valmartucci_db_user:Gm7S3v60fPLrB2Aa@cluster0.graax7a.mongodb.net/musicdb?retryWrites=true&w=majority";
    private static final String DB  = "musicdb";

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(URI);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, DB);
    }
}
