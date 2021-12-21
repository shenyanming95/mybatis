/**
 * Copyright 2009-2016 the original author or authors.
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
package org.apache.ibatis.parsing;

import java.util.Properties;

/**
 * 替换原始串中被"${}"包裹的文本串, 比方说：
 * abc${name}ee, 指定一个{key=name, value=1}的变量集, 就会自动替换成: abc1ee
 */
public class PropertyParser {

    private static final String KEY_PREFIX = "org.apache.ibatis.parsing.PropertyParser.";
    public static final String KEY_ENABLE_DEFAULT_VALUE = KEY_PREFIX + "enable-default-value";
    public static final String KEY_DEFAULT_VALUE_SEPARATOR = KEY_PREFIX + "default-value-separator";

    private static final String ENABLE_DEFAULT_VALUE = "false";
    private static final String DEFAULT_VALUE_SEPARATOR = ":";

    private PropertyParser() {
        // Prevent Instantiation
    }

    /**
     * 这个方法的效果：用参数variables里面的值, 去替换参数string里面用"${}"包裹的子串.
     * 比方说, 原串- abc${name}dd, 变量值数据集-{name:JAVA}, 那么解析完就变成：abcJAVAdd.
     *
     * @param string    原串
     * @param variables 变量值数据集
     * @return 解析后的字符串
     */
    public static String parse(String string, Properties variables) {
        VariableTokenHandler handler = new VariableTokenHandler(variables);
        GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
        return parser.parse(string);
    }

    private static class VariableTokenHandler implements TokenHandler {

        // 变量集, 从里面找到key对应的value
        private final Properties variables;

        // 是否开启默认值
        private final boolean enableDefaultValue;

        // 默认的分隔符, 是":"
        private final String defaultValueSeparator;

        private VariableTokenHandler(Properties variables) {
            this.variables = variables;
            this.enableDefaultValue = Boolean.parseBoolean(getPropertyValue(KEY_ENABLE_DEFAULT_VALUE, ENABLE_DEFAULT_VALUE));
            this.defaultValueSeparator = getPropertyValue(KEY_DEFAULT_VALUE_SEPARATOR, DEFAULT_VALUE_SEPARATOR);
        }

        private String getPropertyValue(String key, String defaultValue) {
            return (variables == null) ? defaultValue : variables.getProperty(key, defaultValue);
        }

        @Override
        public String handleToken(String content) {
            // 如果变量集不为空, 那就从里面获取
            if (variables != null) {
                String key = content;
                if (enableDefaultValue) {
                    // 获取分隔符的下标
                    final int separatorIndex = content.indexOf(defaultValueSeparator);
                    String defaultValue = null;
                    // 如果分隔符存在
                    if (separatorIndex >= 0) {
                        // 截取分隔符之前的字符串, 作为key
                        key = content.substring(0, separatorIndex);
                        // 截取分隔符之后的字符串, 作为value
                        defaultValue = content.substring(separatorIndex + defaultValueSeparator.length());
                    }
                    // 然后再从变量集中捞出指定key的值, 捞不到才用默认值.
                    if (defaultValue != null) {
                        return variables.getProperty(key, defaultValue);
                    }
                }
                // 如果没启用默认值, 直接从变量集中获取
                if (variables.containsKey(key)) {
                    return variables.getProperty(key);
                }
            }
            // 如果变量集为空, 那么直接拼接上"${}"
            return "${" + content + "}";
        }
    }

}
