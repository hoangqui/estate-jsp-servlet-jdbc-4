package com.laptrinhweb.service.impl;

import com.laptrinhweb.converter.BuildingConverter;
import com.laptrinhweb.dto.BuildingDTO;
import com.laptrinhweb.entity.BuildingEntity;
import com.laptrinhweb.repository.impl.BuildingRepository;
import com.laptrinhweb.service.IBuildingService;

public class BuildingService implements IBuildingService{

	private BuildingRepository buildingRepository;
	public BuildingService(){
		buildingRepository = new BuildingRepository();
	}
	@Override
	public BuildingDTO save(BuildingDTO buildingDTO) {
		BuildingConverter buildingConverter = new BuildingConverter();
		BuildingEntity buildingEntity = buildingConverter.converterToEntity(buildingDTO);
//		Long id = buildingRepository.insert(buildingEntity);
//		System.out.println(id);
//		buildingRepository.update(buildingEntity);
		buildingRepository.delete(buildingEntity);
		
		return null;
	}

	
}
