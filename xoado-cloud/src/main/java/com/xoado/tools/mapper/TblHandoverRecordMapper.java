package com.xoado.tools.mapper;

import com.xoado.tools.bean.TblHandoverRecord;
import com.xoado.tools.bean.TblHandoverRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TblHandoverRecordMapper {
    int countByExample(TblHandoverRecordExample example);

    int deleteByExample(TblHandoverRecordExample example);

    int deleteByPrimaryKey(String handoverId);

    int insert(TblHandoverRecord record);

    int insertSelective(TblHandoverRecord record);

    List<TblHandoverRecord> selectByExample(TblHandoverRecordExample example);

    TblHandoverRecord selectByPrimaryKey(String handoverId);

    int updateByExampleSelective(@Param("record") TblHandoverRecord record, @Param("example") TblHandoverRecordExample example);

    int updateByExample(@Param("record") TblHandoverRecord record, @Param("example") TblHandoverRecordExample example);

    int updateByPrimaryKeySelective(TblHandoverRecord record);

    int updateByPrimaryKey(TblHandoverRecord record);
}