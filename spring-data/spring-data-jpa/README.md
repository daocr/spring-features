# 1、 功能概述

[TOC]

# 2、 准备工作

## 2.2 依赖准备

```xml
<dependencies>
    <!-- https://mvnrepository.com/artifact/org.springframework.data/spring-data-jpa -->
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-jpa</artifactId>
        <version>2.4.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/javax.persistence/persistence-api -->
    <dependency>
        <groupId>javax.persistence</groupId>
        <artifactId>persistence-api</artifactId>
        <version>1.0.2</version>
    </dependency>
</dependencies>

```

 
# 3 功能点学习

## 核心 

### 配置
```java
@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
class ApplicationConfig {

  @Bean
  public DataSource dataSource() {

    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    return builder.setType(EmbeddedDatabaseType.HSQL).build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("com.acme.domain");
    factory.setDataSource(dataSource());
    return factory;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

    JpaTransactionManager txManager = new JpaTransactionManager();
    txManager.setEntityManagerFactory(entityManagerFactory);
    return txManager;
  }
}
```

### 持久化 repository 

1、xml 注册 repo 到spring
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans:beans xmlns:beans="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns="http://www.springframework.org/schema/data/jpa"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
    https://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/data/jpa
    https://www.springframework.org/schema/data/jpa/spring-jpa.xsd">

    <repositories base-package="com.acme.repositories">
      <context:exclude-filter type="regex" expression=".*SomeRepository" />
    </repositories>

</beans:beans>
```
2、注解 注册到 spring
```java
@Configuration
@EnableJpaRepositories("com.acme.repositories")
class ApplicationConfiguration {

  @Bean
  EntityManagerFactory entityManagerFactory() {
    // …
  }
}
```
3、收到注册到 spring

```
@Component("specialCustomImpl")
class CustomizedUserRepositoryImpl implements CustomizedUserRepository {

  // Your custom implementation
}
```
## 普通查询

### 1、CrudRepository 普通crud 

```java
@NoRepositoryBean
public interface CrudRepository<T, ID> extends Repository<T, ID> {

	/**
	
        保存一个
	 */
	<S extends T> S save(S entity);

	/**
        保存多个
	 */
	<S extends T> Iterable<S> saveAll(Iterable<S> entities);

	/**
        主键 查询
	 */
	Optional<T> findById(ID id);

	/**
	   主键判断是否存在
	 */
	boolean existsById(ID id);

	/**
        查询全部
	 */
	Iterable<T> findAll();

	/**
	    多主键 查询
	 */
	Iterable<T> findAllById(Iterable<ID> ids);

	/**

        无条件，统计数量
	 */
	long count();

	/**
	    主键删除
	 */
	void deleteById(ID id);

	/**
	    entity 条件删除
	 */
	void delete(T entity);

	/**
	    多 entity 条件删除
	 */
	void deleteAll(Iterable<? extends T> entities);

	/**
	    删除全部
	 */
	void deleteAll();
}
```

### 2、PagingAndSortingRepository 分页

```java
@NoRepositoryBean
public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {

	/**
        排序查询
	 */
	Iterable<T> findAll(Sort sort);

	/**
        分页查询
	 */
	Page<T> findAll(Pageable pageable);
}

```

排序例子：
```java
// 方式1
Sort sort = Sort.by("firstname").ascending()
  .and(Sort.by("lastname").descending());

// 方式2
TypedSort<Person> person = Sort.sort(Person.class);

Sort sort = person.by(Person::getFirstname).ascending()
  .and(person.by(Person::getLastname).descending());

// 方式3
QSort sort = QSort.by(QPerson.firstname.asc())
  .and(QSort.by(QPerson.lastname.desc()));

```

分页 查询
```java
// 1、普通分页
   PageRequest.of(1,10);

// 2、分页带排序
    Sort sort = Sort.by("firstname").ascending()
            .and(Sort.by("lastname").descending());
    PageRequest.of(1,10,sort);

```


### 3、自定义 查询


```java
@NoRepositoryBean
interface MyBaseRepository<T, ID> extends Repository<T, ID> {

    Optional<T> findById(ID id);

    <S extends T> S save(S entity);
}
```

### 流式查询 Query 

```java
@Query("select u from User u")
Stream<User> findAllByCustomQueryAndStream();

Stream<User> readAllByFirstnameNotNull();

@Query("select u from User u")
Stream<User> streamAllPaged(Pageable pageable);
```
异步查询
```java
@Async
Future<User> findByFirstname(String firstname);               

@Async
CompletableFuture<User> findOneByFirstname(String firstname); 

@Async
ListenableFuture<User> findOneByLastname(String lastname);    
```

注解  操作
```java
public interface UserRepository extends JpaRepository<User, Long> {

  //hql 查询
  @Query("select u from User u where u.emailAddress = ?1")
  User findByEmailAddress(String emailAddress);

  // hql 模糊查询
  @Query("select u from User u where u.firstname like %?1")
  List<User> findByFirstnameEndsWith(String firstname);

  // 本地sql 查询
  @Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
  User findByEmailAddress(String emailAddress);

  //本地sql  分页查询
  @Query(value = "SELECT * FROM USERS WHERE LASTNAME = ?1",
    countQuery = "SELECT count(*) FROM USERS WHERE LASTNAME = ?1",
    nativeQuery = true);

  // 排序查询
  @Query("select u from User u where u.lastname like ?1%")
  List<User> findByAndSort(String lastname, Sort sort);

  // 占位符查询
  @Query("select u from User u where u.firstname = :firstname or u.lastname = :lastname")
  User findByLastnameOrFirstname(@Param("lastname") String lastname,
                                 @Param("firstname") String firstname);

  // spring el 表达式查询  
  @Query("select u from #{#entityName} u where u.lastname = ?1")
  List<User> findByLastname(String lastname);

 //修改数据
  @Modifying
  @Query("update User u set u.firstname = ?1 where u.lastname = ?2")
  int setFixedFirstnameFor(String firstname, String lastname);


  // 删除操作
  @Modifying
  @Query("delete from User u where u.role.id = ?1")
  void deleteInBulkByRoleId(long roleId);

  // 存储过程
  @QueryHints(value = { @QueryHint(name = "name", value = "value")},
              forCounting = false)
  Page<User> findByLastname(String lastname, Pageable pageable);

   // example 查询
   Example<User> example = Example.of(new User(null, "White", null));
   repository.count(example);
}
```

# 命名查询

| Keyword                | Sample                                                       | JPQL snippet                                                 |
| :--------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| `Distinct`             | `findDistinctByLastnameAndFirstname`                         | `select distinct … where x.lastname = ?1 and x.firstname = ?2` |
| `And`                  | `findByLastnameAndFirstname`                                 | `… where x.lastname = ?1 and x.firstname = ?2`               |
| `Or`                   | `findByLastnameOrFirstname`                                  | `… where x.lastname = ?1 or x.firstname = ?2`                |
| `Is`, `Equals`         | `findByFirstname`,`findByFirstnameIs`,`findByFirstnameEquals` | `… where x.firstname = ?1`                                   |
| `Between`              | `findByStartDateBetween`                                     | `… where x.startDate between ?1 and ?2`                      |
| `LessThan`             | `findByAgeLessThan`                                          | `… where x.age < ?1`                                         |
| `LessThanEqual`        | `findByAgeLessThanEqual`                                     | `… where x.age <= ?1`                                        |
| `GreaterThan`          | `findByAgeGreaterThan`                                       | `… where x.age > ?1`                                         |
| `GreaterThanEqual`     | `findByAgeGreaterThanEqual`                                  | `… where x.age >= ?1`                                        |
| `After`                | `findByStartDateAfter`                                       | `… where x.startDate > ?1`                                   |
| `Before`               | `findByStartDateBefore`                                      | `… where x.startDate < ?1`                                   |
| `IsNull`, `Null`       | `findByAge(Is)Null`                                          | `… where x.age is null`                                      |
| `IsNotNull`, `NotNull` | `findByAge(Is)NotNull`                                       | `… where x.age not null`                                     |
| `Like`                 | `findByFirstnameLike`                                        | `… where x.firstname like ?1`                                |
| `NotLike`              | `findByFirstnameNotLike`                                     | `… where x.firstname not like ?1`                            |
| `StartingWith`         | `findByFirstnameStartingWith`                                | `… where x.firstname like ?1` (parameter bound with appended `%`) |
| `EndingWith`           | `findByFirstnameEndingWith`                                  | `… where x.firstname like ?1` (parameter bound with prepended `%`) |
| `Containing`           | `findByFirstnameContaining`                                  | `… where x.firstname like ?1` (parameter bound wrapped in `%`) |
| `OrderBy`              | `findByAgeOrderByLastnameDesc`                               | `… where x.age = ?1 order by x.lastname desc`                |
| `Not`                  | `findByLastnameNot`                                          | `… where x.lastname <> ?1`                                   |
| `In`                   | `findByAgeIn(Collection<Age> ages)`                          | `… where x.age in ?1`                                        |
| `NotIn`                | `findByAgeNotIn(Collection<Age> ages)`                       | `… where x.age not in ?1`                                    |
| `True`                 | `findByActiveTrue()`                                         | `… where x.active = true`                                    |
| `False`                | `findByActiveFalse()`                                        | `… where x.active = false`                                   |
| `IgnoreCase`           | `findByFirstnameIgnoreCase`                                  | `… where UPPER(x.firstname) = UPPER(?1)`                     |





#监听器

## 定义事件
```java
@Data
public class Users {

    private Integer id;
    private String name;

    //该方法会在 Users.save()调用时被触发调用
    @DomainEvents
    Collection<UsersSaveEvent> domainEvents() {
        return Arrays.asList(new UsersSaveEvent(this.id));
    }

}
````
## 触发事件

`TransactionalEventListener#phase` 触发时机
- BEFORE_COMMIT
  - 事务提交前触发
- AFTER_COMMIT
  - 事物提交后出发  
- AFTER_ROLLBACK
  - 事务回滚前触发
- AFTER_COMPLETION
  - 事务回滚后出发

```java
@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

	//接受Users发出的类型为UserSaveEvent的DomainEvents事件
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void event(UsersSaveEvent event){
        System.out.println(userRepository.getOne(event.getId()));
    }

}
```


 