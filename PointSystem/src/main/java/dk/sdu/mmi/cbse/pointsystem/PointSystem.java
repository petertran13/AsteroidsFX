package dk.sdu.mmmi.cbse.pointsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class PointSystem {

    private Long totalScore = 0L;

    public static void main(String[] args) {
        SpringApplication.run(PointSystem.class, args);
    }

    @GetMapping(value = "/score", produces = MediaType.TEXT_PLAIN_VALUE)
    public String calculateScore(@RequestParam(value = "point") Long point) {
        totalScore += point;
        return totalScore.toString();
    }
}

