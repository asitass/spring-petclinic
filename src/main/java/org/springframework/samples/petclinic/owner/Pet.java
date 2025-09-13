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
package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

/**
 * Pet 实体类 - 宠物诊所的宠物
 * 
 * 1. 类的作用和职责：
 *    - 表示宠物诊所系统中的宠物对象
 *    - 继承自 NamedEntity，获得名称和ID管理功能
 *    - 管理宠物的基本信息：名称、出生日期、类型
 *    - 维护宠物的访问记录历史
 *    - 与主人（Owner）建立多对一关系
 * 
 * 2. 继承关系：
 *    - extends NamedEntity：继承命名实体的基本功能（name, id）
 *    - NamedEntity extends BaseEntity：继承基础实体的ID管理
 * 
 * 3. JPA 注解说明：
 *    - @Entity：标记为JPA实体类，对应数据库表
 *    - @Table(name = "pets")：指定对应的数据库表名为"pets"
 *    - @Column：指定字段对应的数据库列名
 *    - @ManyToOne：多对一关系，多个宠物属于一个主人
 *    - @OneToMany：一对多关系，一个宠物可以有多个访问记录
 *    - @JoinColumn：指定外键列名
 *    - @OrderBy：指定查询时的排序规则
 * 
 * 4. 格式化注解：
 *    - @DateTimeFormat：指定日期格式，用于前端显示和表单绑定
 * 
 * 5. 关系映射：
 *    - 与 Owner 类：多对一关系（多个宠物属于一个主人）
 *    - 与 PetType 类：多对一关系（多个宠物属于同一类型）
 *    - 与 Visit 类：一对多关系（一个宠物多个访问记录）
 * 
 * 6. 业务功能：
 *    - 宠物信息管理：名称、出生日期、类型
 *    - 访问记录管理：添加、获取访问历史
 *    - 年龄计算：基于出生日期计算宠物年龄
 *    - 类型分类：通过 PetType 进行分类管理
 * 
 * 7. 设计模式：
 *    - 领域模型模式：封装宠物相关的业务逻辑
 *    - 聚合模式：Pet 是访问记录的聚合根
 *    - 值对象模式：使用 LocalDate 表示出生日期
 * 
 * 8. 数据库表结构：
 *    - pets 表包含：id, name, birth_date, type_id, owner_id
 *    - visits 表包含：id, visit_date, description, pet_id
 *    - types 表包含：id, name（宠物类型）
 * 
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Wick Dynex
 */
@Entity
@Table(name = "pets")
public class Pet extends NamedEntity {

	/**
	 * 宠物的出生日期
	 * 
	 * 字段说明：
	 * - 类型：LocalDate（Java 8 时间API）
	 * - 数据库列：birth_date
	 * - 格式化：@DateTimeFormat(pattern = "yyyy-MM-dd")
	 * - 业务含义：宠物的出生日期，用于计算年龄
	 * - 用途：年龄计算、健康评估、疫苗接种计划
	 * 
	 * 设计考虑：
	 * - 使用 LocalDate 而不是 Date，更现代且线程安全
	 * - 格式化为 yyyy-MM-dd 便于前端显示和表单处理
	 * - 可以为 null，表示出生日期未知
	 */
	@Column(name = "birth_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	/**
	 * 宠物的类型
	 * 
	 * 字段说明：
	 * - 类型：PetType（宠物类型实体）
	 * - 关系：@ManyToOne - 多个宠物属于同一类型
	 * - 外键：type_id - 宠物表中的外键列
	 * - 业务含义：宠物的分类（如：猫、狗、仓鼠等）
	 * - 用途：分类管理、统计报表、医疗建议
	 * 
	 * 设计考虑：
	 * - 使用实体关系而不是枚举，便于扩展
	 * - 支持动态添加新的宠物类型
	 * - 可以为 null，表示类型未确定
	 */
	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType type;

	/**
	 * 宠物的访问记录列表
	 * 
	 * 字段说明：
	 * - 类型：Set<Visit>（使用 LinkedHashSet 保持插入顺序）
	 * - 关系：@OneToMany - 一个宠物可以有多个访问记录
	 * - 级联：CascadeType.ALL - 对访问记录的所有操作都会级联
	 * - 加载：FetchType.EAGER - 立即加载所有访问记录
	 * - 外键：pet_id - 访问记录表中的外键列
	 * - 排序：@OrderBy("date ASC") - 按访问日期升序排列
	 * 
	 * 设计考虑：
	 * - 使用 Set 避免重复的访问记录
	 * - LinkedHashSet 保持插入顺序，便于查看历史
	 * - EAGER 加载适合访问记录不多的场景
	 * - 级联操作简化了访问记录的生命周期管理
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "pet_id")
	@OrderBy("date ASC")
	private final Set<Visit> visits = new LinkedHashSet<>();

	/**
	 * 设置宠物的出生日期
	 * 
	 * @param birthDate 宠物的出生日期，可以为 null
	 */
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * 获取宠物的出生日期
	 * 
	 * @return 宠物的出生日期，可能为 null
	 */
	public LocalDate getBirthDate() {
		return this.birthDate;
	}

	/**
	 * 获取宠物的类型
	 * 
	 * @return 宠物的类型对象，可能为 null
	 */
	public PetType getType() {
		return this.type;
	}

	/**
	 * 设置宠物的类型
	 * 
	 * @param type 宠物的类型对象，可以为 null
	 */
	public void setType(PetType type) {
		this.type = type;
	}

	/**
	 * 获取宠物的所有访问记录
	 * 
	 * 返回说明：
	 * - 返回按访问日期升序排列的访问记录集合
	 * - 集合是实时的，修改会影响数据库
	 * - 使用 EAGER 加载，所有访问记录都已加载
	 * - 返回 Collection 接口，隐藏具体实现细节
	 * 
	 * @return 访问记录集合，按日期升序排列
	 */
	public Collection<Visit> getVisits() {
		return this.visits;
	}

	/**
	 * 为宠物添加访问记录
	 * 
	 * 业务逻辑：
	 * - 将新的访问记录添加到宠物的访问历史中
	 * - 使用 Set 确保不会添加重复的访问记录
	 * - 访问记录会自动按日期排序
	 * 
	 * 使用场景：
	 * - 宠物就诊后添加访问记录
	 * - 定期体检记录
	 * - 疫苗接种记录
	 * - 健康检查记录
	 * 
	 * @param visit 要添加的访问记录对象
	 */
	public void addVisit(Visit visit) {
		getVisits().add(visit);
	}

}
