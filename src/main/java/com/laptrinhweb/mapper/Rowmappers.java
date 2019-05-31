package com.laptrinhweb.mapper;

import java.sql.ResultSet;

public interface Rowmappers<T> {
	T mapRow(ResultSet rs);
}
