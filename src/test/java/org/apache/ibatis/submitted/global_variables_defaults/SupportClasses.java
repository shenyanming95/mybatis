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
package org.apache.ibatis.submitted.global_variables_defaults;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

import java.lang.reflect.Field;
import java.util.Properties;

public class SupportClasses {

    public static class CustomObjectFactory extends DefaultObjectFactory {
        private static final long serialVersionUID = 4576592418878031661L;
        private Properties properties;

        public Properties getProperties() {
            return properties;
        }

        @Override
        public void setProperties(Properties properties) {
            this.properties = properties;
        }
    }

    public static class CustomCache extends PerpetualCache {
        private String name;

        public CustomCache(String id) {
            super(id);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class Utils {
        static SupportClasses.CustomCache unwrap(Cache cache) {
            Field field;
            try {
                field = cache.getClass().getDeclaredField("delegate");
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException(e);
            }
            try {
                field.setAccessible(true);
                return (SupportClasses.CustomCache) field.get(cache);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } finally {
                field.setAccessible(false);
            }
        }
    }

}
