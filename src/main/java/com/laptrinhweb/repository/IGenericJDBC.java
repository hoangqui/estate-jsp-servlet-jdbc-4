package com.laptrinhweb.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.laptrinhweb.mapper.ResultSetMapper;

public interface IGenericJDBC<T> {
	
   List<T> query(String sql, Object...parameters ) ;
   void update(String sql, Object...parameters);
   Long insert(String sql, Object...parameters);
   
   Long insert(Object object);
   void update(Object object);
   void delete(Object object);
}
