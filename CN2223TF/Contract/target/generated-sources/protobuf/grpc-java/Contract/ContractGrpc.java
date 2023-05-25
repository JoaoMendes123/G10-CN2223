package Contract;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.28.1)",
    comments = "Source: Contract.proto")
public final class ContractGrpc {

  private ContractGrpc() {}

  public static final String SERVICE_NAME = "Contract.Contract";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<Contract.Void,
      Contract.ExistingIps> getGetIpsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getIps",
      requestType = Contract.Void.class,
      responseType = Contract.ExistingIps.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<Contract.Void,
      Contract.ExistingIps> getGetIpsMethod() {
    io.grpc.MethodDescriptor<Contract.Void, Contract.ExistingIps> getGetIpsMethod;
    if ((getGetIpsMethod = ContractGrpc.getGetIpsMethod) == null) {
      synchronized (ContractGrpc.class) {
        if ((getGetIpsMethod = ContractGrpc.getGetIpsMethod) == null) {
          ContractGrpc.getGetIpsMethod = getGetIpsMethod =
              io.grpc.MethodDescriptor.<Contract.Void, Contract.ExistingIps>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getIps"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Contract.Void.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Contract.ExistingIps.getDefaultInstance()))
              .setSchemaDescriptor(new ContractMethodDescriptorSupplier("getIps"))
              .build();
        }
      }
    }
    return getGetIpsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<Contract.IpAliveRequest,
      Contract.IpReply> getIsIpAliveMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "isIpAlive",
      requestType = Contract.IpAliveRequest.class,
      responseType = Contract.IpReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<Contract.IpAliveRequest,
      Contract.IpReply> getIsIpAliveMethod() {
    io.grpc.MethodDescriptor<Contract.IpAliveRequest, Contract.IpReply> getIsIpAliveMethod;
    if ((getIsIpAliveMethod = ContractGrpc.getIsIpAliveMethod) == null) {
      synchronized (ContractGrpc.class) {
        if ((getIsIpAliveMethod = ContractGrpc.getIsIpAliveMethod) == null) {
          ContractGrpc.getIsIpAliveMethod = getIsIpAliveMethod =
              io.grpc.MethodDescriptor.<Contract.IpAliveRequest, Contract.IpReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "isIpAlive"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Contract.IpAliveRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Contract.IpReply.getDefaultInstance()))
              .setSchemaDescriptor(new ContractMethodDescriptorSupplier("isIpAlive"))
              .build();
        }
      }
    }
    return getIsIpAliveMethod;
  }

  private static volatile io.grpc.MethodDescriptor<Contract.SubmitImagesRequest,
      Contract.SubmitImagesReply> getSubmitImageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "submitImage",
      requestType = Contract.SubmitImagesRequest.class,
      responseType = Contract.SubmitImagesReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<Contract.SubmitImagesRequest,
      Contract.SubmitImagesReply> getSubmitImageMethod() {
    io.grpc.MethodDescriptor<Contract.SubmitImagesRequest, Contract.SubmitImagesReply> getSubmitImageMethod;
    if ((getSubmitImageMethod = ContractGrpc.getSubmitImageMethod) == null) {
      synchronized (ContractGrpc.class) {
        if ((getSubmitImageMethod = ContractGrpc.getSubmitImageMethod) == null) {
          ContractGrpc.getSubmitImageMethod = getSubmitImageMethod =
              io.grpc.MethodDescriptor.<Contract.SubmitImagesRequest, Contract.SubmitImagesReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "submitImage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Contract.SubmitImagesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Contract.SubmitImagesReply.getDefaultInstance()))
              .setSchemaDescriptor(new ContractMethodDescriptorSupplier("submitImage"))
              .build();
        }
      }
    }
    return getSubmitImageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<Contract.ImageId,
      Contract.GetImageInfo> getGetLandmarkInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getLandmarkInfo",
      requestType = Contract.ImageId.class,
      responseType = Contract.GetImageInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<Contract.ImageId,
      Contract.GetImageInfo> getGetLandmarkInfoMethod() {
    io.grpc.MethodDescriptor<Contract.ImageId, Contract.GetImageInfo> getGetLandmarkInfoMethod;
    if ((getGetLandmarkInfoMethod = ContractGrpc.getGetLandmarkInfoMethod) == null) {
      synchronized (ContractGrpc.class) {
        if ((getGetLandmarkInfoMethod = ContractGrpc.getGetLandmarkInfoMethod) == null) {
          ContractGrpc.getGetLandmarkInfoMethod = getGetLandmarkInfoMethod =
              io.grpc.MethodDescriptor.<Contract.ImageId, Contract.GetImageInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getLandmarkInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Contract.ImageId.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Contract.GetImageInfo.getDefaultInstance()))
              .setSchemaDescriptor(new ContractMethodDescriptorSupplier("getLandmarkInfo"))
              .build();
        }
      }
    }
    return getGetLandmarkInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<Contract.ImageId,
      Contract.GetImageMap> getGetMapMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getMap",
      requestType = Contract.ImageId.class,
      responseType = Contract.GetImageMap.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<Contract.ImageId,
      Contract.GetImageMap> getGetMapMethod() {
    io.grpc.MethodDescriptor<Contract.ImageId, Contract.GetImageMap> getGetMapMethod;
    if ((getGetMapMethod = ContractGrpc.getGetMapMethod) == null) {
      synchronized (ContractGrpc.class) {
        if ((getGetMapMethod = ContractGrpc.getGetMapMethod) == null) {
          ContractGrpc.getGetMapMethod = getGetMapMethod =
              io.grpc.MethodDescriptor.<Contract.ImageId, Contract.GetImageMap>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getMap"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Contract.ImageId.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Contract.GetImageMap.getDefaultInstance()))
              .setSchemaDescriptor(new ContractMethodDescriptorSupplier("getMap"))
              .build();
        }
      }
    }
    return getGetMapMethod;
  }

  private static volatile io.grpc.MethodDescriptor<Contract.GetImageNamesWithTRequest,
      Contract.GetImageNamesWithTResponse> getGetNamesFromTImageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getNamesFromTImage",
      requestType = Contract.GetImageNamesWithTRequest.class,
      responseType = Contract.GetImageNamesWithTResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<Contract.GetImageNamesWithTRequest,
      Contract.GetImageNamesWithTResponse> getGetNamesFromTImageMethod() {
    io.grpc.MethodDescriptor<Contract.GetImageNamesWithTRequest, Contract.GetImageNamesWithTResponse> getGetNamesFromTImageMethod;
    if ((getGetNamesFromTImageMethod = ContractGrpc.getGetNamesFromTImageMethod) == null) {
      synchronized (ContractGrpc.class) {
        if ((getGetNamesFromTImageMethod = ContractGrpc.getGetNamesFromTImageMethod) == null) {
          ContractGrpc.getGetNamesFromTImageMethod = getGetNamesFromTImageMethod =
              io.grpc.MethodDescriptor.<Contract.GetImageNamesWithTRequest, Contract.GetImageNamesWithTResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getNamesFromTImage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Contract.GetImageNamesWithTRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Contract.GetImageNamesWithTResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ContractMethodDescriptorSupplier("getNamesFromTImage"))
              .build();
        }
      }
    }
    return getGetNamesFromTImageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ContractStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ContractStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ContractStub>() {
        @java.lang.Override
        public ContractStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ContractStub(channel, callOptions);
        }
      };
    return ContractStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ContractBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ContractBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ContractBlockingStub>() {
        @java.lang.Override
        public ContractBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ContractBlockingStub(channel, callOptions);
        }
      };
    return ContractBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ContractFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ContractFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ContractFutureStub>() {
        @java.lang.Override
        public ContractFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ContractFutureStub(channel, callOptions);
        }
      };
    return ContractFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class ContractImplBase implements io.grpc.BindableService {

    /**
     */
    public void getIps(Contract.Void request,
        io.grpc.stub.StreamObserver<Contract.ExistingIps> responseObserver) {
      asyncUnimplementedUnaryCall(getGetIpsMethod(), responseObserver);
    }

    /**
     */
    public void isIpAlive(Contract.IpAliveRequest request,
        io.grpc.stub.StreamObserver<Contract.IpReply> responseObserver) {
      asyncUnimplementedUnaryCall(getIsIpAliveMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<Contract.SubmitImagesRequest> submitImage(
        io.grpc.stub.StreamObserver<Contract.SubmitImagesReply> responseObserver) {
      return asyncUnimplementedStreamingCall(getSubmitImageMethod(), responseObserver);
    }

    /**
     */
    public void getLandmarkInfo(Contract.ImageId request,
        io.grpc.stub.StreamObserver<Contract.GetImageInfo> responseObserver) {
      asyncUnimplementedUnaryCall(getGetLandmarkInfoMethod(), responseObserver);
    }

    /**
     */
    public void getMap(Contract.ImageId request,
        io.grpc.stub.StreamObserver<Contract.GetImageMap> responseObserver) {
      asyncUnimplementedUnaryCall(getGetMapMethod(), responseObserver);
    }

    /**
     */
    public void getNamesFromTImage(Contract.GetImageNamesWithTRequest request,
        io.grpc.stub.StreamObserver<Contract.GetImageNamesWithTResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetNamesFromTImageMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetIpsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                Contract.Void,
                Contract.ExistingIps>(
                  this, METHODID_GET_IPS)))
          .addMethod(
            getIsIpAliveMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                Contract.IpAliveRequest,
                Contract.IpReply>(
                  this, METHODID_IS_IP_ALIVE)))
          .addMethod(
            getSubmitImageMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                Contract.SubmitImagesRequest,
                Contract.SubmitImagesReply>(
                  this, METHODID_SUBMIT_IMAGE)))
          .addMethod(
            getGetLandmarkInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                Contract.ImageId,
                Contract.GetImageInfo>(
                  this, METHODID_GET_LANDMARK_INFO)))
          .addMethod(
            getGetMapMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                Contract.ImageId,
                Contract.GetImageMap>(
                  this, METHODID_GET_MAP)))
          .addMethod(
            getGetNamesFromTImageMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                Contract.GetImageNamesWithTRequest,
                Contract.GetImageNamesWithTResponse>(
                  this, METHODID_GET_NAMES_FROM_TIMAGE)))
          .build();
    }
  }

  /**
   */
  public static final class ContractStub extends io.grpc.stub.AbstractAsyncStub<ContractStub> {
    private ContractStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ContractStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ContractStub(channel, callOptions);
    }

    /**
     */
    public void getIps(Contract.Void request,
        io.grpc.stub.StreamObserver<Contract.ExistingIps> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetIpsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void isIpAlive(Contract.IpAliveRequest request,
        io.grpc.stub.StreamObserver<Contract.IpReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getIsIpAliveMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<Contract.SubmitImagesRequest> submitImage(
        io.grpc.stub.StreamObserver<Contract.SubmitImagesReply> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getSubmitImageMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void getLandmarkInfo(Contract.ImageId request,
        io.grpc.stub.StreamObserver<Contract.GetImageInfo> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetLandmarkInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getMap(Contract.ImageId request,
        io.grpc.stub.StreamObserver<Contract.GetImageMap> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetMapMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getNamesFromTImage(Contract.GetImageNamesWithTRequest request,
        io.grpc.stub.StreamObserver<Contract.GetImageNamesWithTResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetNamesFromTImageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ContractBlockingStub extends io.grpc.stub.AbstractBlockingStub<ContractBlockingStub> {
    private ContractBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ContractBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ContractBlockingStub(channel, callOptions);
    }

    /**
     */
    public Contract.ExistingIps getIps(Contract.Void request) {
      return blockingUnaryCall(
          getChannel(), getGetIpsMethod(), getCallOptions(), request);
    }

    /**
     */
    public Contract.IpReply isIpAlive(Contract.IpAliveRequest request) {
      return blockingUnaryCall(
          getChannel(), getIsIpAliveMethod(), getCallOptions(), request);
    }

    /**
     */
    public Contract.GetImageInfo getLandmarkInfo(Contract.ImageId request) {
      return blockingUnaryCall(
          getChannel(), getGetLandmarkInfoMethod(), getCallOptions(), request);
    }

    /**
     */
    public Contract.GetImageMap getMap(Contract.ImageId request) {
      return blockingUnaryCall(
          getChannel(), getGetMapMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<Contract.GetImageNamesWithTResponse> getNamesFromTImage(
        Contract.GetImageNamesWithTRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getGetNamesFromTImageMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ContractFutureStub extends io.grpc.stub.AbstractFutureStub<ContractFutureStub> {
    private ContractFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ContractFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ContractFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<Contract.ExistingIps> getIps(
        Contract.Void request) {
      return futureUnaryCall(
          getChannel().newCall(getGetIpsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<Contract.IpReply> isIpAlive(
        Contract.IpAliveRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getIsIpAliveMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<Contract.GetImageInfo> getLandmarkInfo(
        Contract.ImageId request) {
      return futureUnaryCall(
          getChannel().newCall(getGetLandmarkInfoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<Contract.GetImageMap> getMap(
        Contract.ImageId request) {
      return futureUnaryCall(
          getChannel().newCall(getGetMapMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_IPS = 0;
  private static final int METHODID_IS_IP_ALIVE = 1;
  private static final int METHODID_GET_LANDMARK_INFO = 2;
  private static final int METHODID_GET_MAP = 3;
  private static final int METHODID_GET_NAMES_FROM_TIMAGE = 4;
  private static final int METHODID_SUBMIT_IMAGE = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ContractImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ContractImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_IPS:
          serviceImpl.getIps((Contract.Void) request,
              (io.grpc.stub.StreamObserver<Contract.ExistingIps>) responseObserver);
          break;
        case METHODID_IS_IP_ALIVE:
          serviceImpl.isIpAlive((Contract.IpAliveRequest) request,
              (io.grpc.stub.StreamObserver<Contract.IpReply>) responseObserver);
          break;
        case METHODID_GET_LANDMARK_INFO:
          serviceImpl.getLandmarkInfo((Contract.ImageId) request,
              (io.grpc.stub.StreamObserver<Contract.GetImageInfo>) responseObserver);
          break;
        case METHODID_GET_MAP:
          serviceImpl.getMap((Contract.ImageId) request,
              (io.grpc.stub.StreamObserver<Contract.GetImageMap>) responseObserver);
          break;
        case METHODID_GET_NAMES_FROM_TIMAGE:
          serviceImpl.getNamesFromTImage((Contract.GetImageNamesWithTRequest) request,
              (io.grpc.stub.StreamObserver<Contract.GetImageNamesWithTResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SUBMIT_IMAGE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.submitImage(
              (io.grpc.stub.StreamObserver<Contract.SubmitImagesReply>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ContractBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ContractBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return Contract.ContractOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Contract");
    }
  }

  private static final class ContractFileDescriptorSupplier
      extends ContractBaseDescriptorSupplier {
    ContractFileDescriptorSupplier() {}
  }

  private static final class ContractMethodDescriptorSupplier
      extends ContractBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ContractMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ContractGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ContractFileDescriptorSupplier())
              .addMethod(getGetIpsMethod())
              .addMethod(getIsIpAliveMethod())
              .addMethod(getSubmitImageMethod())
              .addMethod(getGetLandmarkInfoMethod())
              .addMethod(getGetMapMethod())
              .addMethod(getGetNamesFromTImageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
