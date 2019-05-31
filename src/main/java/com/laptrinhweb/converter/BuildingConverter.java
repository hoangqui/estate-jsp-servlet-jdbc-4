package com.laptrinhweb.converter;

import org.modelmapper.ModelMapper;

import com.laptrinhweb.dto.BuildingDTO;
import com.laptrinhweb.entity.BuildingEntity;

public class BuildingConverter {
	
	public BuildingDTO converterToDTO(BuildingEntity buildingEntity) {
		ModelMapper modelMapper = new ModelMapper();
		BuildingDTO result = modelMapper.map(buildingEntity, BuildingDTO.class);
		return result;
	}
	
	public BuildingEntity converterToEntity(BuildingDTO buildingDTO) {
		ModelMapper modelMapper = new ModelMapper();
		BuildingEntity result = modelMapper.map(buildingDTO, BuildingEntity.class);
		return result;
	}
}
