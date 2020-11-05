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

- @NoRepositoryBean  
  
  
          
## 3.2 方法命名查询

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

支持查询的关键字


| Logical keyword       | Keyword expressions                            |
| :-------------------- | :--------------------------------------------- |
| `AND`                 | `And`                                          |
| `OR`                  | `Or`                                           |
| `AFTER`               | `After`, `IsAfter`                             |
| `BEFORE`              | `Before`, `IsBefore`                           |
| `CONTAINING`          | `Containing`, `IsContaining`, `Contains`       |
| `BETWEEN`             | `Between`, `IsBetween`                         |
| `ENDING_WITH`         | `EndingWith`, `IsEndingWith`, `EndsWith`       |
| `EXISTS`              | `Exists`                                       |
| `FALSE`               | `False`, `IsFalse`                             |
| `GREATER_THAN`        | `GreaterThan`, `IsGreaterThan`                 |
| `GREATER_THAN_EQUALS` | `GreaterThanEqual`, `IsGreaterThanEqual`       |
| `IN`                  | `In`, `IsIn`                                   |
| `IS`                  | `Is`, `Equals`, (or no keyword)                |
| `IS_EMPTY`            | `IsEmpty`, `Empty`                             |
| `IS_NOT_EMPTY`        | `IsNotEmpty`, `NotEmpty`                       |
| `IS_NOT_NULL`         | `NotNull`, `IsNotNull`                         |
| `IS_NULL`             | `Null`, `IsNull`                               |
| `LESS_THAN`           | `LessThan`, `IsLessThan`                       |
| `LESS_THAN_EQUAL`     | `LessThanEqual`, `IsLessThanEqual`             |
| `LIKE`                | `Like`, `IsLike`                               |
| `NEAR`                | `Near`, `IsNear`                               |
| `NOT`                 | `Not`, `IsNot`                                 |
| `NOT_IN`              | `NotIn`, `IsNotIn`                             |
| `NOT_LIKE`            | `NotLike`, `IsNotLike`                         |
| `REGEX`               | `Regex`, `MatchesRegex`, `Matches`             |
| `STARTING_WITH`       | `StartingWith`, `IsStartingWith`, `StartsWith` |
| `TRUE`                | `True`, `IsTrue`                               |
| `WITHIN`              | `Within`, `IsWithin`                           |
## 4、 监听器

```java
@Bean
public ApplicationListener<BeforeSaveEvent<Object>> loggingSaves() {

	return event -> {

		Object entity = event.getEntity();
		LOG.info("{} is getting saved.", entity);
	};
}
```


###  todo
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

## 5、 entity 回调
支持回调事件列表

| `EntityCallback`                                             | When It Is Published                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| [`BeforeDeleteCallback`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api/org/springframework/data/relational/core/mapping/event/BeforeDeleteCallback.html) | Before an aggregate root gets deleted.                       |
| [`AfterDeleteCallback`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api/org/springframework/data/relational/core/mapping/event/AfterDeleteCallback.html) | After an aggregate root gets deleted.                        |
| [`BeforeConvertCallback`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api//org/springframework/data/relational/core/mapping/event/BeforeConvertCallback.html) | Before an aggregate root gets saved (that is, inserted or updated but after the decision about whether if it gets updated or deleted was made). |
| [`BeforeSaveCallback`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api//org/springframework/data/relational/core/mapping/event/BeforeSaveCallback.html) | Before an aggregate root gets saved (that is, inserted or updated but after the decision about whether if it gets updated or deleted was made). |
| [`AfterSaveCallback`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api/org/springframework/data/relational/core/mapping/event/AfterSaveCallback.html) | After an aggregate root gets saved (that is, inserted or updated). |
| [`AfterLoadCallback`](https://docs.spring.io/spring-data/jdbc/docs/2.1.0/api/org/springframework/data/relational/core/mapping/event/AfterLoadCallback.html) | After an aggregate root gets created from a database `ResultSet` and all its property get set. |

### 5.1、注册回调
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
## 自定义 转换
写时转换
```java
import org.springframework.core.convert.converter.Converter;

@WritingConverter
public class BooleanToStringConverter implements Converter<Boolean, String> {

    @Override
    public String convert(Boolean source) {
        return source != null && source ? "T" : "F";
    }
}
```
从数据库
```java
@ReadingConverter
public class StringToBooleanConverter implements Converter<String, Boolean> {

    @Override
    public Boolean convert(String source) {
        return source != null && source.equalsIgnoreCase("T") ? Boolean.TRUE : Boolean.FALSE;
    }
}
```
### 注册到 spring 
```java
class MyJdbcConfiguration extends AbstractJdbcConfiguration {

    // …

    @Overwrite
    @Bean
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(Arrays.asList(new BooleanToStringConverter(), new StringToBooleanConverter()));
    }
}
```

## 日志处理
 todo
 
## 事务管理 

事务控制枚举 `@Transactional`

```java

public @interface Transactional {

    // 事务管理器名称
	@AliasFor("transactionManager")
	String value() default "";

	 // 事务管理器名称
	String transactionManager() default "";

 
	String[] label() default {};
    
    /**
        事务传播级别
        
             REQUIRED：支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。 
            
             SUPPORTS：支持当前事务，如果当前没有事务，就以非事务方式执行。 
            
             MANDATORY：支持当前事务，如果当前没有事务，就抛出异常。 
            
             REQUIRES_NEW：新建事务，如果当前存在事务，把当前事务挂起。 
            
             NOT_SUPPORTED：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。 
            
             NEVER：以非事务方式执行，如果当前存在事务，则抛出异常。 
            
             NESTED：支持当前事务，如果当前事务存在，则执行一个嵌套事务，如果当前没有事务，就新建一个事务。

    **/

	Propagation propagation() default Propagation.REQUIRED;


    /**
       事务隔离级别

            未提交读取（Read Uncommitted）
            Spring标识：ISOLATION_READ_UNCOMMITTED。允许脏读取，但不允许更新丢失。如果一个事务已经开始写数据，则另外一个事务则不允许同时进行写操作，但允许其他事务读此行数据。该隔离级别可以通过“排他写锁”实现。
            
            已提交读取（Read Committed）
            Spring标识：ISOLATION_READ_COMMITTED。允许不可重复读取，但不允许脏读取。这可以通过“瞬间共享读锁”和“排他写锁”实现。读取数据的事务允许其他事务继续访问该行数据，但是未提交的写事务将会禁止其他事务访问该行。
            
            可重复读取（Repeatable Read）
            Spring标识：ISOLATION_REPEATABLE_READ。禁止不可重复读取和脏读取，但是有时可能出现幻读数据。这可以通过“共享读锁”和“排他写锁”实现。读取数据的事务将会禁止写事务（但允许读事务），写事务则禁止任何其他事务。
            
            序列化（Serializable）
            Spring标识：ISOLATION_SERIALIZABLE。提供严格的事务隔离。它要求事务序列化执行，事务只能一个接着一个地执行，不能并发执行。仅仅通过“行级锁”是无法实现事务序列化的，必须通过其他机制保证新插入的数据不会被刚执行查询操作的事务访问到。
            
            隔离级别越高，越能保证数据的完整性和一致性，但是对并发性能的影响也越大。对于多数应用程序，可以优先考虑把数据库系统的隔离级别设为Read Committed。它能够避免脏读取，而且具有较好的并发性能。尽管它会导致不可重复读、幻读和第二类丢失更新这些并发问题，在可能出现这类问题的个别场合，可以由应用程序采用悲观锁或乐观锁来控制。
            
            Spring中同时提供一个标识：ISOLATION_DEFAULT。表示使用后端数据库默认的隔离级别。大多数数据库默认的事务隔离级别是Read committed，比如Sql Server , Oracle。MySQL的默认隔离级别是Repeatable read。
    **/
	Isolation isolation() default Isolation.DEFAULT;

	/**
            超时时间
	 */
	int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

 
	String timeoutString() default "";

	/**
	    是否只读
	 */
	boolean readOnly() default false;

	/**
	    指定回滚的异常
	 */
	Class<? extends Throwable>[] rollbackFor() default {};

	/**
	  指定回滚的异常
	 */
	String[] rollbackForClassName() default {};

	/**
	    指定发生某个异常但是不会滚事务
	 */
	Class<? extends Throwable>[] noRollbackFor() default {};

	/**
	    指定发生某个异常但是不会滚事务
	 */
	String[] noRollbackForClassName() default {};

}

```

## 审计功能
提供的枚举
- @CreatedDate：创建时间
- @LastModifiedDate：最后更新时间
 -@CreatedBy：创建人信息
- @LastModifiedBy：最后更新人信息

### 注册和声明


```java
@Configuration
@EnableJdbcAuditing
class Config {

  @Bean
  public AuditorAware<AuditableUser> auditorProvider() {
    return new AuditorAwareImpl();
  }
}
```
