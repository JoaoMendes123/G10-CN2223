import Contract.*;
import Contract.Void;
import io.grpc.stub.StreamObserver;

public class ContractMockImplementation extends ContractGrpc.ContractImplBase {

    @Override
    public void isIpAlive(Void request, StreamObserver<IpReply> responseObserver) {
        responseObserver.onNext(IpReply.newBuilder().setAlive(true).build());
        responseObserver.onCompleted();
    }

    @Override
    public void submitImage(Image request, StreamObserver<ImageId> responseObserver) {
        responseObserver.onNext(ImageId.newBuilder().setId("Message Received").build());
        responseObserver.onCompleted();
    }

    @Override
    public void getMap(ImageId request, StreamObserver<GetImageMap> responseObserver) {
        super.getMap(request, responseObserver);
    }

    @Override
    public void getLandmarkInfo(ImageId request, StreamObserver<GetImageInfo> responseObserver) {
        super.getLandmarkInfo(request, responseObserver);
    }
}
