import Contract.*;
import Contract.LandmarkProtoResult;
import Contract.Void;
import FirestoreObjects.LandmarkResult;
import FirestoreObjects.LoggingDocument;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.protobuf.ByteString;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class ContractImplementation extends ContractGrpc.ContractImplBase {
    public static Logger logger = Logger.getLogger(Server.class.getName());
    private final StorageCalls storage;
    private final PubSubCalls pubSub;
    private final FirestoreCalls fireStore;

    public ContractImplementation(StorageCalls storage, PubSubCalls pubSub, FirestoreCalls fireStore){
        this.storage = storage;
        this.pubSub = pubSub;
        this.fireStore = fireStore;
    }
    @Override
    public void isIpAlive(Void request, StreamObserver<IpReply> responseObserver) {
        responseObserver.onNext(IpReply.newBuilder().setAlive(true).build());
        logger.info("Completed isAlive()");
        responseObserver.onCompleted();
    }

    @Override


    @Override
    public void getLandmarksFromRequest(ImageId request, StreamObserver<LandmarkProtoResult> responseObserver) {
        String id = request.getId();
        ApiFuture<DocumentSnapshot> doc = fireStore.getDocumentReference(id).get();
        DocumentReference ref = fireStore.getDocumentReference(id);
        try {
            if(doc.get().exists()){
                List<LandmarkResult> res = fireStore.getLandmarkResultsFromReference(ref);
                res.forEach(landmarkResult -> {
                    responseObserver.onNext(LandmarkResult.toProtoObject(landmarkResult));
                });
                responseObserver.onCompleted();
                logger.info("Completed getLandmarksFromRequest()");
            }else{
                responseObserver.onError(
                        io.grpc.Status
                                .FAILED_PRECONDITION
                                .withDescription("Couldn't find referenced document, landmarksApp has not yet processed request")
                                .asException()
                );
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getNamesFromTImage(GetImageNamesWithTRequest request, StreamObserver<LandmarkProtoResult> responseObserver) {
        double t = request.getT();
        List<LandmarkResult> results = fireStore.getLandmarkResultsWithT(t);
        for (LandmarkResult res: results) {
            logger.info("gettingImagesWithT: " + res.map_blob_name);
            responseObserver.onNext(LandmarkResult.toProtoObject(res));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getMap(MapChoice request, StreamObserver<GetImageMap> responseObserver) {

        String id = request.getId();
        int idx = request.getChoice();

        DocumentReference doc = fireStore.getDocumentReference(id);
        LoggingDocument logObj = fireStore.getLoggingDocumentByReference(doc);
        if(logObj.results.size() > idx){
            String blobName = logObj.results.get(idx).map_blob_name;
            byte[] map = storage.getMap(blobName);
            responseObserver.onNext(GetImageMap.newBuilder().setMap(ByteString.copyFrom(map)).build());
            responseObserver.onCompleted();
            logger.info("Map download successful");
        }else{
            logger.warning("idx > number of results");
            responseObserver.onError(Status.OUT_OF_RANGE.asException());
        }
    }
}

