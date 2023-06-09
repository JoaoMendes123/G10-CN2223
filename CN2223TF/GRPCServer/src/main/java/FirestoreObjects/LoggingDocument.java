package FirestoreObjects;

import Contract.LandmarkProtoResult;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.annotation.Exclude;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoggingDocument {
    public String requestId;
    public String bucket_name;
    public String blob_name;
    public List<LandmarkResult> results;
    public String dateOfCreation;

    public LoggingDocument(String requestId,
                           String bucket_name,
                           String blob_name,
                           List<LandmarkResult> results
    ){
        this.requestId = requestId;
        this.bucket_name = bucket_name;
        this.blob_name = blob_name;
        this.results = results;
        this.dateOfCreation = LocalDateTime.now().toString();
    }


    public LoggingDocument(){

    }


}
