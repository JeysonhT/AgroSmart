package com.example.agrosmart.domain.designModels;

import android.graphics.Bitmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
@Builder
public class NewsInfoDto {
    private Bitmap image;
    private String title;
    private String publicationDate;
    private String author;
    private String content;
}
