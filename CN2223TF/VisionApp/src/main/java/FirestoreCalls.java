import FirestoreDocumentObjects.LoggingDocument;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class FirestoreCalls {
    private final Logger logger = Logger.getLogger(FirestoreCalls.class.getName());
    private final CollectionReference colRef;

    public FirestoreCalls(CollectionReference colRef) {
        this.colRef = colRef;
    }

    public void insertDocument(LoggingDocument doc){
        String docName = doc.requestId;
        try {
            //Creates a document in collection referred by colRef
            DocumentReference docRef = colRef.document(docName);
            ApiFuture<WriteResult> resFut = docRef.create(doc);
            //Required to update the Firestore db, if future is not consulted, no effects are taken in the db
            WriteResult result = null;
            result = resFut.get();
            logger.info("Document created with name: " + docName);
            logger.info("Update Time:" + result.getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
