package co.com.bancolombia.commons.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttachmentType {
    public static final String PATH = "Path";
    public static final String URL = "Url";
    public static final String BASE64 = "Base64";
}
