import Contract.*;
import Contract.Void;
import com.google.cloud.firestore.CollectionReference;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ContractMockImplementation extends ContractGrpc.ContractImplBase {
    public static Logger logger = Logger.getLogger(Server.class.getName());
    private final StorageCalls storage;

    public ContractMockImplementation(StorageCalls storage){
        this.storage = storage;
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
            String blobId = storage.uploadBlobToBucket(img, request.getName(), request.getType());
            responseObserver.onNext(ImageId.newBuilder().setId(blobId).build());
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
