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

import java.util.List;
import java.util.Optional;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * OwnerRepository - 宠物主人数据访问接口
 * 
 * 1. 接口作用和职责：
 *    - 提供 Owner 实体的数据访问操作
 *    - 继承 JpaRepository，获得基本的CRUD操作
 *    - 定义自定义查询方法
 *    - 支持分页查询功能
 * 
 * 2. Spring Data JPA 特性：
 *    - 继承 JpaRepository<Owner, Integer>：获得完整的CRUD操作
 *    - 方法命名约定：Spring Data 自动实现查询方法
 *    - 分页支持：返回 Page<Owner> 支持分页
 *    - 查询方法：findByLastNameStartingWith 按姓氏前缀查找
 * 
 * 3. 主要方法：
 *    - findByLastNameStartingWith：按姓氏前缀查找主人（支持分页）
 *    - findById：根据ID查找主人（返回Optional）
 *    - save：保存主人信息
 *    - deleteById：删除主人
 *    - findAll：查找所有主人
 * 
 * 4. 查询方法命名规则：
 *    - findBy + 属性名：按属性查找
 *    - StartingWith：前缀匹配
 *    - Pageable：分页参数
 *    - Optional：可能为空的结果
 * 
 * 5. 使用场景：
 *    - 主人注册时检查重复
 *    - 按姓氏搜索主人
 *    - 分页显示主人列表
 *    - 主人信息的增删改查
 * 
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Wick Dynex
 */
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

	/**
	 * Retrieve {@link Owner}s from the data store by last name, returning all owners
	 * whose last name <i>starts</i> with the given name.
	 * @param lastName Value to search for
	 * @return a Collection of matching {@link Owner}s (or an empty Collection if none
	 * found)
	 */
	Page<Owner> findByLastNameStartingWith(String lastName, Pageable pageable);

	/**
	 * Retrieve an {@link Owner} from the data store by id.
	 * <p>
	 * This method returns an {@link Optional} containing the {@link Owner} if found. If
	 * no {@link Owner} is found with the provided id, it will return an empty
	 * {@link Optional}.
	 * </p>
	 * @param id the id to search for
	 * @return an {@link Optional} containing the {@link Owner} if found, or an empty
	 * {@link Optional} if not found.
	 * @throws IllegalArgumentException if the id is null (assuming null is not a valid
	 * input for id)
	 */
	Optional<Owner> findById(@Nonnull Integer id);

}
