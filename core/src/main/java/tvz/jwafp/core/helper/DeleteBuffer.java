package tvz.jwafp.core.helper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class DeleteBuffer {
    private Map<String, BooleanWrapper> items;
}
