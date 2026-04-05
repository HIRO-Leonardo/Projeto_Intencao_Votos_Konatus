package Projeto_Votos.main.enums;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


public enum GrupoMunicipio {
    GRUPO_0,
    GRUPO_1, // Até 20 mil
    GRUPO_2, // 20k a 100k
    GRUPO_3, // 100k a 1 milhão
    GRUPO_4;  // Mais de 1 milhão
}
