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

import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * PetType 实体类 - 宠物类型分类
 * 
 * 1. 类的作用和职责：
 *    - 表示宠物的类型分类（如：猫、狗、仓鼠、鸟等）
 *    - 继承自 NamedEntity，获得名称和ID管理功能
 *    - 作为 Pet 实体的分类标准
 *    - 支持动态添加新的宠物类型
 * 
 * 2. 继承关系：
 *    - extends NamedEntity：继承命名实体的基本功能（name, id）
 *    - NamedEntity extends BaseEntity：继承基础实体的ID管理
 * 
 * 3. JPA 注解说明：
 *    - @Entity：标记为JPA实体类，对应数据库表
 *    - @Table(name = "types")：指定对应的数据库表名为"types"
 * 
 * 4. 关系映射：
 *    - 与 Pet 类：一对多关系（一个类型对应多个宠物）
 *    - 作为 Pet 的 @ManyToOne 关系的目标实体
 * 
 * 5. 业务功能：
 *    - 宠物分类管理：提供标准化的宠物类型
 *    - 统计报表：按类型统计宠物数量
 *    - 医疗建议：不同类型宠物有不同的医疗需求
 *    - 界面显示：在下拉列表中选择宠物类型
 * 
 * 6. 设计模式：
 *    - 值对象模式：简单的分类标识
 *    - 枚举替代模式：使用数据库存储而不是枚举
 *    - 分类模式：为其他实体提供分类标准
 * 
 * 7. 数据库表结构：
 *    - types 表包含：id, name
 *    - pets 表通过 type_id 外键关联到 types 表
 * 
 * 8. 常见宠物类型示例：
 *    - Cat（猫）
 *    - Dog（狗）
 *    - Hamster（仓鼠）
 *    - Bird（鸟）
 *    - Fish（鱼）
 *    - Rabbit（兔子）
 *    - Snake（蛇）
 *    - Lizard（蜥蜴）
 * 
 * @author Juergen Hoeller Can be Cat, Dog, Hamster...
 */
@Entity
@Table(name = "types")
public class PetType extends NamedEntity {

}
