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
package org.apache.ibatis.type;

import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.session.Configuration;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link TypeHandler}的抽象父类, 基本上每一种JDBC类型, 都会通过它的实现类来完成java类型的映射.
 * 这里实际上是对JDBC{@link PreparedStatement}的一种封装, 原生的JDBC编码需要我们自己调用
 * {@link PreparedStatement #setXXX(index)}去替换掉占位符的<b>?</b>号.
 */
public abstract class BaseTypeHandler<T> extends TypeReference<T> implements TypeHandler<T> {

    /**
     * @deprecated Since 3.5.0 - See https://github.com/mybatis/mybatis-3/issues/1203.
     * This field will remove future.
     */
    @Deprecated
    protected Configuration configuration;

    /**
     * Sets the configuration.
     *
     * @param c the new configuration
     * @deprecated Since 3.5.0 - See https://github.com/mybatis/mybatis-3/issues/1203.
     * This property will remove future.
     */
    @Deprecated
    public void setConfiguration(Configuration c) {
        this.configuration = c;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            if (jdbcType == null) {
                throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
            }
            try {
                ps.setNull(i, jdbcType.TYPE_CODE);
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . "
                        + "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. "
                        + "Cause: " + e, e);
            }
        } else {
            try {
                setNonNullParameter(ps, i, parameter, jdbcType);
            } catch (Exception e) {
                throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " . "
                        + "Try setting a different JdbcType for this parameter or a different configuration property. "
                        + "Cause: " + e, e);
            }
        }
    }

    @Override
    public T getResult(ResultSet rs, String columnName) throws SQLException {
        try {
            return getNullableResult(rs, columnName);
        } catch (Exception e) {
            throw new ResultMapException("Error attempting to get column '" + columnName + "' from result set.  Cause: " + e, e);
        }
    }

    @Override
    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            return getNullableResult(rs, columnIndex);
        } catch (Exception e) {
            throw new ResultMapException("Error attempting to get column #" + columnIndex + " from result set.  Cause: " + e, e);
        }
    }

    @Override
    public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            return getNullableResult(cs, columnIndex);
        } catch (Exception e) {
            throw new ResultMapException("Error attempting to get column #" + columnIndex + " from callable statement.  Cause: " + e, e);
        }
    }

    /**
     * 设置非空的sql参数
     *
     * @param ps        预编译sql
     * @param i         索引下标, jdbc下标从1开始
     * @param parameter 实际参数值
     * @param jdbcType  参数对应jdbc类型
     * @throws SQLException 异常
     */
    public abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

    /**
     * 通过数据库返回的字段名获取值
     *
     * @param rs         jdbc结果集, 本质是一个二维数组
     * @param columnName 字段名
     * @return 字段名对应的值
     * @throws SQLException 异常
     */
    public abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException;

    /**
     * 数据库返回的字段, 通过下标获取值
     *
     * @param rs          jdbc结果集, 本质是一个二维数组
     * @param columnIndex 字段的下标
     * @return 字段名对应的值
     * @throws SQLException 异常
     */
    public abstract T getNullableResult(ResultSet rs, int columnIndex) throws SQLException;

    public abstract T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException;

}
