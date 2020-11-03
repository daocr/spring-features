# 1、 功能概述

[TOC]

# 2、 准备工作

添加依赖
```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-jdbc</artifactId>
    <version>2.1.0</version>
  </dependency>
</dependencies>
```

```xml
<repositories>
  <repository>
    <id>spring-milestone</id>
    <name>Spring Maven MILESTONE Repository</name>
    <url>https://repo.spring.io/libs-milestone</url>
  </repository>
</repositories>

```
# 3 功能点学习

## 3.1 默认配置


框架类默认实现 AbstractJdbcConfiguration
``` java
public class AbstractJdbcConfiguration {
 

    /***
        映射上下文
    **/ 
    @Bean
    public JdbcMappingContext jdbcMappingContext(Optional<NamingStrategy> namingStrategy, JdbcCustomConversions customConversions) {
        // 设置命名策略
        JdbcMappingContext mappingContext = new JdbcMappingContext((NamingStrategy)namingStrategy.orElse(NamingStrategy.INSTANCE));
        mappingContext.setSimpleTypeHolder(customConversions.getSimpleTypeHolder());
        return mappingContext;
    }

    @Bean
    public JdbcConverter jdbcConverter(JdbcMappingContext mappingContext, NamedParameterJdbcOperations operations, @Lazy RelationResolver relationResolver, JdbcCustomConversions conversions, Dialect dialect) {
        DefaultJdbcTypeFactory jdbcTypeFactory = new DefaultJdbcTypeFactory(operations.getJdbcOperations());
        return new BasicJdbcConverter(mappingContext, relationResolver, conversions, jdbcTypeFactory, dialect.getIdentifierProcessing());
    }

    @Bean
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions();
    }

    @Bean
    public JdbcAggregateTemplate jdbcAggregateTemplate(ApplicationContext applicationContext, JdbcMappingContext mappingContext, JdbcConverter converter, DataAccessStrategy dataAccessStrategy) {
        return new JdbcAggregateTemplate(applicationContext, mappingContext, converter, dataAccessStrategy);
    }

    @Bean
    public DataAccessStrategy dataAccessStrategyBean(NamedParameterJdbcOperations operations, JdbcConverter jdbcConverter, JdbcMappingContext context, Dialect dialect) {
        return new DefaultDataAccessStrategy(new SqlGeneratorSource(context, jdbcConverter, dialect), context, jdbcConverter, operations);
    }

    @Bean
    public Dialect jdbcDialect(NamedParameterJdbcOperations operations) {
        return DialectResolver.getDialect(operations.getJdbcOperations());
    }
}
```


自定义配置信息
```java
@Configuration
@EnableJdbcRepositories                                                                
class ApplicationConfig extends AbstractJdbcConfiguration {                            

    /***
        设置数据源
    */   
    @Bean
    public DataSource dataSource() {                                                   
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.HSQL).build();
    }

    /**
        构建   JdbcTemplate
     */       
    @Bean
    NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource) { 
        return new NamedParameterJdbcTemplate(dataSource);
    }
    /**
        设置事物管理器
    */
    @Bean
    TransactionManager transactionManager(DataSource dataSource) {                     
        return new DataSourceTransactionManager(dataSource);
    }
}

```
