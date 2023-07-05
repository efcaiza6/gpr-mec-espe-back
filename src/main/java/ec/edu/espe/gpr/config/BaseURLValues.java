package ec.edu.espe.gpr.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BaseURLValues {
    private final String gprStorageURL;

    public BaseURLValues(@Value("${gpr.storage.base-url}") String gprStorageURL) {
        this.gprStorageURL = gprStorageURL;
    }
}
