package net.scero.test.db.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import net.scero.test.db.pojos.ExampleDBTuple;

public interface TestDBMapper {
    @InsertProvider(type = UserSqlBuilder.class, method = "createTableIfNotExist")
    public void createTableIfNotExist();

    @Insert("insert into ExampleTable(id, nombre, edad_elemento) VALUES (#{id}, #{exampleDBTuple.nombre}, #{exampleDBTuple.edadElemento})")
    public void create(@Param("id") int id, @Param("exampleDBTuple") ExampleDBTuple exampleDBTuple);

    @Select("select id, nombre, edad_elemento from ExampleTable where nombre like '%' + #{nombre} + '%'")
    public List<ExampleDBTuple> findAll(@Param("nombre") final String nombre);

    
    /**
     * Construye las sentencias sql
     * Se puede usar el objeto SQL para crear querys complejas
     */
    class UserSqlBuilder {
        public String createTableIfNotExist() {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE if not exists ExampleTable (").append(System.lineSeparator());
            sb.append("id NUMERIC(8) not null,").append(System.lineSeparator());
            sb.append("nombre VARCHAR(30) NOT NULL,").append(System.lineSeparator());
            sb.append("edad_elemento numeric(3),").append(System.lineSeparator());
            sb.append("PRIMARY KEY (id)").append(System.lineSeparator());
            sb.append(");");
            return sb.toString();
        }
    }
}
