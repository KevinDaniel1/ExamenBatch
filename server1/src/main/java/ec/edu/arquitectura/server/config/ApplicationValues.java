package ec.edu.arquitectura.server.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationValues {
  private final String mongoHost;
  private final String mongoDB;
  private final String mongoUsr;
  private final String mongoPwd;
  private final String mongoAut;

  public ApplicationValues(
      @Value("${ec.edu.espe.arquitectura.mongo.host}") String mongoHost,
      @Value("${ec.edu.espe.arquitectura.mongo.db}") String mongoDB,
      @Value("${ec.edu.espe.arquitectura.mongo.usr}") String mongoUsr,
      @Value("${ec.edu.espe.arquitectura.mongo.pwd}") String mongoPwd,
      @Value("${ec.edu.espe.arquitectura.mongo.aut}") String mongoAut) {

    this.mongoHost = mongoHost;
    this.mongoDB = mongoDB;
    this.mongoUsr = mongoUsr;
    this.mongoPwd = mongoPwd;
    this.mongoAut = mongoAut;
  }
}

