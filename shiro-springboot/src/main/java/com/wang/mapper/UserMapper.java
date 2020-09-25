package com.wang.mapper;

import com.wang.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    User queryUserByName(@Param("name") String name);
}
