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
package org.apache.ibatis.plugin;

import java.util.Properties;

/**
 * 拦截器
 *
 * @author Clinton Begin
 */
public interface Interceptor {

    /**
     * 拦截方法
     *
     * @param invocation 调用信息
     * @return 调用结果
     * @throws Throwable 异常
     */
    Object intercept(Invocation invocation) throws Throwable;

    /**
     * 调用{@link Plugin#wrap(Object, Interceptor)}为目标对象创建代理对象
     */
    default Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 设置拦截器属性
     *
     * @param properties 属性配置
     */
    default void setProperties(Properties properties) {
        // NOP
    }

}
