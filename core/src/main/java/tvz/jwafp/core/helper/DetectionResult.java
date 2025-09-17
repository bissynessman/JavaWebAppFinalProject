package tvz.jwafp.core.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class DetectionResult {
    private BigDecimal score;
    @JsonProperty("sentence_scores")
    private List<SentenceScore> sentenceScores;
    private String text;
    @JsonProperty("token_probs")
    private List<BigDecimal> tokenProbs;
    private List<String> tokens;

    @Getter
    @Setter
    public static class SentenceScore {
        private BigDecimal score;
        private String sentence;
    }
}
