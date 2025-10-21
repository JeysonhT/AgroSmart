package com.example.agrosmart.domain.models.mappers;

import com.example.agrosmart.data.local.dto.CropDTO;
import com.example.agrosmart.domain.models.Crop;

public class CropMapper {

    // De Realm a DTO (para guardar o enviar a Firestore)
    public static CropDTO toDto(Crop crop) {
        if (crop == null) return null;
        return new CropDTO(
                crop.getCropName(),
                crop.getDescription(),
                crop.getHarvestTime(),
                crop.getType()
        );
    }

    // De DTO a Realm (para guardar localmente)
    public static Crop toEntity(CropDTO dto) {
        if (dto == null) return null;
        Crop crop = new Crop();
        crop.setCropName(dto.getCropName());
        crop.setDescription(dto.getDescription());
        crop.setHarvestTime(dto.getContent());
        crop.setType(dto.getType());
        return crop;
    }
}
