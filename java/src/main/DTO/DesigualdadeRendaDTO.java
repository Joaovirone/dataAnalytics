package com.dataAnalytics.dataAnalytics.dto;

import java.util.Map;

public record DesigualdadeRendaDTO(
    String indicador,
    Map<String, Double> mediasPorRenda
) {}