package dk.sdu.mmmi.cbse.main;

import org.springframework.web.client.RestTemplate;

public class ScoreClient {

    private static final String SCORE_SERVICE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    public long sendPoints(long points) {
        try {
            String response = restTemplate.getForObject(
                    SCORE_SERVICE_URL + "/score?point=" + points, String.class
            );
            long result = Long.parseLong(response);
            System.out.println("✅ Sent " + points + " points. Total score now: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("❌ Failed to send score: " + e.getMessage());
            return -1;
        }
    }
}

