package org.example.generator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.generator.domain.LeadsGroup;

import java.util.List;

/**
* @author daocr
* @description 针对表【leads_group(栗子队列)】的数据库操作Mapper
* @createDate 2024-07-14 23:02:56
* @Entity org.example.generator.domain.LeadsGroup
*/

@Mapper
public interface LeadsGroupMapper {

    int deleteByPrimaryKey(Long id);

    int insert(LeadsGroup record);

    int insertSelective(LeadsGroup record);

    LeadsGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LeadsGroup record);

    int updateByPrimaryKey(LeadsGroup record);

    LeadsGroup selectByIdIn(@Param("ids") List<String> ids);

}
