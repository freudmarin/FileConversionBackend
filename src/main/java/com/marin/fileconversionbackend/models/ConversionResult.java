package com.marin.fileconversionbackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConversionResult {
    private String message;
    private String filePath;
}
