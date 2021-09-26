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
package org.apache.ibatis.parsing;

/**
 * 符号解析器, 通过指定符号的前缀、后缀, 以及文本替换器{@link TokenHandler}, 解析成新文本.
 * 比方说, 文本串为"abc${ppp}edf", 那么就可以替换出找出"${}"包裹的"ppp"子串, 用{@link TokenHandler}
 * 更换为新的子串, 比如说：abc?edf.
 */
public class GenericTokenParser {

    /**
     * 指定起始符号和终止符号, 比方说, "#{"和"}", 那么
     * 就会从 "#{" 开始解析直至遇到 "}", 找到这段子串, 然后交给
     * {@link TokenHandler}转换.
     */
    private final String openToken;
    private final String closeToken;

    /**
     * 文本串处理, 将解析到的文本串更换为新串.
     */
    private final TokenHandler handler;

    public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.handler = handler;
    }

    public String parse(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        // 确定token起始符号, 如果为-1说明不包含, 直接返回文本值
        int start = text.indexOf(openToken);
        if (start == -1) {
            return text;
        }
        char[] src = text.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        do {
            // openToken的前一个字符为"\\", 表示它被转义, 那么就要重新选取下一个openToken
            if (start > 0 && src[start - 1] == '\\') {
                // builder拼接上从offset位置到openToken这一段的子串, 再加上openToken, 比方说：abc#{
                builder.append(src, offset, start - offset - 1).append(openToken);
                // 越过当前这个openToken, 重新赋值给offset
                offset = start + openToken.length();
            } else {
                // 如果没有转义, 那么就可以开始寻找closeToken了
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                // builder拼接上从offset位置到openToken这一段的子串(因为这个类的作用在于替换掉那些符号, 所以还是需要原串)
                builder.append(src, offset, start - offset);
                // 从openToken后面的字符串开始找, 避免了每次都要整个字符串一起找.
                offset = start + openToken.length();
                // 确定closeToken的起始位置
                int end = text.indexOf(closeToken, offset);
                // 如果存在endToken
                while (end > -1) {
                    // 同上面代码一样, 也是确定closeToken不存在转义字符
                    if (end > offset && src[end - 1] == '\\') {
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        // 代码走到这里, 说明已经找到closeOpen, 就截取从openToken到closeToken
                        // 之间的文本串
                        expression.append(src, offset, end - offset);
                        break;
                    }
                }
                // 不存在endToken, 退出循环
                if (end == -1) {
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    // 拼接上表达式子串, 同时用 tokenHandler 转换字符串
                    builder.append(handler.handleToken(expression.toString()));
                    offset = end + closeToken.length();
                }
            }
            // 从offset往后的位置重新找openToken
            start = text.indexOf(openToken, offset);
        } while (start > -1);
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }
}
