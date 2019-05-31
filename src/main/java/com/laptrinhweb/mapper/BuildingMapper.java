package com.laptrinhweb.mapper;

import java.sql.ResultSet;

import com.laptrinhweb.dto.BuildingDTO;

public class BuildingMapper implements Rowmappers<BuildingDTO> {

	@Override
	public BuildingDTO mapRow(ResultSet rs) {
		try {
			BuildingDTO buildingModel = new BuildingDTO();
			buildingModel.setName(rs.getString("name"));
			buildingModel.setNumberOfBasement(rs.getInt("numberofbasement"));
			buildingModel.setBuildingArea(rs.getInt("buildingarea"));
			buildingModel.setWard(rs.getString("ward"));
			buildingModel.setStreet(rs.getString("street"));
			buildingModel.setStructure(rs.getString("structure"));
			
			
			return buildingModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
