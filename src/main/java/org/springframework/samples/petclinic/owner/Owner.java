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

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.util.Assert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

/**
 * Owner 实体类 - 宠物诊所的宠物主人
 * 
 * 1. 类的作用和职责：
 *    - 表示宠物诊所系统中的宠物主人（客户）
 *    - 继承自 Person 类，获得基本的人员信息（姓名等）
 *    - 扩展了地址、城市、电话等联系信息
 *    - 管理该主人拥有的所有宠物
 *    - 提供宠物的查找、添加、访问记录管理等功能
 * 
 * 2. 继承关系：
 *    - extends Person：继承人员的基本信息（firstName, lastName）
 *    - Person extends NamedEntity：继承命名实体的基本功能
 *    - NamedEntity extends BaseEntity：继承基础实体的ID管理
 * 
 * 3. JPA 注解说明：
 *    - @Entity：标记为JPA实体类，对应数据库表
 *    - @Table(name = "owners")：指定对应的数据库表名为"owners"
 *    - @Column：指定字段对应的数据库列名
 *    - @OneToMany：一对多关系，一个主人可以有多个宠物
 *    - @JoinColumn：指定外键列名
 *    - @OrderBy：指定查询时的排序规则
 * 
 * 4. 验证注解说明：
 *    - @NotBlank：字段不能为空或只包含空白字符
 *    - @Pattern：使用正则表达式验证电话号码格式
 * 
 * 5. 关系映射：
 *    - 与 Pet 类：一对多关系（一个主人多个宠物）
 *    - 与 Visit 类：通过 Pet 间接关联（主人->宠物->访问记录）
 * 
 * 6. 业务功能：
 *    - 宠物管理：添加、查找、获取宠物
 *    - 访问记录管理：为特定宠物添加访问记录
 *    - 数据验证：确保联系信息的有效性
 *    - 状态管理：区分新对象和已持久化对象
 * 
 * 7. 设计模式：
 *    - 领域模型模式：封装业务逻辑和数据
 *    - 聚合根模式：Owner 是宠物的聚合根
 *    - 工厂方法模式：通过 addPet() 创建宠物关联
 * 
 * 8. 数据库表结构：
 *    - owners 表包含：id, first_name, last_name, address, city, telephone
 *    - pets 表包含：id, name, birth_date, type_id, owner_id
 *    - visits 表包含：id, visit_date, description, pet_id
 * 
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Oliver Drotbohm
 * @author Wick Dynex
 */
@Entity
@Table(name = "owners")
public class Owner extends Person {

	/**
	 * 主人地址
	 * 
	 * 字段说明：
	 * - 类型：String
	 * - 数据库列：address
	 * - 验证：@NotBlank - 不能为空或只包含空白字符
	 * - 业务含义：宠物主人的详细地址信息
	 * - 用途：用于联系和邮寄等业务场景
	 */
	@Column(name = "address")
	@NotBlank
	private String address;

	/**
	 * 主人所在城市
	 * 
	 * 字段说明：
	 * - 类型：String
	 * - 数据库列：city
	 * - 验证：@NotBlank - 不能为空或只包含空白字符
	 * - 业务含义：宠物主人居住的城市
	 * - 用途：用于地理区域分析和统计
	 */
	@Column(name = "city")
	@NotBlank
	private String city;

	/**
	 * 主人电话号码
	 * 
	 * 字段说明：
	 * - 类型：String
	 * - 数据库列：telephone
	 * - 验证：
	 *   * @NotBlank - 不能为空或只包含空白字符
	 *   * @Pattern(regexp = "\\d{10}") - 必须是10位数字
	 * - 业务含义：宠物主人的联系电话
	 * - 用途：紧急联系、预约提醒等
	 * - 格式要求：必须是10位数字（如：1234567890）
	 */
	@Column(name = "telephone")
	@NotBlank
	@Pattern(regexp = "\\d{10}", message = "{telephone.invalid}")
	private String telephone;

	/**
	 * 主人拥有的宠物列表
	 * 
	 * 字段说明：
	 * - 类型：List<Pet>（不可变列表）
	 * - 关系：@OneToMany - 一个主人可以有多个宠物
	 * - 级联：CascadeType.ALL - 对宠物的所有操作都会级联到主人
	 * - 加载：FetchType.EAGER - 立即加载所有宠物
	 * - 外键：owner_id - 宠物表中的外键列
	 * - 排序：@OrderBy("name") - 按宠物名称排序
	 * - 初始化：使用 ArrayList 确保列表可修改
	 * 
	 * 设计考虑：
	 * - 使用 final 确保引用不变，但列表内容可变
	 * - EAGER 加载适合宠物数量不多的场景
	 * - 级联操作简化了宠物的生命周期管理
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "owner_id")
	@OrderBy("name")
	private final List<Pet> pets = new ArrayList<>();

	/**
	 * 获取主人地址
	 * 
	 * @return 主人的地址信息
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * 设置主人地址
	 * 
	 * @param address 主人的地址信息
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取主人所在城市
	 * 
	 * @return 主人居住的城市
	 */
	public String getCity() {
		return this.city;
	}

	/**
	 * 设置主人所在城市
	 * 
	 * @param city 主人居住的城市
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 获取主人电话号码
	 * 
	 * @return 主人的电话号码
	 */
	public String getTelephone() {
		return this.telephone;
	}

	/**
	 * 设置主人电话号码
	 * 
	 * @param telephone 主人的电话号码（必须是10位数字）
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * 获取主人拥有的所有宠物列表
	 * 
	 * 返回说明：
	 * - 返回按名称排序的宠物列表
	 * - 列表是实时的，修改会影响数据库
	 * - 使用 EAGER 加载，所有宠物都已加载
	 * 
	 * @return 宠物列表，按名称排序
	 */
	public List<Pet> getPets() {
		return this.pets;
	}

	/**
	 * 为主人添加新宠物
	 * 
	 * 业务逻辑：
	 * - 只添加新创建的宠物（isNew() 返回 true）
	 * - 已存在的宠物不会被重复添加
	 * - 新宠物会自动关联到当前主人
	 * 
	 * 使用场景：
	 * - 主人注册新宠物时调用
	 * - 确保宠物与主人的关联关系正确
	 * 
	 * @param pet 要添加的宠物对象
	 */
	public void addPet(Pet pet) {
		if (pet.isNew()) {
			getPets().add(pet);
		}
	}

	/**
	 * 根据宠物名称查找宠物（忽略新宠物）
	 * 
	 * 业务逻辑：
	 * - 在主人的宠物列表中查找指定名称的宠物
	 * - 使用忽略大小写的比较
	 * - 忽略新创建的宠物（未保存到数据库的宠物）
	 * 
	 * 使用场景：
	 * - 检查宠物名称是否重复
	 * - 查找已存在的宠物进行编辑
	 * 
	 * @param name 要查找的宠物名称
	 * @return 找到的宠物对象，如果没找到返回 null
	 */
	public Pet getPet(String name) {
		return getPet(name, false);
	}

	/**
	 * 根据宠物ID查找宠物
	 * 
	 * 业务逻辑：
	 * - 遍历主人的所有宠物
	 * - 只查找已持久化的宠物（isNew() 返回 false）
	 * - 比较宠物的ID与指定ID
	 * 
	 * 使用场景：
	 * - 根据URL参数中的ID查找宠物
	 * - 编辑特定宠物的信息
	 * - 删除特定宠物
	 * 
	 * @param id 要查找的宠物ID
	 * @return 找到的宠物对象，如果没找到返回 null
	 */
	public Pet getPet(Integer id) {
		for (Pet pet : getPets()) {
			if (!pet.isNew()) {
				Integer compId = pet.getId();
				if (compId.equals(id)) {
					return pet;
				}
			}
		}
		return null;
	}

	/**
	 * 根据宠物名称查找宠物（可控制是否忽略新宠物）
	 * 
	 * 业务逻辑：
	 * - 在主人的宠物列表中查找指定名称的宠物
	 * - 使用忽略大小写的字符串比较
	 * - 根据 ignoreNew 参数决定是否考虑新宠物
	 * 
	 * 参数说明：
	 * - ignoreNew = true：只查找已保存的宠物
	 * - ignoreNew = false：查找所有宠物（包括新创建的）
	 * 
	 * 使用场景：
	 * - 验证宠物名称唯一性
	 * - 编辑宠物时查找现有宠物
	 * - 添加新宠物时检查重名
	 * 
	 * @param name 要查找的宠物名称
	 * @param ignoreNew 是否忽略新宠物（未保存的宠物）
	 * @return 找到的宠物对象，如果没找到返回 null
	 */
	public Pet getPet(String name, boolean ignoreNew) {
		for (Pet pet : getPets()) {
			String compName = pet.getName();
			if (compName != null && compName.equalsIgnoreCase(name)) {
				if (!ignoreNew || !pet.isNew()) {
					return pet;
				}
			}
		}
		return null;
	}

	/**
	 * 生成对象的字符串表示
	 * 
	 * 使用 Spring 的 ToStringCreator 工具类生成格式化的字符串
	 * 包含对象的所有重要属性，便于调试和日志记录
	 * 
	 * 输出格式：
	 * Owner[id=1, new=false, lastName=Smith, firstName=John, address=123 Main St, city=Springfield, telephone=1234567890]
	 * 
	 * @return 格式化的字符串表示
	 */
	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId())
			.append("new", this.isNew())
			.append("lastName", this.getLastName())
			.append("firstName", this.getFirstName())
			.append("address", this.address)
			.append("city", this.city)
			.append("telephone", this.telephone)
			.toString();
	}

	/**
	 * 为指定宠物添加访问记录
	 * 
	 * 业务逻辑：
	 * - 根据宠物ID查找对应的宠物
	 * - 将访问记录添加到宠物的访问历史中
	 * - 执行参数验证确保数据完整性
	 * 
	 * 参数验证：
	 * - petId 不能为 null
	 * - visit 不能为 null
	 * - 指定的宠物必须存在
	 * 
	 * 使用场景：
	 * - 宠物就诊后添加访问记录
	 * - 定期体检记录
	 * - 疫苗接种记录
	 * 
	 * @param petId 宠物的ID，不能为 null
	 * @param visit 要添加的访问记录，不能为 null
	 * @throws IllegalArgumentException 如果参数为 null 或宠物不存在
	 */
	public void addVisit(Integer petId, Visit visit) {

		Assert.notNull(petId, "Pet identifier must not be null!");
		Assert.notNull(visit, "Visit must not be null!");

		Pet pet = getPet(petId);

		Assert.notNull(pet, "Invalid Pet identifier!");

		pet.addVisit(visit);
	}

}
