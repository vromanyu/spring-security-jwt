package com.vromanyu.spring_security_jwt_v2.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

@Component
public class SimpleFunctionCaller {

 private final JdbcTemplate jdbcTemplate;

 public SimpleFunctionCaller(JdbcTemplate jdbcTemplate) {
  this.jdbcTemplate = jdbcTemplate;
 }

 public int simpleCalculator(String functionName, int[] values){
  if (values.length != 2) {
   throw new RuntimeException("Invalid number of parameters");
  }
  SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
  SqlParameterSource params = new MapSqlParameterSource().addValue("a", values[0]).addValue("b", values[1]);
  simpleJdbcCall.withFunctionName(functionName);
  Integer res = simpleJdbcCall.executeFunction(Integer.class, params);
  return res == null ? -1 : res;
 }
}
