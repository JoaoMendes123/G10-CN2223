import FirestoreObjects.LandmarkResult;
import FirestoreObjects.LoggingDocument;
import com.google.api.Logging;
import com.google.api.core.ApiFuture;
import com.google.api.services.storage.Storage;
import com.google.cloud.firestore.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class FirestoreCalls {
    private final Logger logger = Logger.getLogger(FirestoreCalls.class.getName());
    private final CollectionReference colRef;

    public FirestoreCalls(CollectionReference colRef) {
        this.colRef = colRef;
    }

    public DocumentSnapshot getDocumentById(String docId){

        try {
            ApiFuture<DocumentSnapshot> fut = colRef.document(docId).get();
            return fut.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * TODO
     * @param id
     */
    public void getLandmarkResults(String id){

        try {
            ApiFuture<DocumentSnapshot> docSnap = colRef.document(id).get();
            DocumentSnapshot doc = docSnap.get();
            LoggingDocument l = doc.toObject(LoggingDocument.class);
            logger.info("test log : " + l.blob_name);
            logger.info("test log : " + l.results.get(0).name);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
