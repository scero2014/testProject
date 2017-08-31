package net.scero.test.mongodb;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class EntityTest {
    //---- Variables ----//
    @Id
    private String id;
    
    private String name;
    private Integer age;
    
    public EntityTest(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
