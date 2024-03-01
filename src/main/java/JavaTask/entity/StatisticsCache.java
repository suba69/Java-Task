package JavaTask.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@AllArgsConstructor
@Document(value = "cache")
public class StatisticsCache {

    @Id
    private String id;
    private String selectedDate;
    private String cachedResult;

    public StatisticsCache() {
    }

}