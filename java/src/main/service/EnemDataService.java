package com.dataAnalytics.dataAnalytics.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class EnemDataService {

    // Caminho para o seu arquivo local (pode ser a amostra pequena primeiro para testar)
    private static final String FILE_PATH = "C:/caminho/para/sua/amostra_enem.csv"; 

    public Map<String, Double> calcularMediaPorRenda() throws Exception {
        Map<String, Double> somaNotas = new HashMap<>();
        Map<String, Integer> contagemAlunos = new HashMap<>();

        // Lendo o arquivo em stream para não estourar a memória
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                String renda = record.get("Q006");
                
                // Pegando apenas a nota de matemática como exemplo
                String notaMtStr = record.get("NU_NOTA_MT");
                
                if (notaMtStr != null && !notaMtStr.trim().isEmpty()) {
                    double notaMt = Double.parseDouble(notaMtStr);
                    
                    somaNotas.put(renda, somaNotas.getOrDefault(renda, 0.0) + notaMt);
                    contagemAlunos.put(renda, contagemAlunos.getOrDefault(renda, 0) + 1);
                }
            }
        }

        // Calculando as médias finais
        Map<String, Double> mediaFinal = new HashMap<>();
        for (String renda : somaNotas.keySet()) {
            double media = somaNotas.get(renda) / contagemAlunos.get(renda);
            mediaFinal.put(renda, Math.round(media * 100.0) / 100.0); // Arredondando para 2 casas
        }

        return mediaFinal;
    }
}