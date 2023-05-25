package org.example;

import Contract.*;
import Contract.Void;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.example.Utils.getRunningVMsIps;

public class Main {

    private static final int SERVER_PORT = 7001;

    private static ManagedChannel channel;

    private static ContractGrpc.ContractStub stub;
    public static void main(String[] args) {
        try {
            connectToServer();
            //connected to instance ready to start
            printMenu();
            //Temporary
            BufferedImage image = ImageIO.read(new File("/Users/joaomendes/Desktop/CN/G10-CN2223/CN2223TF/FotoNasa.jpg"));
            byte[] imageBytes = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
            Image i = Image.newBuilder().setImage(ByteString.copyFrom(imageBytes)).build();

            ReplyObserver<ImageId> reply = new ReplyObserver<>();
            stub.submitImage(i, reply);
            while(!reply.isCompleted()){
                System.out.println("waiting for server answer to complete...");
                Thread.sleep(200);
            }
            System.out.println(reply.getReplies().get(0));



        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets running instances available and picks a random ip to connect to.
     */
    public static void connectToServer() throws MalformedURLException {
        String[] ips = getRunningVMsIps();
        IpReply alive;
        do {
            if(ips.length == 0){
                System.out.println("Couldn't find any instances running...");
                System.exit(1);
            }
            int idx = (int)(Math.random()*ips.length);
            String pick = ips[idx];
            System.out.println("Connecting to " + pick);
            channel = ManagedChannelBuilder.forAddress(pick, SERVER_PORT)
                    .usePlaintext().build();
            stub = ContractGrpc.newStub(channel);
            //only to get status on the instance.
            var blockingStub = ContractGrpc.newBlockingStub(channel);
            alive = blockingStub.isIpAlive(Void.newBuilder().build());
            if(!alive.getAlive()) System.out.println("Failed to connect, trying again...");
            ips = getRunningVMsIps();
        }while (!alive.getAlive());
    }

    public static void printMenu(){
        //TODO
    }
}