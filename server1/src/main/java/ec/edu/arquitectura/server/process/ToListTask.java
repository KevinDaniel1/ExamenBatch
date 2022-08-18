package ec.edu.arquitectura.server.process;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.util.FileUtils;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import ec.edu.arquitectura.server.dto.EstudianteDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ToListTask implements Tasklet, StepExecutionListener{
    
    private static final String BASE_URL = "http://localhost:8080/Paralelos";
    private static final String BASE_URL2 = "http://localhost:8080/Paralelos/ParalelosNivel";
    private final RestTemplate restTemplate;

    private FileUtils fileUtils;

    List<EstudianteDTO> estudiantes = new ArrayList<>();

    public ToListTask() {
        this.restTemplate = new RestTemplate(getClientHttpRequestFactory());
    }
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        ResponseEntity<EstudianteDTO[]> response = this.restTemplate.getForEntity(BASE_URL, EstudianteDTO[].class);
        EstudianteDTO[] objectArray = response.getBody();
        estudiantes = Arrays.asList(objectArray);
        
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileWriter flwriter = null;
        try {
            flwriter = new FileWriter("C:\\Arquitectura\\EXAMEN_TERCER_PARCIAL\\Ejemplos\\estudiantesNivel.txt",true);
            BufferedWriter bfwriter = new BufferedWriter(flwriter);
            for(EstudianteDTO dto : estudiantes){    
                bfwriter.write(dto.getCedula()+","+dto.getApellidos()+","+dto.getNombres()+","+dto.getNivel()+"\n");
            }
            bfwriter.close();
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
        } finally {
            if (flwriter != null) {
				try {//cierra el flujo principal
					flwriter.close();
				} catch (IOException e) {
                    log.error("Error: {}", e.getMessage());
				}
			}
        }
        return null;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
       
        return null;
    }


    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        int connectTimeout = 5000;
        int readTimeout = 5000;
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);
        return clientHttpRequestFactory;
    }
   
}
