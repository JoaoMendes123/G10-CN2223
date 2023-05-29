import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;

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
}
