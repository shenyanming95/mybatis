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
package org.apache.ibatis.binding;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.domain.blog.Blog;

import java.util.List;

public interface MapperWithOneAndMany {

    @Select({
            "SELECT *",
            "FROM blog"
    })
    @Results({
            @Result(
                    property = "author", column = "author_id",
                    one = @One(select = "org.apache.ibatis.binding.BoundAuthorMapper.selectAuthor"),
                    many = @Many(select = "org.apache.ibatis.binding.BoundBlogMapper.selectPostsById"))
    })
    List<Blog> selectWithBothOneAndMany();

}
