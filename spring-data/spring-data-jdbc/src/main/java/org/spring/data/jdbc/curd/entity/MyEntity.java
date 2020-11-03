package org.spring.data.jdbc.curd.entity;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 命名策略：NamingStrategy
 * 填充值策略： @AccessType(AccessType.Type.FIELD)
 */
@Table("my_entity")
@AccessType(AccessType.Type.FIELD)
public class MyEntity {

    /**
     * 指定主键
     */
    @Id
    Integer id;

    /**
     * 自定义 名称
     */
    @Column(value = "my_name")
    String name;
}