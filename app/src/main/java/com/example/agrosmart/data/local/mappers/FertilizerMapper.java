package com.example.agrosmart.data.local.mappers;

import com.example.agrosmart.data.local.dto.FertilizerDTO;
import com.example.agrosmart.domain.models.Fertilizer;

public class FertilizerMapper {

    public static Fertilizer toModel(FertilizerDTO dto) {
        Fertilizer fertilizer = new Fertilizer();
        fertilizer.setName(dto.getName());
        fertilizer.setApplicationMethod(dto.getApplicationMethod());
        fertilizer.setRecommendedDose(dto.getRecommendedDose());
        fertilizer.setDescription(dto.getDescription());
        fertilizer.setSupplier(dto.getSupplier());
        fertilizer.setType(dto.getType());
        return fertilizer;
    }
}
