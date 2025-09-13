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

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Visit 实体类 - 宠物访问记录
 * 
 * 1. 类的作用和职责：
 *    - 表示宠物在诊所的访问记录（就诊、体检、疫苗接种等）
 *    - 继承自 BaseEntity，获得ID管理功能
 *    - 记录访问的日期和详细描述
 *    - 与 Pet 实体建立多对一关系
 * 
 * 2. 继承关系：
 *    - extends BaseEntity：继承基础实体的ID管理功能
 * 
 * 3. JPA 注解说明：
 *    - @Entity：标记为JPA实体类，对应数据库表
 *    - @Table(name = "visits")：指定对应的数据库表名为"visits"
 *    - @Column：指定字段对应的数据库列名
 * 
 * 4. 验证注解说明：
 *    - @NotBlank：描述字段不能为空或只包含空白字符
 * 
 * 5. 格式化注解：
 *    - @DateTimeFormat：指定日期格式，用于前端显示和表单绑定
 * 
 * 6. 关系映射：
 *    - 与 Pet 类：多对一关系（多个访问记录属于一个宠物）
 *    - 通过 Pet 间接与 Owner 关联
 * 
 * 7. 业务功能：
 *    - 访问记录管理：记录宠物的所有诊所访问
 *    - 医疗历史追踪：提供完整的医疗历史记录
 *    - 健康监控：跟踪宠物的健康状况变化
 *    - 预约管理：记录预约和实际访问情况
 * 
 * 8. 设计模式：
 *    - 值对象模式：简单的访问记录
 *    - 事件记录模式：记录宠物的重要事件
 *    - 审计模式：记录系统操作的历史
 * 
 * 9. 数据库表结构：
 *    - visits 表包含：id, visit_date, description, pet_id
 *    - 通过 pet_id 外键关联到 pets 表
 * 
 * 10. 访问类型示例：
 *     - 常规体检（Routine Checkup）
 *     - 疫苗接种（Vaccination）
 *     - 疾病治疗（Illness Treatment）
 *     - 手术（Surgery）
 *     - 紧急治疗（Emergency Treatment）
 *     - 美容护理（Grooming）
 * 
 * @author Ken Krebs
 * @author Dave Syer
 */
@Entity
@Table(name = "visits")
public class Visit extends BaseEntity {

	/**
	 * 访问日期
	 * 
	 * 字段说明：
	 * - 类型：LocalDate（Java 8 时间API）
	 * - 数据库列：visit_date
	 * - 格式化：@DateTimeFormat(pattern = "yyyy-MM-dd")
	 * - 业务含义：宠物访问诊所的日期
	 * - 用途：记录访问时间、排序访问历史、统计访问频率
	 * 
	 * 设计考虑：
	 * - 使用 LocalDate 而不是 Date，更现代且线程安全
	 * - 格式化为 yyyy-MM-dd 便于前端显示和表单处理
	 * - 默认为当前日期，表示访问记录创建的时间
	 */
	@Column(name = "visit_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	/**
	 * 访问描述
	 * 
	 * 字段说明：
	 * - 类型：String
	 * - 验证：@NotBlank - 不能为空或只包含空白字符
	 * - 业务含义：访问的详细描述信息
	 * - 用途：记录诊断结果、治疗方案、注意事项等
	 * 
	 * 内容示例：
	 * - "常规体检，健康状况良好"
	 * - "接种狂犬病疫苗"
	 * - "治疗感冒，开药3天"
	 * - "绝育手术，恢复良好"
	 */
	@NotBlank
	private String description;

	/**
	 * 构造函数 - 创建新的访问记录
	 * 
	 * 业务逻辑：
	 * - 自动设置访问日期为当前日期
	 * - 表示访问记录创建的时间
	 * - 描述字段需要后续设置
	 * 
	 * 使用场景：
	 * - 创建新的访问记录时调用
	 * - 确保访问日期不为空
	 * - 简化访问记录的创建过程
	 */
	public Visit() {
		this.date = LocalDate.now();
	}

	/**
	 * 获取访问日期
	 * 
	 * @return 访问日期，默认为当前日期
	 */
	public LocalDate getDate() {
		return this.date;
	}

	/**
	 * 设置访问日期
	 * 
	 * @param date 访问日期，可以为 null
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * 获取访问描述
	 * 
	 * @return 访问的详细描述信息
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * 设置访问描述
	 * 
	 * @param description 访问的详细描述信息，不能为空
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
