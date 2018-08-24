package com.xoado.FilingCabinet.mapper;

import com.xoado.FilingCabinet.bean.TblHandoverRecord;
import com.xoado.FilingCabinet.bean.TblHandoverRecordExample;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TblHandoverRecordMapper {
    int countByExample(TblHandoverRecordExample example);

    int deleteByExample(TblHandoverRecordExample example);

    int deleteByPrimaryKey(String handoverId);

    int insert(TblHandoverRecord record);

    int insertSelective(TblHandoverRecord record);

    List<TblHandoverRecord> selectByExample(TblHandoverRecordExample example);
    
    List<LinkedHashMap<String, Object>> selectHandover(@Param("archiveid")String archiveid,@Param("handoverid")String handoverid);
//  当前资料的交接详情
    List<LinkedHashMap<String, Object>> selecthandovertoken(String sql);
    
    List<LinkedHashMap<String, Object>> selectExpress(String sql);

    TblHandoverRecord selectByPrimaryKey(String handoverId);

    int updateByExampleSelective(@Param("record") TblHandoverRecord record, @Param("example") TblHandoverRecordExample example);

    int updateByExample(@Param("record") TblHandoverRecord record, @Param("example") TblHandoverRecordExample example);

    int updateByPrimaryKeySelective(TblHandoverRecord record);

    int updateByPrimaryKey(TblHandoverRecord record);
}