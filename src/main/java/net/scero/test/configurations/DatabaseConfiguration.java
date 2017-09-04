package net.scero.test.configurations;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;
import net.scero.test.db.exceptions.DatabaseException;
import net.scero.test.db.handlers.BooleanTypeHandler;
import net.scero.test.db.mappers.TestDBMapper;

@Slf4j
@Configuration
public class DatabaseConfiguration {
    /**
     * Database bean
     *
     * @return
     */
    @Bean(name = "dataSource", destroyMethod = "")
    @ConfigurationProperties(prefix = "datasources.postgresql")
    @Primary
    public DataSource dataSource() {
        return new HikariDataSource();
    }

    /**
     * Factoría sqlSessionFactory
     * 
     * @return SQLSessionFactory postgreSQL
     */
    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws DatabaseException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setTypeHandlers(getTypeHandlers());
        sqlSessionFactoryBean.setDataSource(dataSource);
        try {
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            configuration.setMapUnderscoreToCamelCase(true);
            configuration.setJdbcTypeForNull(JdbcType.NULL);
            setMappers(configuration);
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            log.error("Error creating sqlSessionFactoryReceptivo", e);
            throw new DatabaseException("Error configurando mybatis", e);
        }
    }
    
    /**
     * Bean testDBMapper
     * @param sqlSessionFactory
     * @return
     */
    @Bean
    public TestDBMapper testDBMapper(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        SqlSessionTemplate sessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sessionTemplate.getMapper(TestDBMapper.class);
    }

    
    /**
     * Sobreescritura de handlers para interpretar información
     * @return
     */
    private TypeHandler<?>[] getTypeHandlers() {
        return new TypeHandler<?>[] {
            new BooleanTypeHandler()
        };
    }

    /**
     * Setea mappers en la configuración
     * @param configuration
     */
    private void setMappers(org.apache.ibatis.session.Configuration configuration) {
        configuration.addMapper(TestDBMapper.class);
    }

}
