package org.example;

import Contract.*;
import Contract.Void;
import io.grpc.stub.StreamObserver;

public class ContractImplementation extends ContractGrpc.ContractImplBase {


    @Override
    public void getIps(Void request, StreamObserver<ExistingIps> responseObserver) {
        super.getIps(request, responseObserver);
    }

    @Override
    public void isIpAlive(IpAliveRequest request, StreamObserver<IpReply> responseObserver) {
        super.isIpAlive(request, responseObserver);
    }
}
