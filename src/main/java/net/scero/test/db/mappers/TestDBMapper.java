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

    @Insert("insert into aif2_t_client_classification(id, nombre, edad_elemento) VALUES (#{id}, #{exampleDBTuple.nombre}, #{exampleDBTuple.edadElemento})")
    public void create(@Param("id") int id, @Param("exampleDBTuple") ExampleDBTuple exampleDBTuple);

    @Select("select id, nombre, edad_elemento from ExampleTable where nombre like '%nombre%'")
    public List<ExampleDBTuple> findAll(@Param("nombre") final String nombre);

    
    /**
     * Construye las sentencias sql
     * Se puede usar el objeto SQL para crear querys complejas
     */
    class UserSqlBuilder {
        public String createTableIfNotExist() {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE if not exists ExampleTable {");
            sb.append("id NUMERIC(8) not null,");
            sb.append("nombre VARCHAR(3) NOT NULL,");
            sb.append("edad_elemento numeric(3),");
            sb.append("PRIMARY KEY (id)");
            sb.append("};");
            return sb.toString();
        }
    }
}
