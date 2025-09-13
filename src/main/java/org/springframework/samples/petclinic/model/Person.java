/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;

/**
 * Simple JavaBean domain object representing an person.
 *
 * @author Ken Krebs
 */
@MappedSuperclass
public class Person extends BaseEntity {

	/**
	 * 人员名字字段
	 * 使用 @Column 注解指定数据库列名为 "first_name"，遵循数据库命名规范（下划线分隔）
	 * 使用 @NotBlank 注解进行验证，确保名字不能为空或只包含空白字符
	 * 这是 JPA 实体类中的标准写法，用于映射数据库表结构
	 * 
	 * JPA (Java Persistence API) 是什么：
	 * 1. JPA 是 Java 平台的标准规范，用于管理 Java 对象与关系数据库之间的映射
	 * 2. 它是 ORM (Object-Relational Mapping) 技术的 Java 标准实现
	 * 3. JPA 提供了一套注解和 API，让开发者可以用面向对象的方式操作数据库
	 * 4. 主要实现包括 Hibernate、EclipseLink、OpenJPA 等
	 * 5. Spring Boot 默认集成 Hibernate 作为 JPA 实现
	 * 
	 * 为什么可以用 @Column：
	 * 1. @Column 是 JPA 规范中定义的注解，用于指定实体属性与数据库列的映射关系
	 * 2. JPA 框架通过反射机制读取注解信息，在运行时建立对象与数据库的映射关系
	 * 3. 当 JPA 处理这个实体时，会自动将 firstName 属性映射到数据库的 first_name 列
	 * 4. 如果不使用 @Column，JPA 会使用默认的命名策略（通常是驼峰命名转下划线）
	 * 5. 显式使用 @Column 可以确保数据库列名的一致性，避免因命名策略变化导致的问题
	 * 6. 注解在编译时被保留，运行时通过反射可以获取注解信息
	 * 7. Spring Boot 自动配置了 JPA，所以这些注解会被自动识别和处理
	 * 
	 * 反射机制是什么：
	 * 1. 反射是 Java 语言的一个特性，允许程序在运行时检查和操作类、方法、字段等
	 * 2. 通过反射，可以在运行时获取类的信息，包括类名、父类、接口、方法、字段等
	 * 3. 反射可以动态创建对象、调用方法、访问字段，即使这些成员是私有的
	 * 4. JPA 使用反射机制读取实体类上的注解信息，如 @Column、@Entity 等
	 * 5. 反射的工作原理：通过 Class 对象获取类的元数据，然后通过 Method、Field 等对象操作
	 * 6. 反射的优缺点：优点是可以实现动态编程，缺点是性能开销较大，安全性较低
	 * 7. 在 JPA 中，反射用于：读取注解配置、创建实体对象、设置属性值、获取属性值等
	 * 
	 * @MappedSuperclass 是什么：
	 * 1. @MappedSuperclass 是 JPA 中的一个重要注解，用于标记一个类作为其他实体类的父类
	 * 2. 与 @Entity 不同，@MappedSuperclass 标记的类本身不会映射到数据库表
	 * 3. 它的作用是定义公共的属性和映射信息，供子类继承使用
	 * 4. 子类会继承父类的所有 JPA 注解和属性，包括 @Column、@Id 等
	 * 5. 这种设计模式实现了代码复用，避免了重复定义相同的字段和注解
	 * 6. 在继承层次中，只有最终的子类（用 @Entity 标记）才会映射到数据库表
	 * 7. 父类的属性会成为子类对应数据库表的一部分
	 * 8. 这种设计特别适合有共同字段的实体类，如 id、创建时间、更新时间等
	 * 9. 在 Spring PetClinic 项目中，Person 类被 Owner 和 Vet 等实体类继承
	 * 10. 这样 Owner 和 Vet 表都会有 first_name 和 last_name 列，但不需要重复定义
	 * 
	 * @NotBlank 是什么：
	 * 1. @NotBlank 是 Bean Validation (JSR-303/JSR-380) 规范中定义的验证注解
	 * 2. 它用于验证字符串字段不能为 null、空字符串("")或只包含空白字符
	 * 3. @NotBlank 是 @NotNull、@NotEmpty 和 @NotBlank 三个注解中最严格的
	 * 4. @NotNull：只检查不为 null
	 * 5. @NotEmpty：检查不为 null 且不为空字符串，但允许只包含空白字符
	 * 6. @NotBlank：检查不为 null、不为空字符串且不全是空白字符
	 * 7. 在 Spring Boot 中，Bean Validation 会自动集成，无需额外配置
	 * 8. 验证会在以下时机触发：表单提交、REST API 调用、JPA 持久化等
	 * 9. 如果验证失败，会抛出 ConstraintViolationException 或 MethodArgumentNotValidException
	 * 10. 可以通过 @Valid 注解在方法参数上启用验证
	 * 11. 验证错误信息可以通过 message 属性自定义，支持国际化
	 * 12. 在 JPA 实体类中使用 @NotBlank 可以确保数据完整性，防止无效数据进入数据库
	 * 13. 这种验证是声明式的，代码简洁，易于维护
	 * 14. Spring Boot 会自动处理验证错误，并返回适当的错误响应
	 */
	@Column(name = "first_name")
	@NotBlank
	private String firstName;

	/**
	 * 人员姓氏字段
	 * 使用 @Column 注解指定数据库列名为 "last_name"，与名字字段保持一致的命名风格
	 * 使用 @NotBlank 注解进行验证，确保姓氏不能为空或只包含空白字符
	 * 这种设计允许在数据库层面和业务逻辑层面都进行数据完整性约束
	 * 
	 * JPA 的工作原理：
	 * 1. 实体类：用 @Entity 或 @MappedSuperclass 标记的 Java 类
	 * 2. 属性映射：用 @Column 等注解标记的属性，指定与数据库列的对应关系
	 * 3. 持久化上下文：JPA 维护一个内存中的对象状态管理区域
	 * 4. 自动 SQL 生成：JPA 根据注解配置自动生成 SQL 语句
	 * 5. 事务管理：与 Spring 事务管理集成，确保数据一致性
	 * 
	 * 为什么可以用 @Column：
	 * 1. JPA 框架通过反射机制读取注解信息，在运行时建立对象与数据库的映射关系
	 * 2. @Column 注解是 JPA 规范的一部分，被所有 JPA 实现（如 Hibernate）支持
	 * 3. 注解在编译时被保留，运行时通过反射可以获取注解信息
	 * 4. Spring Boot 自动配置了 JPA，所以这些注解会被自动识别和处理
	 * 5. 这种注解驱动的方式比传统的 XML 配置更简洁，也更容易维护
	 * 6. JPA 会根据这些注解自动处理对象的持久化、查询、更新和删除操作
	 * 
	 * 反射机制在 JPA 中的具体应用：
	 * 1. 类加载时：JPA 扫描所有类，通过反射检查哪些类有 @Entity 或 @MappedSuperclass 注解
	 * 2. 注解解析：通过反射获取类、字段、方法上的注解信息，如 @Column、@Id 等
	 * 3. 元数据构建：根据注解信息构建实体元数据，包括表名、列名、数据类型等
	 * 4. 对象创建：通过反射调用构造函数创建实体对象实例
	 * 5. 属性访问：通过反射设置和获取实体对象的属性值，即使属性是私有的
	 * 6. 代理生成：Hibernate 通过反射生成代理对象，实现懒加载等功能
	 * 7. 性能优化：JPA 会在启动时完成大部分反射操作，运行时直接使用缓存的结果
	 * 
	 * @MappedSuperclass 的具体作用：
	 * 1. 代码复用：避免在多个实体类中重复定义相同的字段和注解
	 * 2. 维护性：当需要修改公共字段时，只需要在父类中修改一次
	 * 3. 一致性：确保所有子类都有相同的字段定义和验证规则
	 * 4. 继承层次：支持多层继承，子类可以继续被其他类继承
	 * 5. 数据库设计：子类的数据库表会包含父类的所有字段
	 * 6. 查询优化：JPA 可以基于继承关系优化查询性能
	 * 7. 多态支持：支持面向对象的多态特性，可以统一处理不同类型的实体
	 * 8. 在 PetClinic 项目中，Person 类定义了人员的基本信息，Owner 和 Vet 都继承它
	 * 9. 这样设计的好处是：Owner 和 Vet 都有姓名信息，但可以有不同的业务逻辑
	 * 10. 数据库表结构：Owner 表和 Vet 表都会有 first_name 和 last_name 列
	 * 
	 * @NotBlank 是什么：
	 * 1. @NotBlank 是 Bean Validation (JSR-303/JSR-380) 规范中定义的验证注解
	 * 2. 它用于验证字符串字段不能为 null、空字符串("")或只包含空白字符
	 * 3. @NotBlank 是 @NotNull、@NotEmpty 和 @NotBlank 三个注解中最严格的
	 * 4. @NotNull：只检查不为 null
	 * 5. @NotEmpty：检查不为 null 且不为空字符串，但允许只包含空白字符
	 * 6. @NotBlank：检查不为 null、不为空字符串且不全是空白字符
	 * 7. 在 Spring Boot 中，Bean Validation 会自动集成，无需额外配置
	 * 8. 验证会在以下时机触发：表单提交、REST API 调用、JPA 持久化等
	 * 9. 如果验证失败，会抛出 ConstraintViolationException 或 MethodArgumentNotValidException
	 * 10. 可以通过 @Valid 注解在方法参数上启用验证
	 * 11. 验证错误信息可以通过 message 属性自定义，支持国际化
	 * 12. 在 JPA 实体类中使用 @NotBlank 可以确保数据完整性，防止无效数据进入数据库
	 * 13. 这种验证是声明式的，代码简洁，易于维护
	 * 14. Spring Boot 会自动处理验证错误，并返回适当的错误响应
	 */
	@Column(name = "last_name")
	@NotBlank
	private String lastName;

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
