import FirestoreObjects.LandmarkResult;
import FirestoreObjects.LoggingDocument;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

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


    /**
     * Gets DocumentReference of Document with docId.
     * @param docId - requestID.
     */
    public DocumentReference getDocumentReference(String docId){
        return colRef.document(docId);

    }


    /**
     * Gets complete LoggingDocument from docReference.
     * @param doc - DocumentReference to request.
     * @return Complete LoggingDocument.
     */
    public LoggingDocument getLoggingDocumentByReference(DocumentReference doc){

        try {
            LoggingDocument logDoc = doc.get().get().toObject(LoggingDocument.class);
            if(logDoc != null){
                logDoc.results = getLandmarkResultsFromReference(doc);
            }
            return logDoc;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param doc - DocumenteReference to request.
     * @return list of LandmarkResult from a specific request.
     */
    public List<LandmarkResult> getLandmarkResultsFromReference(DocumentReference doc){
        ArrayList<LandmarkResult> list = new ArrayList<>();
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
     * Goes through every request and checks which ones have results with score > t
     * @param t - minimum score
     * @return list of results with > t
     */

}
