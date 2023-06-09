import DocumentObjects.LandmarkResult;
import DocumentObjects.LoggingDocument;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class FirestoreCalls {
    private final Logger logger = Logger.getLogger(FirestoreCalls.class.getName());
    private final CollectionReference COLLECTION_REFERENCE;

    public FirestoreCalls(CollectionReference colRef) {
        this.COLLECTION_REFERENCE = colRef;
    }

    public void insertDocument(LoggingDocument doc){
        String docName = doc.requestId;
        try {
            //Creates a document in collection referred by COLLECTION_REFERENCE
            DocumentReference docRef = COLLECTION_REFERENCE.document(docName);
            //create collection results in document
            //Required to update the Firestore db, if future is not consulted, no effects are taken in the db
            CollectionReference resultsCol = docRef.collection("results");
            for (LandmarkResult lres: doc.results) {
                 DocumentReference resultRef = resultsCol.document(lres.name);
                 resultRef.create(lres);
            }
            ApiFuture<WriteResult> resFut = docRef.create(doc);
            WriteResult result = resFut.get();
            logger.info("Document created with name: " + docName);
            logger.info("Inserted requestDocument " + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            logger.warning(e.getMessage());

        }
    }
}
