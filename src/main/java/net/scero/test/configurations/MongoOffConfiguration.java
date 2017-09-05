package net.scero.test.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.DB;

//@Configuration
public class MongoOffConfiguration {
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(new MongoDbFactory() {
            
            @Override
            public PersistenceExceptionTranslator getExceptionTranslator() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public DB getDb(String arg0) throws DataAccessException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public DB getDb() throws DataAccessException {
                // TODO Auto-generated method stub
                return null;
            }
        });
    }
}
