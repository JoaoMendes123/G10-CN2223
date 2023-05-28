import Contract.*;
import Contract.Void;
import io.grpc.stub.StreamObserver;

import java.util.UUID;
import java.util.logging.Logger;

public class ContractMockImplementation extends ContractGrpc.ContractImplBase {
    public static Logger logger = Logger.getLogger(Server.class.getName());
    private final StorageCalls storage;
    private final PubSubCalls pubSub;

    public ContractMockImplementation(StorageCalls storage, PubSubCalls pubSub){
        this.storage = storage;
        this.pubSub = pubSub;
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
    public void getMap(ImageId request, StreamObserver<GetImageMap> responseObserver) {
        //TODO
        super.getMap(request, responseObserver);
    }

    @Override
    public void getLandmarkInfo(ImageId request, StreamObserver<GetImageInfo> responseObserver) {
        //TODO
        super.getLandmarkInfo(request, responseObserver);
    }
}
