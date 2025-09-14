package tvz.jwafp.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tvz.jwafp.core.helper.DetectionResult;
import tvz.jwafp.core.helper.Messages;
import tvz.jwafp.core.utils.JsonParser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiDetectionService {
    private static final String API_URL = "https://api.sapling.ai/api/v1/aidetect";
    private static final String API_KEY = "KKFK2EUPFYF6A04QC9Z7RZJFZ6EJY8HU";

    private final RestTemplate restTemplate;
    private final Messages messages;

    @Autowired
    public AiDetectionService(@Qualifier("apiRestTemplate") RestTemplate restTemplate, Messages messages) {
        this.restTemplate = restTemplate;
        this.messages = messages;
    }

    public Map<String, Object> check(String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Content-Type", "application/json");
        String sanitized = sanitize(content);
        String requestBody = "{\"key\":\"" + API_KEY +"\", \"text\":\"" + sanitized + "\"}";

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        DetectionResult result = null;
        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);

            result = JsonParser.parseIntoObject(response.getBody(), DetectionResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return interpret(result);
    }

    private Map<String, Object> interpret(DetectionResult detectionResult) {
        final int SCALE = 12;
        final RoundingMode RM = RoundingMode.HALF_UP;
        final BigDecimal ONE = BigDecimal.ONE.setScale(SCALE, RM);
        final BigDecimal ZERO = BigDecimal.ZERO.setScale(SCALE, RM);

        final BigDecimal ALPHA_BASE = new BigDecimal("0.5").setScale(SCALE, RM);
        final BigDecimal VAR_WEIGHT  = new BigDecimal("1.0").setScale(SCALE, RM);
        final BigDecimal DISC_WEIGHT = new BigDecimal("1.0").setScale(SCALE, RM);

        final BigDecimal TH_090 = new BigDecimal("0.90").setScale(SCALE, RM);
        final BigDecimal TH_070 = new BigDecimal("0.70").setScale(SCALE, RM);
        final BigDecimal TH_040 = new BigDecimal("0.40").setScale(SCALE, RM);

        Map<String, Object> out = new HashMap<>();

        if (detectionResult == null) {
            out.put("overallScore", null);
            out.put("weightedSentenceMean", null);
            out.put("weightedVariance", null);
            out.put("discrepancy", null);
            out.put("alpha", ONE);
            out.put("adjustedScore", null);
            out.put("classification", "Unknown");
            return out;
        }

        BigDecimal overallRaw = detectionResult.getScore();
        BigDecimal overall;
        if (overallRaw == null) {
            overall = ZERO;
        } else {
            overall = overallRaw.setScale(SCALE, RM);
            if (overall.compareTo(ZERO) < 0) overall = ZERO;
            if (overall.compareTo(ONE) > 0) overall = ONE;
        }
        out.put("overallScore", overall);

        List<DetectionResult.SentenceScore> sentenceScores = detectionResult.getSentenceScores();
        if (sentenceScores == null || sentenceScores.isEmpty()) {
            out.put("weightedSentenceMean", null);
            out.put("weightedVariance", null);
            out.put("discrepancy", null);
            out.put("alpha", ONE);
            out.put("adjustedScore", overall);
            String classification;
            String level;
            if (overall.compareTo(TH_090) >= 0) {
                classification = "Very Likely AI";
                level = "red";
            }
            else if (overall.compareTo(TH_070) >= 0) {
                classification = "Likely AI";
                level = "red";
            }
            else if (overall.compareTo(TH_040) >= 0) {
                classification = "Uncertain";
                level = "yellow";
            }
            else {
                classification = "Likely Human";
                level = "green";
            }
            out.put("classification", classification);
            out.put("classificationLevel", level);
            return out;
        }

        final int nSentences = sentenceScores.size();
        List<BigDecimal> lengths = new ArrayList<>(nSentences);
        BigDecimal totalLength = BigDecimal.ZERO.setScale(SCALE, RM);
        for (DetectionResult.SentenceScore sentenceScore : sentenceScores) {
            String text = sentenceScore.getSentence();
            int length = (text == null) ? 1 : text.length();
            BigDecimal bdLength = new BigDecimal(length).setScale(SCALE, RM);
            lengths.add(bdLength);
            totalLength = totalLength.add(bdLength);
        }
        if (totalLength.compareTo(ZERO) == 0) {
            totalLength = new BigDecimal(nSentences).setScale(SCALE, RM);
            lengths.clear();
            for (int i = 0; i < nSentences; ++i) lengths.add(BigDecimal.ONE.setScale(SCALE, RM));
        }

        List<BigDecimal> weights = new ArrayList<>(nSentences);
        for (int i = 0; i < nSentences; ++i) {
            BigDecimal weight = lengths.get(i).divide(totalLength, SCALE, RM);
            weights.add(weight);
        }

        BigDecimal scoresWeightedSum = BigDecimal.ZERO.setScale(SCALE, RM);
        BigDecimal squaredScoresWeightedSum = BigDecimal.ZERO.setScale(SCALE, RM);
        for (int i = 0; i < nSentences; ++i) {
            BigDecimal scoreRaw = sentenceScores.get(i).getScore();
            BigDecimal score = (scoreRaw == null) ? ZERO : scoreRaw.setScale(SCALE, RM);
            if (score.compareTo(ZERO) < 0) score = ZERO;
            if (score.compareTo(ONE) > 0) score = ONE;
            BigDecimal weight = weights.get(i);
            scoresWeightedSum = scoresWeightedSum.add(weight.multiply(score));
            squaredScoresWeightedSum = squaredScoresWeightedSum.add(weight.multiply(score.pow(2).setScale(SCALE, RM)));
        }

        BigDecimal weightedMean = scoresWeightedSum.setScale(SCALE, RM);
        BigDecimal weightedVar = squaredScoresWeightedSum.subtract(weightedMean.pow(2)).setScale(SCALE, RM);

        if (weightedVar.compareTo(BigDecimal.ZERO) < 0
                && weightedVar.abs().compareTo(new BigDecimal("1e-18")) < 0) {
            weightedVar = BigDecimal.ZERO.setScale(SCALE, RM);
        }

        out.put("weightedSentenceMean", weightedMean);
        out.put("weightedVariance", weightedVar);

        BigDecimal discrepancy = overall.subtract(weightedMean).abs().setScale(SCALE, RM);
        out.put("discrepancy", discrepancy);

        BigDecimal alphaRaw = ALPHA_BASE
                .add(VAR_WEIGHT.multiply(weightedVar))
                .add(DISC_WEIGHT.multiply(discrepancy))
                .setScale(SCALE, RM);
        BigDecimal alpha;
        if (alphaRaw.compareTo(ZERO) < 0) alpha = ZERO;
        else if (alphaRaw.compareTo(ONE) > 0) alpha = ONE;
        else alpha = alphaRaw;
        out.put("alpha", alpha);

        BigDecimal adjusted = alpha.multiply(overall)
                .add(ONE.subtract(alpha).multiply(weightedMean))
                .setScale(SCALE, RM);
        out.put("adjustedScore", adjusted);

        String classification;
        String level;
        if (adjusted.compareTo(TH_090) >= 0) {
            classification = messages.getMessage("detection.result-very-likely-ai");
            level = "red";
        }
        else if (adjusted.compareTo(TH_070) >= 0) {
            classification = messages.getMessage("detection.result-likely-ai");
            level = "red";
        }
        else if (adjusted.compareTo(TH_040) >= 0) {
            classification = messages.getMessage("detection.result-uncertain");
            level = "yellow";
        }
        else {
            classification = messages.getMessage("detection.result-likely-human");
            level = "green";
        }
        out.put("classification", classification);
        out.put("classificationLevel", level);

        return out;
    }

    private static String sanitize(String content) {
        return content.replaceAll("[\\r\\n]", "").replaceAll("\\s+", " ");
    }
}
