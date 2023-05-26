import Contract.*;
import Contract.Void;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ContractMockImplementation extends ContractGrpc.ContractImplBase {
    public static Logger logger = Logger.getLogger(Server.class.getName());
    @Override
    public void isIpAlive(Void request, StreamObserver<IpReply> responseObserver) {
        responseObserver.onNext(IpReply.newBuilder().setAlive(true).build());
        logger.info("Completed isAlive()");
        responseObserver.onCompleted();
    }

    @Override
    public void submitImage(Image request, StreamObserver<ImageId> responseObserver) {
        responseObserver.onNext(ImageId.newBuilder().setId("Message Received").build());
        logger.info("Completed submitImage()");
        responseObserver.onCompleted();
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
