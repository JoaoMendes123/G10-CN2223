import Contract.*;
import Contract.LandmarkProtoResult;
import Contract.Void;
import FirestoreObjects.LandmarkResult;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    public void submitImage(Image request, StreamObserver<ImageId> responseObserver) {
        byte[] img = request.getImage().toByteArray();
        try {
            //RequestId to return to client for later interactions
            String requestID = UUID.randomUUID().toString().substring(0,4);
            String blobId = storage.uploadImageToBucket(img, request.getName(), request.getType());
            pubSub.sendMessage(blobId, storage.getBucketName(), requestID);

            responseObserver.onNext(ImageId.newBuilder().setId(requestID).build());
            responseObserver.onCompleted();

            logger.info("Completed submitImage()");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getLandmarksFromRequest(ImageId request, StreamObserver<LandmarkProtoResult> responseObserver) {
        String id = request.getId();
        DocumentSnapshot doc = fireStore.getDocumentById(id);

        if(doc.exists()){
            List<LandmarkResult> res = fireStore.getLandmarkResultsFromRequest(id);
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
}