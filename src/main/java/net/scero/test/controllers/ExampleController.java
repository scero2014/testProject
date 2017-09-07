package net.scero.test.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.scero.test.core.TokenSecurity;
import net.scero.test.core.User;
import net.scero.test.db.ThreadResultHandler;
import net.scero.test.db.mappers.TestDBMapper;
import net.scero.test.db.pojos.ExampleDBTuple;
import net.scero.test.mongodb.EntityRepository;
import net.scero.test.mongodb.EntityTest;

@Controller
@Slf4j
public class ExampleController {
    //---- Variables ----//
    // Para mongo
    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private MongoClient      mongoClient;

    @Autowired
    private TestDBMapper     testDBMapper;

    @Resource
    private TokenSecurity tokenSecurity;
    
    //---- Constructors ----//

    //---- Public Methods ----//
    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ResponseBody
    public ResponseEntity<String> defaultEndpoint() {
        HttpHeaders headers = new HttpHeaders();
        String result;
        HttpStatus httpStatus;
        try {
            result = "Hola mundo";
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, headers, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/aop")
    @ResponseBody
    public ResponseEntity<String> aopTestEndpoint(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String result;
        HttpStatus httpStatus;
        try {
            result = "Aop example";
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, headers, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/secure")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<String> securityTestEndpoint(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String result;
        HttpStatus httpStatus;
        try {
            result = "Ejecutado";
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, headers, httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/ip")
    @RolesAllowed("ROLE_IP")
    public ResponseEntity<String> securityIpTestEndpoint(HttpServletRequest request) {
        String result;
        HttpStatus httpStatus;
        try {
            result = "Ejecutado";
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, new HttpHeaders(), httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/storage")
    public ResponseEntity<String> storageEndpoint(HttpServletRequest request) {
        String result;
        HttpStatus httpStatus;
        try {
            StringBuilder sb = new StringBuilder();

            sb.append("Fichero: prueba.txt <br/>");
            sb.append(processFile(new File("prueba.txt")));
            sb.append("<br/><br/>");

            sb.append("Fichero: /data/prueba.txt <br/>");
            sb.append(processFile(new File("/data/prueba.txt")));
            sb.append("<br/><br/>");

            sb.append("Fichero: /data/testproject/prueba.txt <br/>");
            sb.append(processFile(new File("/data/testproject/prueba.txt")));
            sb.append("<br/><br/>");

            sb.append("Fichero: /testproject/data/prueba.txt <br/>");
            sb.append(processFile(new File("/testproject/data/prueba.txt")));
            sb.append("<br/><br/>");

            result = sb.toString();

            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, new HttpHeaders(), httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/mongo")
    public ResponseEntity<String> mongoEndpoint(HttpServletRequest request) {
        String result;
        HttpStatus httpStatus;
        try {
            entityRepository.save(new EntityTest("Jose", 20));
            StringBuilder sb = new StringBuilder();
            sb.append("findAll <br/>");
            for (EntityTest entityTest : entityRepository.findAll()) {
                sb.append("Elemento: ").append(entityTest.toString()).append("<br/>");
            }

            sb.append("<br/>").append("findByAge").append("<br/>");
            try {
                for (EntityTest entityTest : entityRepository.findByAge(21)) {
                    sb.append("Elemento: ").append(entityTest.toString()).append("<br/>");
                }
            } catch (Exception e) {
                sb.append("Petardazo");
            }
            sb.append("<br/>").append("findByNameAndAge").append("<br/>");
            try {
                for (EntityTest entityTest : entityRepository.findByNameAndAge("Jose", 21)) {
                    sb.append("Elemento: ").append(entityTest.toString()).append("<br/>");
                }
            } catch (Exception e) {
                sb.append("Petardazo");
            }

            sb.append("<br/>").append("findAll with example").append("<br/>");
            try {
                EntityTest filter = new EntityTest();
                filter.setAge(20);
                for (EntityTest entityTest : entityRepository.findAll(Example.of(filter))) {
                    sb.append("Elemento: ").append(entityTest.toString()).append("<br/>");
                }
            } catch (Exception e) {
                sb.append("Petardazo");
            }

            MongoCursor<String> it = mongoClient.getDatabase("sampledb").listCollectionNames().iterator();
            sb.append("<br/>").append("listCollectionNames: ").append("<br/>");
            while (it.hasNext()) {
                sb.append(it.next()).append("<br/>");
            }

            MongoCollection<Document> collection = mongoClient.getDatabase("sampledb").getCollection("entityTest");
            MongoCursor<Document> cursor = collection.find(new Document().append("name", "Jose")).iterator();
            sb.append("<br/>").append("Busqueda directa por find: ").append("<br/>");
            while (cursor.hasNext()) {
                sb.append(cursor.next()).append("<br/>");
            }

            result = sb.toString();
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, new HttpHeaders(), httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/postgres")
    public ResponseEntity<String> postgresEndpoint(HttpServletRequest request) {
        String result;
        HttpStatus httpStatus;
        try {
            testDBMapper.createTableIfNotExist();

            List<ExampleDBTuple> elements = testDBMapper.findAll("jose");
            int id;
            if (!elements.isEmpty()) {
                id = elements.stream().map(e -> e.getId()).max(Comparator.comparing(e -> e)).get() + 1;
            } else {
                id = 1;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("El siguiente id: ").append(id).append("<br/><br/>");

            ExampleDBTuple exampleDBTuple = new ExampleDBTuple();
            exampleDBTuple.setNombre(new StringBuilder().append("jose n.").append(id).toString());
            exampleDBTuple.setEdadElemento(10 + id);
            testDBMapper.create(id, exampleDBTuple);

            for (ExampleDBTuple item : testDBMapper.findAll("jose")) {
                sb.append(item).append("<br/>");
            }

            sb.append("--- Acceso por handler ---<br/>");
            UserHandler userHandler = new UserHandler();
            testDBMapper.findAllHandler("jose", userHandler);
            sb.append("Suma de todas las edades: ").append(userHandler.getSumaEdades()).append("<br/>");

            sb.append("--- Acceso por ThreadResultHandler (id >=5 && id <= 8) ---<br/>");
            ThreadResultHandler<ExampleDBTuple> th = new ThreadResultHandler<ExampleDBTuple>() {
                @Override
                public void query() throws Exception {
                    testDBMapper.findAllHandler("jose", this);
                }
            };

            th.start();
            while((exampleDBTuple = th.next()) != null) {
                if (exampleDBTuple.getId() >= 5 && exampleDBTuple.getId() <= 8) {
                    sb.append(exampleDBTuple).append("<br/>");
                }
            }
            th.release();

            result = sb.toString();
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, new HttpHeaders(), httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/createToken/{id}/{user}")
    public ResponseEntity<String> createToken(HttpServletRequest request, @PathVariable int id, @PathVariable String user) {
        String result;
        HttpStatus httpStatus;
        try {
            result = tokenSecurity.createJWT(new User(user, id, "WEBUSER"), 30);
            httpStatus = HttpStatus.OK;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, new HttpHeaders(), httpStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getUserFromToken")
    public ResponseEntity<String> getUserFromToken(HttpServletRequest request) {
        String result;
        HttpStatus httpStatus;
        String token = request.getHeader("token");
        try {
            User user = tokenSecurity.authenticate(token);
            result = user.toString();
            httpStatus = HttpStatus.OK;
        } catch (ExpiredJwtException e) {
            result = "El tokey " + token + " ha expirado";
            log.info(result);
            httpStatus = HttpStatus.UNAUTHORIZED;
        } catch (JwtException e) {
            result = "Error de seguridad al intentar auteticar el token " + token;
            log.info(result, e);
            httpStatus = HttpStatus.UNAUTHORIZED;
        } catch (Exception e) {
            result = e.toString();
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, new HttpHeaders(), httpStatus);
    }

    //---- Private Methods ----//
    private String processFile(File file) {
        String result;
        try {
            if (!file.exists()) {
                file.createNewFile();
                if (!file.exists()) {
                    result = "Se intenta crear file, pero no se puede";
                } else {
                    result = "File creado";
                }
            } else if (!file.canRead()) {
                result = "No se puede leer";
            } else if (!file.canWrite()) {
                result = "No se puede escribir";
            } else {
                BufferedWriter oWriter = new BufferedWriter(new FileWriter(file, true));
                oWriter.write("Un pollo <br/>");
                oWriter.close();

                StringBuilder sb = new StringBuilder();
                BufferedReader oReader = new BufferedReader(new FileReader(file));
                String line = oReader.readLine();
                while (line != null) {
                    sb.append(line);
                    line = oReader.readLine();
                }
                oReader.close();
                result = sb.toString();
            }
        } catch (IOException e) {
            result = e.toString();
        }
        return result;
    }

    //---- Private Classes ----//
    class UserHandler implements ResultHandler<ExampleDBTuple> {
        @Getter
        private int sumaEdades;

        public UserHandler() {
            sumaEdades = 0;
        }

        @Override
        public void handleResult(ResultContext<? extends ExampleDBTuple> arg0) {
            ExampleDBTuple exampleDBTuple = arg0.getResultObject();
            sumaEdades += exampleDBTuple.getEdadElemento();
        }

    }
}
