package com.github.paganini2008.springplayer.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.github.paganini2008.devtools.enums.EnumConstant;
import com.github.paganini2008.devtools.enums.EnumUtils;

/**
 * 
 * GenericEnumTypeHandler
 *
 * @author Feng Yan
 * @version 1.0.0
 */
public class SimpleEnumTypeHandler<E extends EnumConstant> extends BaseTypeHandler<E> {

	private final Class<E> enumClass;

	public SimpleEnumTypeHandler(Class<E> enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getValue());
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		int value = rs.getInt(columnName);
		return rs.wasNull() ? null : EnumUtils.valueOf(enumClass, value);
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		int value = rs.getInt(columnIndex);
		return rs.wasNull() ? null : EnumUtils.valueOf(enumClass, value);
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		int value = cs.getInt(columnIndex);
		return cs.wasNull() ? null : EnumUtils.valueOf(enumClass, value);
	}

	public Class<E> getEnumClass() {
		return enumClass;
	}

}
