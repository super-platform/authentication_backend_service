//package com.platform.configs;
//
//import org.springframework.boot.autoconfigure.mongo.MongoProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//@Configuration
//@EnableMongoRepositories(
//        basePackages = {
//                "com.platform.repositories.mongo"
//        },
//        mongoTemplateRef = "mongoDBTemplateTemplate"
//)
//public class MongoDBConfig {
//
//    @Primary
//    @Bean(name = "templateProperties")
//    @ConfigurationProperties(prefix = "spring.data.mongodb.ngtu-template")
//    public MongoProperties getTemplateProps(){
//        return new MongoProperties();
//    }
//
//    @Primary
//    @Bean(name = "mongoDBTemplateTemplate")
//    public MongoTemplate mongoDBTemplateTemplate(){
//        return new MongoTemplate(newMongoDBTemplateDatabaseFactory(getTemplateProps()));
//    }
//
//    @Primary
//    @Bean
//    public MongoDatabaseFactory newMongoDBTemplateDatabaseFactory(MongoProperties mongoProperties){
//        return new SimpleMongoClientDatabaseFactory(mongoProperties.getUri());
//    }
//
//}
