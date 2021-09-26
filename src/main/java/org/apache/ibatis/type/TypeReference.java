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
package org.apache.ibatis.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 引用泛型类型
 */
public abstract class TypeReference<T> {

    /**
     *  表示{@link TypeReference} 中的泛型 T
     */
    private final Type rawType;

    protected TypeReference() {
        rawType = getSuperclassTypeParameter(getClass());
    }

    Type getSuperclassTypeParameter(Class<?> clazz) {
        // 获取带泛型的父类类型, 详见此类main()方法
        Type genericSuperclass = clazz.getGenericSuperclass();
        // 如果继承的父类没有泛型, 尝试继续向上寻找.
        if (genericSuperclass instanceof Class) {
            // try to climb up the hierarchy until meet something useful
            if (TypeReference.class != genericSuperclass) {
                return getSuperclassTypeParameter(clazz.getSuperclass());
            }
            throw new TypeException("'" + getClass() + "' extends TypeReference but misses the type parameter. "
                    + "Remove the extension or add a type parameter to it.");
        }
        // 因为 TypeReference<T> 只有一个泛型, 所以直接取[0]就能拿到这个泛型
        Type rawType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        // 本身还是一个参数类型, 比如 TypeReference<List<String>>, 那就直接获取它的rawType, 拿到实际的泛型类型
        // 即例子中的String.class
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) rawType).getRawType();
        }
        return rawType;
    }

    public final Type getRawType() {
        return rawType;
    }

    @Override
    public String toString() {
        return rawType.toString();
    }

    /**
     * for test
     */
    public static void main(String[] args) {
        Example example = new Example();
        Class<? extends Example> clazz = example.getClass();

        // 一个获取父类的class类型, 一个获取带泛型的class类型
        System.out.println(clazz.getSuperclass());
        System.out.println(clazz.getGenericSuperclass());
    }

    private static class Example extends TypeReference<String> {

    }
}
