import Contract.ContractGrpc;
import Contract.ExistingIps;
import Contract.Void;
import io.grpc.stub.StreamObserver;

public class Server extends ContractGrpc.ContractImplBase {
    @Override
    public void getIps(Void request, StreamObserver<ExistingIps> responseObserver) {
        super.getIps(request, responseObserver);
    }


    public static void main(String[] args) {

    }
}
