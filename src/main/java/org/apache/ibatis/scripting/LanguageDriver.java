/*
 * Copyright 2009-2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.scripting;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

/**
 * 脚本语言驱动
 *
 * @author Frank D. Martinez [mnesarco]
 */
public interface LanguageDriver {

    /**
     * Creates a {@link ParameterHandler} that passes the actual parameters to the the JDBC statement.
     *
     * @param mappedStatement The mapped statement that is being executed
     * @param parameterObject The input parameter object (can be null)
     * @param boundSql        The resulting SQL once the dynamic language has been executed.
     * @return the parameter handler
     * @see DefaultParameterHandler
     */
    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);

    /**
     * mybatis框架启动时, 在构建{@link MappedStatement}时调用, 这个方法用于从
     * 各个 xxxMapper.xml 文件解析出各个标签
     * <pre>
     *     <select>、<update>、<insert>、<delete>
     * </pre>
     * 为其生成一个{@link SqlSource}.
     *
     * @param configuration 全局配置类
     * @param script        从xml文件解析到XNode节点, 其实就是各个：<select>、<update>、<insert>、<delete>标签
     * @param parameterType 参数类型, 可能为null
     * @return the sql source
     */
    SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType);

    /**
     * mybatis框架启动时, 在构建{@link MappedStatement}时调用, 这个方法用于从
     * 各个注解中：{@link Select}、{@link Insert}、{@link Update}、{@link Delete} 获取sql语句,
     * 为其生成一个{@link SqlSource}.
     *
     * @param configuration 全局配置类
     * @param script        注解中的sql语句, 即{@link Select}、{@link Insert}...
     * @param parameterType 参数类型, 可能为null
     * @return the sql source
     */
    SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType);

}
