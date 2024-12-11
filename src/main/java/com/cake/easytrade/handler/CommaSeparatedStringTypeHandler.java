package com.cake.easytrade.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class CommaSeparatedStringTypeHandler implements TypeHandler<List<String>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        // Serialize List to a comma-separated string or set NULL if the List is empty/null
        ps.setString(i, parameter == null || parameter.isEmpty() ? null : String.join(",", parameter));
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        // Deserialize from a comma-separated string to a List or return an empty List
        String value = rs.getString(columnName);
        return value == null || value.isEmpty() ? new ArrayList<>() : Arrays.asList(value.split(","));
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null || value.isEmpty() ? new ArrayList<>() : Arrays.asList(value.split(","));
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null || value.isEmpty() ? new ArrayList<>() : Arrays.asList(value.split(","));
    }
}

