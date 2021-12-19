package kalva.mc.vulnerable;

//import org.apache.log4j.Logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private static final Logger logger = LogManager.getLogger("HelloWorld");
//    final static Logger logger = Logger.getLogger(MainController.class.getName());

    @GetMapping("/")
    public String index(@RequestHeader("X-Api-Version") String apiVersion) {
        int i=10;
        logger.info("Received a request for API version " + apiVersion);
        return "Hello, world!";
    }
}
