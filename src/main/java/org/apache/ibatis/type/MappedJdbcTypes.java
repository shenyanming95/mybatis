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

import java.lang.annotation.*;

/**
 * 用来指定多个{@link JdbcType}映射的{@link TypeHandler}.
 * <pre>
 *     @ MappedJdbcTypes({JdbcType.CHAR, JdbcType.VARCHAR})
 *     public class StringTrimmingTypeHandler implements TypeHandler<String> {
 *     }
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappedJdbcTypes {
    /**
     * Returns jdbc types to map {@link TypeHandler}.
     */
    JdbcType[] value();

    /**
     * Returns whether map to jdbc null type.
     *
     * @return {@code true} if map, {@code false} if otherwise
     */
    boolean includeNullJdbcType() default false;
}
