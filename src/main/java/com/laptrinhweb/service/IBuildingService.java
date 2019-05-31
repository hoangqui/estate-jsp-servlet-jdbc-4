package com.laptrinhweb.service;

import com.laptrinhweb.dto.BuildingDTO;
import com.laptrinhweb.entity.BuildingEntity;

public interface IBuildingService {
	BuildingDTO save(BuildingDTO buildingDTO);
}
