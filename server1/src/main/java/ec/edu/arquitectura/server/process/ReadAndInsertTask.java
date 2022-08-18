package ec.edu.arquitectura.server.process;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
public class ReadAndInsertTask implements Tasklet, StepExecutionListener{
    
    private static final String BASE_URL = "http://localhost:8080/Estudiante";
    private final RestTemplate restTemplate;

    List<EstudianteDTO> estudiantes = new ArrayList<>();

    public ReadAndInsertTask() {
        this.restTemplate = new RestTemplate(getClientHttpRequestFactory());
    }
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        File file = new File("C:\\Arquitectura\\EXAMEN_TERCER_PARCIAL\\Ejemplos\\estudiante.txt");
         Scanner scanner;
         try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
				Scanner delimitar = new Scanner(linea);
                delimitar.useDelimiter("\\s*,\\s*");
                EstudianteDTO dto = EstudianteDTO.builder()
                    .cedula(delimitar.next())
                    .apellidos(delimitar.next())
                    .nombres(delimitar.next())
                    .nivel(Integer.parseInt(delimitar.next()))
                    .build();
                estudiantes.add(dto);
            }
            scanner.close();
         } catch (FileNotFoundException  e) {
            log.error("Error: {}", e.getMessage());
         }
        
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        for(EstudianteDTO dto : estudiantes){ 
            Map<String, String> map = new HashMap<>();
            map.put("cedula", dto.getCedula());
            map.put("apellidos", dto.getApellidos());
            map.put("nombres", dto.getNombres());
            map.put("nivel", dto.getNivel().toString());
            ResponseEntity<EstudianteDTO> response = this.restTemplate.postForEntity(BASE_URL, map, EstudianteDTO.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Request Successful");
            } else {
                log.error("Request Failed");
            }
        }
        estudiantes.clear();
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
