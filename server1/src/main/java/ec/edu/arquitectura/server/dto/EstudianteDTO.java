package ec.edu.arquitectura.server.dto;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EstudianteDTO {
    
    private String nombres;

    private String apellidos;

    private String cedula;

    private Integer nivel;
}
