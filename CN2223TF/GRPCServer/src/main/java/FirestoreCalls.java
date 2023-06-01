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

    public List<LandmarkResult> getLandmarkResultsFromRequest(String id){
        ArrayList<LandmarkResult> list = new ArrayList<>();
        DocumentReference doc = colRef.document(id);
        CollectionReference resultsCollection = doc.collection("results");
        resultsCollection.listDocuments().forEach(resultDoc ->{
            ApiFuture<DocumentSnapshot> snapshot = resultDoc.get();
                    try {
                        DocumentSnapshot docSnap = snapshot.get();
                        list.add(docSnap.toObject(LandmarkResult.class));
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                });
        return list;
    }



    /**
     * TODO
     * @param
     */
    public List<LandmarkResult> getLandmarkResultsWithT(double t){
        ArrayList<LandmarkResult> results = new ArrayList<>();
        Iterable<DocumentReference> docs = colRef.listDocuments();
        docs.forEach(documentReference -> {
            CollectionReference resultsCollection = documentReference.collection("results");
            Query query = resultsCollection.whereGreaterThan("score", t);
            ApiFuture<QuerySnapshot> snapshot = query.get();
            try {
                List<QueryDocumentSnapshot> resultsWithT = snapshot.get().getDocuments();
                resultsWithT.forEach(result -> {
                    results.add(result.toObject(LandmarkResult.class));
                });

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        return results;
        /**
        try {
            ApiFuture<DocumentSnapshot> docSnap = colRef.where()
            DocumentSnapshot doc = docSnap.get();
            LoggingDocument l = doc.toObject(LoggingDocument.class);
            logger.info("test log : " + l.blob_name);
            logger.info("test log : " + l.results.get(0).name);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }*/

    }
}
