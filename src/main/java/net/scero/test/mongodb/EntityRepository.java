package net.scero.test.mongodb;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntityRepository extends MongoRepository<EntityTest, String> {
    public List<EntityTest> findByAge(Integer age);
    public List<EntityTest> findByNameAndAge(String name, Integer age);
}
