package dk.sdu.mmmi.cbse.main;

import org.springframework.web.client.RestTemplate;

public class ScoreClient {

    private static final String SCORE_SERVICE_URL = "http://localhost:8080";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static long totalScore = 0;

    public static long sendPoints(long points) {
        try {
            String response = restTemplate.getForObject(
                    SCORE_SERVICE_URL + "/score?point=" + points, String.class
            );
            totalScore = Long.parseLong(response);
            return totalScore;
        } catch (Exception e) {
            return totalScore;
        }
    }

    public static long getScore() {
        return totalScore;
    }
}
