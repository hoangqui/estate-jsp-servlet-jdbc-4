package com.laptrinhweb.mapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.laptrinhweb.annotation.Column;
import com.laptrinhweb.annotation.Entity;

public class ResultSetMapper<T> {
	
	public List<T> mapRow(ResultSet rs, Class zClass){
		
		List<T> result = new ArrayList<>();
		try {
			if(zClass.isAnnotationPresent(Entity.class)) {
				ResultSetMetaData resultsetMetaData = rs.getMetaData();
				Field[] fields = zClass.getDeclaredFields();
				while(rs.next()) {
					T object = (T) zClass.newInstance();
					// get gia tri cua 1 row trong resultSet va set trong entity
					for(int i = 0; i < resultsetMetaData.getColumnCount(); i++) {
						String columName = resultsetMetaData.getColumnName(i + 1);
						Object columnValue = rs.getObject(i + 1);
						// current class
						for(Field field: fields) {
							if(field.isAnnotationPresent(Column.class)) {
								Column column = field.getAnnotation(Column.class);
								if(column.name().equals(columName) && columnValue != null) {
									BeanUtils.setProperty(object, field.getName(), columnValue);
									break;
								}
							}
						}
						//parent class
						Class<?> parentsClass = zClass.getSuperclass();
						while(parentsClass != null) {
							Field[] fieldsParents = parentsClass.getDeclaredFields();
							for(Field field: fields) {
								if(field.isAnnotationPresent(Column.class)) {
									Column column = field.getAnnotation(Column.class);
									if(column.name().equals(columName) && columnValue != null) {
										BeanUtils.setProperty(object, field.getName(), columnValue);
										break;
									}
								}
							}
							parentsClass = parentsClass.getSuperclass();
						}

					}
					result.add(object); // ngoai vong for
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
}
