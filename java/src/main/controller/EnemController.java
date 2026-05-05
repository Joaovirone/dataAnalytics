package com.dataAnalytics.dataAnalytics.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/enem")
public class EnemController {

    private final EnemDataService enemDataService;

    public EnemController(EnemDataService enemDataService) {
        this.enemDataService = enemDataService;
    }

    @GetMapping("/desigualdade/renda")
    public ResponseEntity<DesigualdadeRendaDTO> getDesigualdadePorRenda() {
        try {
            Map<String, Double> medias = enemDataService.calcularMediaPorRenda();
            DesigualdadeRendaDTO dto = new DesigualdadeRendaDTO("Média de Matemática por Faixa de Renda", medias);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}