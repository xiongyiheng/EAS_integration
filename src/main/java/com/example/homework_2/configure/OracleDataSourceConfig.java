package com.example.homework_2.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "oracleEntityManagerFactory", transactionManagerRef = "oracleTransactionManager", basePackages = {
        "com.example.homework_2.oracle.repository"})
public class OracleDataSourceConfig {
    @Autowired
    private HibernateProperties hibernateProperties;
    @Resource
    @Qualifier("oracleDataSource")
    private DataSource oracleDataSource;

    @Resource
    private JpaProperties jpaProperties;

    @Bean(name = "entityManagerOracle")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return oracleEntityManagerFactory(builder).getObject().createEntityManager();
    }

    /**
     * 设置实体类所在位置
     */
    @Bean(name = "oracleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String,Object> properties = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(),new HibernateSettings());
        return builder.dataSource(oracleDataSource)
                .packages("com.example.homework_2.oracle.entity")
                .persistenceUnit("oraclePersistenceUnit")
                .properties(properties)
                .build();
    }

    //private Map<String, Object> getProperties() {
     //   return jpaProperties.getHibernateProperties(new HibernateSettings());
    //}

    @Bean(name = "oracleTransactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(oracleEntityManagerFactory(builder).getObject());
    }
}