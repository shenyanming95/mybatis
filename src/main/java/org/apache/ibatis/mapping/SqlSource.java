/**
 * Copyright 2009-2019 the original author or authors.
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
package org.apache.ibatis.mapping;

/**
 * 从xml文件或者注解读取到的sql语句, 然后根据从用户收到的输入参数解析,
 * 使其变成能传递到数据库的SQL.
 */
public interface SqlSource {

    /**
     * 将原始的sql(比方说写在xml或者注解中的sql), 转换成真正能在数据库执行的SQL
     * @param parameterObject 参数, 用来设置
     * @return 实际SQL
     */
    BoundSql getBoundSql(Object parameterObject);

}
