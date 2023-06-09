package DocumentObjects;
import com.google.cloud.firestore.annotation.Exclude;

import java.time.LocalDateTime;
import java.util.List;

public class LoggingDocument {

    @Exclude
    public final List<LandmarkResult> results;
    public String requestId;
    public String bucket_name;
    public String blob_name;
    public String dateOfCreation;

    public LoggingDocument(String requestId,
                           String bucket_name,
                           String blob_name,
                           List<LandmarkResult> results
    ){
        this.requestId = requestId;
        this.bucket_name = bucket_name;
        this.blob_name = blob_name;
        this.dateOfCreation = LocalDateTime.now().toString();
        this.results = results;
    }

}
