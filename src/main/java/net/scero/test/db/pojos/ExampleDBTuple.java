package net.scero.test.db.pojos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExampleDBTuple {
    private int id;
    private String nombre;
    private int edadElemento;
}
