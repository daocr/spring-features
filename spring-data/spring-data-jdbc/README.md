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

    /**
            配置jdbc 类型转换
     **/
    @Bean
    public JdbcConverter jdbcConverter(JdbcMappingContext mappingContext, NamedParameterJdbcOperations operations, @Lazy RelationResolver relationResolver, JdbcCustomConversions conversions, Dialect dialect) {
        DefaultJdbcTypeFactory jdbcTypeFactory = new DefaultJdbcTypeFactory(operations.getJdbcOperations());
        return new BasicJdbcConverter(mappingContext, relationResolver, conversions, jdbcTypeFactory, dialect.getIdentifierProcessing());
    }

    /**
        自定义类型转换器
    */
    @Bean
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions();
    }

    @Bean
    public JdbcAggregateTemplate jdbcAggregateTemplate(ApplicationContext applicationContext, JdbcMappingContext mappingContext, JdbcConverter converter, DataAccessStrategy dataAccessStrategy) {
        return new JdbcAggregateTemplate(applicationContext, mappingContext, converter, dataAccessStrategy);
    }

    /** 数据库访问策略
    **/
    @Bean
    public DataAccessStrategy dataAccessStrategyBean(NamedParameterJdbcOperations operations, JdbcConverter jdbcConverter, JdbcMappingContext context, Dialect dialect) {
        return new DefaultDataAccessStrategy(new SqlGeneratorSource(context, jdbcConverter, dialect), context, jdbcConverter, operations);
    }
    
    /**
        数据库方言
    **/
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
## 3.2 常用枚举

- @Table
  - aa
- @Column
  - ff    
- @Id
  - 主键
- @MappedCollection
  - aa
- @Embedded
  - 嵌套对象
- @Query("select firstName, lastName from User u where u.emailAddress = :email")
  - @Param("email")
- @Modifying
  - 修改语句   
  
  
          
## 方法命名查询

| Keyword                              | Sample                                      | Logical result                     |
| :----------------------------------- | :------------------------------------------ | :--------------------------------- |
| `After`                              | `findByBirthdateAfter(Date date)`           | `birthdate > date`                 |
| `GreaterThan`                        | `findByAgeGreaterThan(int age)`             | `age > age`                        |
| `GreaterThanEqual`                   | `findByAgeGreaterThanEqual(int age)`        | `age >= age`                       |
| `Before`                             | `findByBirthdateBefore(Date date)`          | `birthdate < date`                 |
| `LessThan`                           | `findByAgeLessThan(int age)`                | `age < age`                        |
| `LessThanEqual`                      | `findByAgeLessThanEqual(int age)`           | `age <= age`                       |
| `Between`                            | `findByAgeBetween(int from, int to)`        | `age BETWEEN from AND to`          |
| `NotBetween`                         | `findByAgeBetween(int from, int to)`        | `age NOT BETWEEN from AND to`      |
| `In`                                 | `findByAgeIn(Collection<Integer> ages)`     | `age IN (age1, age2, ageN)`        |
| `NotIn`                              | `findByAgeNotIn(Collection ages)`           | `age NOT IN (age1, age2, ageN)`    |
| `IsNotNull`, `NotNull`               | `findByFirstnameNotNull()`                  | `firstname IS NOT NULL`            |
| `IsNull`, `Null`                     | `findByFirstnameNull()`                     | `firstname IS NULL`                |
| `Like`, `StartingWith`, `EndingWith` | `findByFirstnameLike(String name)`          | `firstname LIKE name`              |
| `NotLike`, `IsNotLike`               | `findByFirstnameNotLike(String name)`       | `firstname NOT LIKE name`          |
| `Containing` on String               | `findByFirstnameContaining(String name)`    | `firstname LIKE '%' name +'%'`     |
| `NotContaining` on String            | `findByFirstnameNotContaining(String name)` | `firstname NOT LIKE '%' name +'%'` |
| `(No keyword)`                       | `findByFirstname(String name)`              | `firstname = name`                 |
| `Not`                                | `findByFirstnameNot(String name)`           | `firstname != name`                |
| `IsTrue`, `True`                     | `findByActiveIsTrue()`                      | `active IS TRUE`                   |
| `IsFalse`, `False`                   | `findByActiveIsFalse()`                     | `active IS FALSE`                  |

## 监听器

```java
@Bean
public ApplicationListener<BeforeSaveEvent<Object>> loggingSaves() {

	return event -> {

		Object entity = event.getEntity();
		LOG.info("{} is getting saved.", entity);
	};
}
```


###  
```java
public class PersonLoadListener extends AbstractRelationalEventListener<Person> {

	@Override
	protected void onAfterLoad(AfterLoadEvent<Person> personLoad) {
		LOG.info(personLoad.getEntity());
	}
}
```

| Event                                                        | When It Is Published                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [`BeforeDeleteEvent`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api/org/springframework/data/relational/core/mapping/event/BeforeDeleteEvent.html) | Before an aggregate root gets deleted.                       |
| [`AfterDeleteEvent`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api/org/springframework/data/relational/core/mapping/event/AfterDeleteEvent.html) | After an aggregate root gets deleted.                        |
| [`BeforeConvertEvent`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api//org/springframework/data/relational/core/mapping/event/BeforeConvertEvent.html) | Before an aggregate root gets saved (that is, inserted or updated but after the decision about whether if it gets updated or deleted was made). |
| [`BeforeSaveEvent`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api//org/springframework/data/relational/core/mapping/event/BeforeSaveEvent.html) | Before an aggregate root gets saved (that is, inserted or updated but after the decision about whether if it gets updated or deleted was made). |
| [`AfterSaveEvent`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api/org/springframework/data/relational/core/mapping/event/AfterSaveEvent.html) | After an aggregate root gets saved (that is, inserted or updated). |
| [`AfterLoadEvent`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api/org/springframework/data/relational/core/mapping/event/AfterLoadEvent.html) | After an aggregate root gets created from a database `ResultSet` and all its properties get set. |

## entity 回调
支持回调事件列表

| `EntityCallback`                                             | When It Is Published                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [`BeforeDeleteCallback`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api/org/springframework/data/relational/core/mapping/event/BeforeDeleteCallback.html) | Before an aggregate root gets deleted.                       |
| [`AfterDeleteCallback`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api/org/springframework/data/relational/core/mapping/event/AfterDeleteCallback.html) | After an aggregate root gets deleted.                        |
| [`BeforeConvertCallback`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api//org/springframework/data/relational/core/mapping/event/BeforeConvertCallback.html) | Before an aggregate root gets saved (that is, inserted or updated but after the decision about whether if it gets updated or deleted was made). |
| [`BeforeSaveCallback`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api//org/springframework/data/relational/core/mapping/event/BeforeSaveCallback.html) | Before an aggregate root gets saved (that is, inserted or updated but after the decision about whether if it gets updated or deleted was made). |
| [`AfterSaveCallback`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api/org/springframework/data/relational/core/mapping/event/AfterSaveCallback.html) | After an aggregate root gets saved (that is, inserted or updated). |
| [`AfterLoadCallback`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api/org/springframework/data/relational/core/mapping/event/AfterLoadCallback.html) | After an aggregate root gets created from a database `ResultSet` and all its property get set. |

### 注册回调
```java
@Order(1)                                                            
@Component
class First implements BeforeSaveCallback<Person> {

	@Override
	public Person onBeforeSave(Person person) {
		return // ...
	}
}

@Component
class DefaultingEntityCallback implements BeforeSaveCallback<Person>,
                                                           Ordered { 

	@Override
	public Object onBeforeSave(Person entity, String collection) {
		// ...
	}

	@Override
	public int getOrder() {
		return 100;                                                  
	}
}

@Configuration
public class EntityCallbackConfiguration {

    @Bean
    BeforeSaveCallback<Person> unorderedLambdaReceiverCallback() {   
        return (BeforeSaveCallback<Person>) it -> // ...
    }
}

@Component
class UserCallbacks implements BeforeConvertCallback<User>,
                                        BeforeSaveCallback<User> {   

	@Override
	public Person onBeforeConvert(User user) {
		return // ...
	}

	@Override
	public Person onBeforeSave(User user) {
		return // ...
	}
}
```
