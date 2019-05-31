package com.laptrinhweb.repository.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.laptrinhweb.annotation.Column;
import com.laptrinhweb.annotation.Table;
import com.laptrinhweb.mapper.ResultSetMapper;
import com.laptrinhweb.repository.IGenericJDBC;

public class AbstractJDBC<T> implements IGenericJDBC<T>{
	
	private Class<T> zClass;
	
	@SuppressWarnings("unchecked")
	public AbstractJDBC() {
		Type type = getClass().getGenericSuperclass();
		ParameterizedType parameterizedType = (ParameterizedType)type;
		zClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}
	

//	public static Connection getConnect() {
//		Connection con = null;
//		String url = "jdbc:mysql://127.0.0.1:3306/estate";
//		String user = "root";
//		String password = "123456";
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			con = DriverManager.getConnection(url, user, password);
//			System.out.println("ket noi thanh cong");
//		} catch (ClassNotFoundException | SQLException e) {
//			System.out.println("ket noi that bai");
//			e.printStackTrace();
//		}
//		return con;
//	}
	private Connection getConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String databaseURL = "jdbc:mysql://localhost:3306/estate";
			String user = "root";
			String password = "123456";
			return DriverManager.getConnection(databaseURL, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<T> query(String sql, Object... parameters) {
		
	
		ResultSetMapper<T> resultSetMapper = new ResultSetMapper<>();

		try (Connection con = getConnect();
			 PreparedStatement statement = con.prepareStatement(sql);
			 ResultSet res = statement.executeQuery();){
			if(con != null) {	
				// set parameter in statement
				for(int i = 0; i < parameters.length; i++) {
					int index = i + 1;
					statement.setObject(index,parameters[i] );
				}
				return resultSetMapper.mapRow(res, this.zClass);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public void update(String sql, Object... parameters) {
		
		Connection con = null;
		PreparedStatement statement = null;
		
		try {
			
			con = getConnect();
			con.setAutoCommit(false);
		
			statement = con.prepareStatement(sql);
			
			if(con != null) {
				
				 
				// set parameter in statement
				for(int i = 0; i < parameters.length; i++) {
//					Object parameter = parameters[i];
					int index = i + 1;
					statement.setObject(index,parameters[i] );
//					if(parameter instanceof String) {
//						statement.setString(index, (String)parameter);
//					}else if (parameter instanceof Integer) {
//						statement.setInt(index, (Integer)parameter);
//					}
				}
				 
				statement.executeUpdate();
				
				con.commit();
				
			}
		} catch ( SQLException e) {
			if(con != null) {
				try {
					con.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}finally {
			try {
				if(con != null) {
					con.close();
				}
				if(statement != null) {
					statement.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public Long insert(String sql, Object... parameters) {
		
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet res = null;
		
		
		try {
			
			con = getConnect();
			con.setAutoCommit(false);
		
			statement = con.prepareStatement(sql, statement.RETURN_GENERATED_KEYS);
			
			if(con != null) {
				
				 
				// set parameter in statement
				for(int i = 0; i < parameters.length; i++) {
//					Object parameter = parameters[i];
					int index = i + 1;
					statement.setObject(index,parameters[i] );
//					if(parameter instanceof String) {
//						statement.setString(index, (String)parameter);
//					}else if (parameter instanceof Integer) {
//						statement.setInt(index, (Integer)parameter);
//					}
				}
				 
				int rowsInserted = statement.executeUpdate();
				res = statement.getGeneratedKeys();
				con.commit();
				if (rowsInserted > 0) {
					while(res.next()) {
					long id = res.getLong(1);
					return id;
					}
				}
			}
		} catch ( SQLException e) {
			if(con != null) {
				try {
					con.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}finally {
			try {
				if(con != null) {
					con.close();
				}
				if(statement != null) {
					statement.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return null;
	}

	@SuppressWarnings("static-access")
	@Override
	public Long insert(Object object) {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet res = null;
		
		
		try {
			
			con = getConnect();
			con.setAutoCommit(false);
			
			String sql = createSQLInsert();
			statement = con.prepareStatement(sql, statement.RETURN_GENERATED_KEYS);
			
			if(con != null) {
				
				 Class<?> zClass = object.getClass();
				 Field[] fields = zClass.getDeclaredFields();
				// set parameter in statement
				for(int i = 0; i < fields.length; i++) {
					int index = i + 1;
					Field field = fields[i];
					field.setAccessible(true);
					statement.setObject(index,field.get(object) );
				}
				 
				// set parent
				Class<?> parentsClass = zClass.getSuperclass();
				int indexParent = fields.length + 1;
				while(parentsClass != null) {
					for(int i = 0; i < parentsClass.getDeclaredFields().length; i++) {
						Field field = parentsClass.getDeclaredFields()[i];
						field.setAccessible(true);
						statement.setObject(indexParent,field.get(object) );
						indexParent = indexParent + 1;
					}
					parentsClass = parentsClass.getSuperclass();
				}
				
				int rowsInserted = statement.executeUpdate();
				res = statement.getGeneratedKeys();
				con.commit();
				if (rowsInserted > 0) {
					while(res.next()) {
					long id = res.getLong(1);
					return id;
					}
				}
			}
		} catch ( SQLException | IllegalAccessException e) {
			if(con != null) {
				try {
					con.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}finally {
			try {
				if(con != null) {
					con.close();
				}
				if(statement != null) {
					statement.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return null;
	}

	private String createSQLInsert() {
		String tableName ="";
		if(zClass.isAnnotationPresent(Table.class)) {
			Table table = zClass.getAnnotation(Table.class);
			tableName = table.name();
		}
		StringBuilder fields = new StringBuilder("");
		StringBuilder params = new StringBuilder("");
		for(Field field: zClass.getDeclaredFields()) {
			if(fields.length() > 1) {
				fields.append(",");
				params.append(",");
			}
			if(field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				
					fields.append(column.name());
					params.append("?");
				
			}
		}
		// Check parent class
		Class<?> parentsClass = zClass.getSuperclass();
		while(parentsClass != null) {
			Field[] fieldsParents = parentsClass.getDeclaredFields();
			for(Field field: fieldsParents) {
				if(fields.length() > 1) {
					fields.append(",");
					params.append(",");
				}
				if(field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					
						fields.append(column.name());
						params.append("?");
					
				}
			}
			parentsClass = parentsClass.getSuperclass();
		}

		String sql = "INSERT INTO " +tableName +"("+fields.toString()+")VALUES("+params.toString()+")";
		return sql;
		
	}


	@Override
	public void update(Object object) {
		Connection con = null;
		PreparedStatement statement = null;
		
		try {
			
			con = getConnect();
			con.setAutoCommit(false);
			
			String sql = createSQLUpdate();
			statement = con.prepareStatement(sql);
			
			if(con != null) {
				
				 Class<?> zClass = object.getClass();
				 Field[] fields = zClass.getDeclaredFields();
				// set parameter in statement
				for(int i = 0; i < fields.length; i++) {
					int index = i + 1;
					Field field = fields[i];
					field.setAccessible(true);
					statement.setObject(index,field.get(object) );
				}
				 
				// set parent
				Class<?> parentsClass = zClass.getSuperclass();
				int indexParent = fields.length + 1;
				Object id = null;
				while(parentsClass != null) {
					for(int i = 0; i < parentsClass.getDeclaredFields().length; i++) {
						Field field = parentsClass.getDeclaredFields()[i];
						field.setAccessible(true);
						String name = field.getName();
						if(!name.equals("id")){
							statement.setObject(indexParent,field.get(object) );
							indexParent = indexParent + 1;
						}else {
							id = field.get(object);
						}
						
					}
					parentsClass = parentsClass.getSuperclass();
				}
				 statement.setObject(indexParent,id);
				 statement.executeUpdate();
				
				con.commit();
			}
		} catch ( SQLException | IllegalAccessException e) {
			if(con != null) {
				try {
					con.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}finally {
			try {
				if(con != null) {
					con.close();
				}
				if(statement != null) {
					statement.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	
		
	}


	private String createSQLUpdate() {
		String tableName ="";
		if(zClass.isAnnotationPresent(Table.class)) {
			Table table = zClass.getAnnotation(Table.class);
			tableName = table.name();
		}
		StringBuilder sets = new StringBuilder("");
		String where = null;
		for(Field field: zClass.getDeclaredFields()) {
			
			if(field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				String columnName = column.name();
				String value = columnName + " = ? ";
				if(!columnName.equals("id")) {
					if(sets.length() > 1) {
						sets.append(", ");
					}
					sets.append(value);
				}		
				
			}
		}
		// Check parent class
		Class<?> parentsClass = zClass.getSuperclass();
		while(parentsClass != null) {
			Field[] fieldsParents = parentsClass.getDeclaredFields();
			for(Field field: fieldsParents) {	
				if(field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					String columnName = column.name();
					String value = columnName + " = ? ";
					if(!columnName.equals("id")) {
						if(sets.length() > 1) {
							sets.append(", ");
						}
						sets.append(value);
					}else {
						where = " WHERE " + value;
					}
					
				}
			}
			parentsClass = parentsClass.getSuperclass();
		}

		String sql = "UPDATE " +tableName +" SET "+ sets.toString()+ where;
		return sql;
	}


	@Override
	public void delete(Object object) {
		Connection con = null;
		PreparedStatement statement = null;
		
		try {
			
			con = getConnect();
			con.setAutoCommit(false);
			
			String sql = createSQLDelete();
//			update(sql, id);
			
			statement = con.prepareStatement(sql);
			
			if(con != null) {
				
				 Class<?> zClass = object.getClass();
				 Field[] fields = zClass.getDeclaredFields();
				// set parameter in statement
				for(int i = 0; i < fields.length; i++) {
					int index = i + 1;
					Field field = fields[i];
					field.setAccessible(true);
					statement.setObject(index,field.get(object) );
				}
				 
				// set parent
				Class<?> parentsClass = zClass.getSuperclass();
				int indexParent = fields.length + 1;
				Object id = null;
				while(parentsClass != null) {
					for(int i = 0; i < parentsClass.getDeclaredFields().length; i++) {
						Field field = parentsClass.getDeclaredFields()[i];
						field.setAccessible(true);
						String name = field.getName();
//						if(!name.equals("id")){
//							statement.setObject(indexParent,field.get(object) );
//							indexParent = indexParent + 1;
//						}else {
//							id = field.get(object);
//						}
						id = field.get(object);
					}
					parentsClass = parentsClass.getSuperclass();
				}
				 statement.setObject(indexParent,id);
				 statement.executeUpdate();
				
				con.commit();
			}
		} catch ( SQLException | IllegalAccessException e) {
			if(con != null) {
				try {
					con.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}finally {
			try {
				if(con != null) {
					con.close();
				}
				if(statement != null) {
					statement.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}


	private String createSQLDelete() {
		String tableName ="";
		if(zClass.isAnnotationPresent(Table.class)) {
			Table table = zClass.getAnnotation(Table.class);
			tableName = table.name();
		}
		StringBuilder sets = new StringBuilder("");
		String where = null;
		for(Field field: zClass.getDeclaredFields()) {
			
			if(field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				String columnName = column.name();
				
				if(!columnName.equals("id")) {
					sets.append(columnName);
				}	
				
			}
		}
		// Check parent class
		Class<?> parentsClass = zClass.getSuperclass();
		while(parentsClass != null) {
			Field[] fieldsParents = parentsClass.getDeclaredFields();
			for(Field field: fieldsParents) {	
				if(field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					String columnName = column.name();
					String value = columnName + " = ? ";
					if(columnName.equals("id")) {
						where = " WHERE " + value;
					}
					
				}
			}
			parentsClass = parentsClass.getSuperclass();
		}

		String sql = "DELETE FROM " +tableName + where;
		return sql;
	}
	

	
}
