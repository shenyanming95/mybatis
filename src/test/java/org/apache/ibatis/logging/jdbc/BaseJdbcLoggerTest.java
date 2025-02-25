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
package org.apache.ibatis.logging.jdbc;

import org.apache.ibatis.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Array;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseJdbcLoggerTest {

    @Mock
    Log log;
    @Mock
    Array array;
    private BaseJdbcLogger logger;

    @BeforeEach
    void setUp() {
        logger = new BaseJdbcLogger(log, 1) {
        };
    }

    @Test
    void shouldDescribePrimitiveArrayParameter() throws Exception {
        logger.setColumn("1", array);
        when(array.getArray()).thenReturn(new int[]{1, 2, 3});
        assertThat(logger.getParameterValueString()).startsWith("[1, 2, 3]");
    }

    @Test
    void shouldDescribeObjectArrayParameter() throws Exception {
        logger.setColumn("1", array);
        when(array.getArray()).thenReturn(new String[]{"one", "two", "three"});
        assertThat(logger.getParameterValueString()).startsWith("[one, two, three]");
    }
}
