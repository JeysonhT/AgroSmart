package com.example.agrosmart.data.local.mappers;

import com.example.agrosmart.data.local.dto.DeficiencyDTO;
import com.example.agrosmart.domain.models.Deficiency;

public class DeficiencyMapper {

    public static Deficiency toModel(DeficiencyDTO dto) {
        Deficiency deficiency = new Deficiency();
        deficiency.setName(dto.getTitle());
        deficiency.setDescription(dto.getDescription());
        deficiency.setSymptoms(dto.getSymptoms());
        deficiency.setSolutions(dto.getSolutions());
        // imageResource is not mapped
        return deficiency;
    }
}
