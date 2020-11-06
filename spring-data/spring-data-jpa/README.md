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

排序例子：
```java
@NoRepositoryBean
interface MyBaseRepository<T, ID> extends Repository<T, ID> {

    Optional<T> findById(ID id);

    <S extends T> S save(S entity);
}
```

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

#表达式




 