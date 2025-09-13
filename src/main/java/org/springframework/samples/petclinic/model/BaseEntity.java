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

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Simple JavaBean domain object with an id property. Used as a base class for objects
 * needing this property.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
/**
 * 为什么 BaseEntity 需要实现 Serializable 接口？
 * 
 * 1. 序列化是什么？
 *    - 序列化：将Java对象转换为字节流的过程
 *    - 反序列化：将字节流转换回Java对象的过程
 *    - 本质：对象状态的持久化存储和传输机制
 * 
 * 2. 序列化有什么用？
 *    - 网络传输：对象可以在不同JVM之间传输
 *    - 持久化存储：对象可以保存到文件、数据库
 *    - 缓存机制：对象可以存储在内存缓存中
 *    - 分布式系统：支持集群和微服务架构
 *    - 会话管理：Web应用中的Session存储
 * 
 * 3. 为什么BaseEntity需要实现Serializable？
 *    - 作为基类：确保所有继承的实体类都支持序列化
 *    - Spring框架要求：Session、Cache、Security等功能需要序列化
 *    - JPA/Hibernate要求：某些操作需要序列化实体对象
 *    - 分布式部署：支持集群环境下的对象共享
 *    - 缓存系统：Redis、Memcached等需要序列化对象
 * 
 * 4. 实际应用场景：
 *    - Web应用：用户Session中的对象需要序列化
 *    - 缓存：将查询结果缓存到Redis中
 *    - 消息队列：对象在消息队列中传输
 *    - 分布式Session：多服务器共享用户状态
 *    - 数据备份：对象状态的备份和恢复
 * 
 * 5. 不实现Serializable的后果：
 *    - 运行时异常：NotSerializableException
 *    - 功能受限：无法使用Spring的某些功能
 *    - 部署问题：无法进行分布式部署
 *    - 缓存失效：无法使用对象缓存
 * 
 * 6. 序列化的工作原理：
 *    - 对象状态：保存对象的字段值
 *    - 类信息：保存类的元数据信息
 *    - 字节流：转换为可传输的字节数组
 *    - 版本控制：通过serialVersionUID管理版本兼容性
 * 
 * 7. implement 和 extend 的区别：
 *    - implement（实现）：
 *      * 用于实现接口（interface）
 *      * 一个类可以实现多个接口
 *      * 必须实现接口中定义的所有方法
 *      * 表示"具有某种能力"或"遵循某种契约"
 *      * 例如：implements Serializable, Comparable
 *      * 关系：类与接口的关系，是"实现"关系
 *      * 语法：class A implements Interface1, Interface2
 *      * 目的：获得接口定义的功能，实现多态
 * 
 *    - extend（继承）：
 *      * 用于继承类（class）
 *      * 一个类只能继承一个父类（单继承）
 *      * 自动获得父类的所有非私有成员
 *      * 表示"是一种"关系
 *      * 例如：extends BaseEntity, extends Person
 *      * 关系：类与类的关系，是"继承"关系
 *      * 语法：class A extends ParentClass
 *      * 目的：代码复用，建立类层次结构
 * 
 * 8. 为什么这里用 implement 而不是 extend？
 *    - Serializable 是接口，不是类
 *    - 接口定义契约：要求类具有序列化能力
 *    - 实现接口：表示该类支持序列化操作
 *    - 多接口支持：可以同时实现多个接口
 *    - 契约遵循：必须提供序列化相关的方法实现
 * 
 * 9. Comparable 接口是什么？
 *    - Comparable 是Java标准库中的接口，位于 java.lang 包
 *    - 作用：定义对象的自然排序规则
 *    - 核心方法：compareTo(T o) 方法
 *    - 返回值：int类型，表示比较结果
 *      * 负数：当前对象小于参数对象
 *      * 0：当前对象等于参数对象
 *      * 正数：当前对象大于参数对象
 * 
 * 10. Comparable 的应用场景：
 *     - 排序：Arrays.sort(), Collections.sort() 自动使用
 *     - 集合：TreeSet, TreeMap 等有序集合
 *     - 搜索：二分查找等算法
 *     - 比较：对象之间的大小比较
 *     - 优先级队列：PriorityQueue 中的元素排序
 * 
 * 11. Comparable 示例：
 *     class Person implements Comparable<Person> {
 *         private String name;
 *         private int age;
 *         
 *         public int compareTo(Person other) {
 *             // 按年龄排序
 *             return Integer.compare(this.age, other.age);
 *             // 或者按姓名排序
 *             // return this.name.compareTo(other.name);
 *         }
 *     }
 * 
 * 12. Comparable vs Comparator：
 *     - Comparable：对象内部定义排序规则（自然排序）
 *     - Comparator：外部定义排序规则（比较器）
 *     - Comparable：实现简单，但排序规则固定
 *     - Comparator：灵活，可以为同一类定义多种排序规则
 * 
 * 13. 为什么BaseEntity不实现Comparable？
 *     - BaseEntity只有id字段，没有业务意义的排序规则
 *     - 不同的实体类可能有不同的排序需求
 *     - 排序规则应该在具体的业务实体类中定义
 *     - 保持基类的简洁性，避免不必要的约束
 *     - 子类可以根据需要选择是否实现Comparable
 * 
 * 14. Bean 是什么？
 *    - Bean 是 Spring 框架中的核心概念，表示由 Spring 容器管理的对象
 *    - 本质：普通的 Java 对象（POJO - Plain Old Java Object）
 *    - 特点：由 Spring 容器负责创建、配置、管理和销毁
 *    - 生命周期：从创建到销毁的整个过程都由 Spring 管理
 * 
 * 15. Bean 的特征：
 *    - 无参构造函数：Spring 通过反射创建对象
 *    - 属性注入：通过 setter 方法或字段注入依赖
 *    - 生命周期回调：支持 @PostConstruct、@PreDestroy 等注解
 *    - 作用域管理：单例、原型、请求、会话等不同作用域
 *    - 依赖注入：自动解决对象之间的依赖关系
 * 
 * 16. Bean 的类型：
 *    - 普通 Bean：业务逻辑对象，如 Service、Repository
 *    - 配置 Bean：配置类中定义的对象，如 @Bean 方法
 *    - 组件 Bean：通过注解标记的类，如 @Component、@Service
 *    - 实体 Bean：JPA 实体类，如 @Entity 标记的类
 *    - 控制器 Bean：Web 控制器，如 @Controller、@RestController
 * 
 * 17. Bean 的创建方式：
 *    - 注解方式：@Component、@Service、@Repository、@Controller
 *    - 配置方式：@Configuration 类中的 @Bean 方法
 *    - XML 配置：传统的 XML 配置文件方式
 *    - 自动扫描：@ComponentScan 自动发现和注册 Bean
 * 
 * 18. Bean 的作用域：
 *    - Singleton（单例）：整个应用中只有一个实例，默认作用域
 *    - Prototype（原型）：每次请求都创建新实例
 *    - Request（请求）：每个 HTTP 请求创建一个实例
 *    - Session（会话）：每个 HTTP 会话创建一个实例
 *    - Application（应用）：整个 Web 应用创建一个实例
 * 
 * 19. Bean 的生命周期：
 *    - 实例化：Spring 容器创建对象实例
 *    - 属性注入：设置对象的属性和依赖
 *    - 初始化：调用初始化方法（@PostConstruct）
 *    - 使用：Bean 可以被其他对象使用
 *    - 销毁：容器关闭时调用销毁方法（@PreDestroy）
 * 
 * 20. 为什么 BaseEntity 不是 Bean？
 *    - BaseEntity 是实体类基类，不是业务组件
 *    - 实体类由 JPA/Hibernate 管理，不是 Spring 容器管理
 *    - 实体类通过 new 关键字创建，不是依赖注入
 *    - 实体类专注于数据模型，不包含业务逻辑
 *    - 实体类的生命周期由持久化框架管理
 * 
 * 21. 实体类 vs Bean 的区别：
 *    - 实体类：数据模型，表示数据库表结构
 *    - Bean：业务组件，包含业务逻辑
 *    - 实体类：由 JPA 管理，专注于数据持久化
 *    - Bean：由 Spring 管理，专注于业务功能
 *    - 实体类：通过 ORM 框架操作数据库
 *    - Bean：通过依赖注入提供业务服务
 * 
 * 22. ORM 框架是什么？
 *    - ORM：Object-Relational Mapping（对象关系映射）
 *    - 作用：在面向对象编程语言和关系型数据库之间建立映射关系
 *    - 核心思想：将数据库表映射为Java类，将表记录映射为对象实例
 *    - 主要实现包括 Hibernate、EclipseLink、OpenJPA 等
 *    - Spring Boot 默认集成 Hibernate 作为 ORM 框架
 * 
 * 23. 为什么可以用 @Entity：
 *    - JPA 框架通过反射机制读取注解信息，在运行时建立对象与数据库的映射关系
 *    - @Entity 注解是 JPA 规范的一部分，被所有 JPA 实现（如 Hibernate）支持
 *    - 注解在编译时被保留，运行时通过反射可以获取注解信息
 *    - Spring Boot 自动配置了 JPA，所以这些注解会被自动识别和处理
 *    - JPA 会根据这些注解自动处理对象的持久化、查询、更新和删除操作
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
 * 24. @MappedSuperclass 注解详解：
 *     - 作用：标记一个类为映射超类，该类不会被映射为数据库表
 *     - 特点：子类会继承父类的字段和注解，但父类本身不生成表
 *     - 用途：定义公共字段和映射信息，供多个实体类共享
 *     - 与 @Entity 的区别：@Entity 会生成表，@MappedSuperclass 不会
 *     - 继承关系：子类必须使用 @Entity 注解才能成为实体类
 * 
 * 25. Serializable 接口详解：
 *     - 位置：java.io.Serializable 接口
 *     - 特点：标记接口（Marker Interface），没有方法需要实现
 *     - 作用：告诉 JVM 这个类的对象可以被序列化
 *     - 序列化：将对象转换为字节流，用于网络传输或持久化存储
 *     - 反序列化：将字节流转换回对象实例
 * 
 * 26. @Id 注解详解：
 *     - 作用：标记主键字段，表示这是实体的唯一标识符
 *     - 位置：javax.persistence.Id（JPA 规范）
 *     - 特点：每个实体类必须有且仅有一个主键
 *     - 类型：可以是基本类型、包装类型或自定义类型
 *     - 数据库：对应数据库表的主键列
 * 
 * 27. @GeneratedValue 注解详解：
 *     - 作用：指定主键的生成策略
 *     - 参数：strategy 属性定义生成方式
 *     - GenerationType.IDENTITY：数据库自动递增（MySQL AUTO_INCREMENT）
 *     - GenerationType.SEQUENCE：使用数据库序列（Oracle）
 *     - GenerationType.TABLE：使用表生成主键
 *     - GenerationType.AUTO：让 JPA 自动选择策略
 * 
 * 28. GenerationType.IDENTITY 详解：
 *     - 数据库支持：MySQL、SQL Server、PostgreSQL 等
 *     - 工作原理：数据库在插入记录时自动分配递增的主键值
 *     - 优点：简单高效，数据库原生支持
 *     - 缺点：批量插入时性能可能不如序列
 *     - 使用场景：单表主键，不需要跨表唯一性
 * 
 * 29. Integer 类型选择：
 *     - 为什么用 Integer 而不是 int？
 *     - Integer 是包装类型，可以表示 null 值
 *     - 新创建的实体对象 id 为 null，表示还未持久化
 *     - int 是基本类型，不能为 null，无法表示"未设置"状态
 *     - JPA 需要区分"新对象"和"已持久化对象"
 * 
 * 30. isNew() 方法详解：
 *     - 作用：判断实体对象是否为新建对象（未持久化）
 *     - 逻辑：id 为 null 表示新对象，id 不为 null 表示已持久化
 *     - 用途：在业务逻辑中区分新增和更新操作
 *     - 示例：if (entity.isNew()) { save(entity); } else { update(entity); }
 *     - 设计模式：这是 Domain-Driven Design 中的常见模式
 * 
 * 31. 实体类设计原则：
 *     - 单一职责：每个实体类只负责一个业务概念
 *     - 开闭原则：对扩展开放，对修改封闭
 *     - 里氏替换：子类可以替换父类使用
 *     - 接口隔离：接口设计要精简，不要冗余
 *     - 依赖倒置：依赖抽象而不是具体实现
 * 
 * 32. 实体类命名规范：
 *     - 类名：使用名词，首字母大写，如 User、Order、Product
 *     - 字段名：使用驼峰命名，如 firstName、lastName、emailAddress
 *     - 方法名：使用动词，如 getName()、setName()、isActive()
 *     - 常量：全大写，用下划线分隔，如 MAX_SIZE、DEFAULT_VALUE
 * 
 * 33. 实体类最佳实践：
 *     - 提供无参构造函数：JPA 需要反射创建对象
 *     - 实现 equals() 和 hashCode()：基于业务主键比较
 *     - 使用包装类型：支持 null 值，便于判断对象状态
 *     - 避免循环引用：防止序列化时的无限递归
 *     - 合理使用懒加载：避免 N+1 查询问题
 * 
 * 34. 数据库设计考虑：
 *     - 主键设计：使用自增整数，简单高效
 *     - 索引优化：为常用查询字段建立索引
 *     - 外键约束：保证数据完整性
 *     - 字段长度：根据业务需求设置合适的长度
 *     - 数据类型：选择最合适的数据类型，节省存储空间
 * 
 * 35. Spring Boot 集成 JPA：
 *     - 自动配置：Spring Boot 自动配置 JPA 相关组件
 *     - 数据源配置：通过 application.properties 配置数据库连接
 *     - 实体扫描：自动扫描 @Entity 注解的类
 *     - 事务管理：提供声明式事务支持
 *     - 测试支持：提供 @DataJpaTest 等测试注解
 * 
 * 36. 性能优化建议：
 *     - 懒加载：使用 @OneToMany(fetch = FetchType.LAZY)
 *     - 批量操作：使用批量插入和更新
 *     - 查询优化：使用 @Query 编写高效查询
 *     - 缓存策略：合理使用二级缓存
 *     - 连接池：配置合适的数据库连接池大小
 * 
 * 37. 常见问题和解决方案：
 *     - LazyInitializationException：在事务范围内访问懒加载属性
 *     - 循环引用：使用 @JsonIgnore 或 DTO 模式
 *     - 性能问题：使用分页查询，避免一次性加载大量数据
 *     - 并发问题：使用乐观锁或悲观锁
 *     - 数据一致性问题：合理使用事务边界
 * 
 * 38. 测试策略：
 *     - 单元测试：测试实体类的业务逻辑
 *     - 集成测试：测试 JPA 映射和数据库操作
 *     - 性能测试：测试查询性能和并发处理能力
 *     - 数据测试：验证数据的完整性和一致性
 * 
 * 39. 版本控制和兼容性：
 *     - serialVersionUID：用于序列化版本控制
 *     - 数据库迁移：使用 Flyway 或 Liquibase 管理数据库版本
 *     - API 版本：使用版本号管理 API 兼容性
 *     - 向后兼容：保持对旧版本数据的兼容性
 * 
 * 40. 总结：
 *     - BaseEntity 是所有实体类的基类，提供公共的 id 字段
 *     - 实现 Serializable 接口，支持序列化和分布式部署
 *     - 使用 @MappedSuperclass 注解，不生成数据库表
 *     - 使用 @Id 和 @GeneratedValue 定义主键生成策略
 *     - 提供 isNew() 方法判断对象状态
 *     - 遵循面向对象设计原则和 JPA 最佳实践
 *     - 为整个 PetClinic 项目提供统一的数据模型基础
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

	/**
	 * 实体主键，使用数据库自动递增策略生成
	 * 
	 * 字段说明：
	 * - 类型：Integer（包装类型，支持 null 值）
	 * - 作用：唯一标识每个实体对象
	 * - 生成策略：IDENTITY（数据库自动递增）
	 * - 数据库映射：对应表的主键列
	 * - 业务含义：区分新对象（null）和已持久化对象（非 null）
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 获取实体主键
	 * 
	 * @return 主键值，如果为 null 表示新对象
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置实体主键
	 * 
	 * @param id 主键值，通常由数据库自动生成
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 判断实体对象是否为新建对象（未持久化）
	 * 
	 * 业务逻辑：
	 * - id 为 null：表示新创建的对象，还未保存到数据库
	 * - id 不为 null：表示已持久化的对象，存在于数据库中
	 * 
	 * 使用场景：
	 * - 在 Service 层判断是执行 insert 还是 update 操作
	 * - 在 Controller 层判断是新增还是编辑页面
	 * - 在业务逻辑中区分对象的生命周期状态
	 * 
	 * @return true 如果是新对象，false 如果是已持久化对象
	 */
	public boolean isNew() {
		return this.id == null;
	}

}
