package com.carpool.dao;

import com.carpool.entity.SysUserEntity;
import com.carpool.entity.UserWindowDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

/**
 * 系统用户
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2016年9月18日 上午9:34:11
 */
public interface SysContractCall {
    
	@SuppressWarnings("rawtypes")
    @Select("call condqgq()")
    @Options(statementType= StatementType.CALLABLE )
    public HashMap condqgq();
    
    @SuppressWarnings("rawtypes")
    @Select("call concqgq()")
    @Options(statementType= StatementType.CALLABLE )
    public HashMap concqgq();
    
}
