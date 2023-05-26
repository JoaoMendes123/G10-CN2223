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
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.example.Utils.getRunningVMsIps;

public class Main {
    private static final int SERVER_PORT = 7001;

    private static ManagedChannel channel;
    private static Logger logger = Logger.getLogger(Main.class.getName());
    private static ContractGrpc.ContractStub stub;
    private static boolean isConnected = false;
    public static void main(String[] args) {
        while(true){
                if(!isConnected)
                    connectToServer();
                else{
                    printMenu();
                    Scanner in = new Scanner(System.in);
                    var pick = in.nextInt();
                    switch (pick){
                        case 0 : submitImage();break;
                        case 99 : shutdown();break;
                    }
                }
        }
    }

    /**
     * Gets running instances available and picks a random ip to connect to.
     */
    public static void connectToServer() {
        String[] ips;
        try {
            ips = getRunningVMsIps();
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
                if(alive.getAlive()) {
                    isConnected = true;
                    return;
                }
                //updates available ips
                System.out.println("Failed to connect, trying again...");
                channel.shutdown();
                channel.awaitTermination(100, TimeUnit.MILLISECONDS);
                ips = getRunningVMsIps();
            }while (!alive.getAlive());
        } catch (MalformedURLException | InterruptedException | io.grpc.StatusRuntimeException e) {
            logger.warning("Check if server is on...");
            throw new RuntimeException(e);
        }

    }
    public static void printMenu(){
        System.out.println("### Menu ###");
        System.out.println("0 - submitImage()");
        System.out.println("99 - exit()");
        //TODO
    }

    private static void shutdown(){
        try {
            channel.shutdown();
            while(!channel.isShutdown()) {
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            logger.warning(e.getMessage());

        }
        System.exit(1);
    }

    //TODO
    public static void submitImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("/Users/joaomendes/Desktop/CN/G10-CN2223/CN2223TF/FotoNasa.jpg"));
            byte[] imageBytes = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
            Image i = Image.newBuilder().setImage(ByteString.copyFrom(imageBytes)).build();
            ReplyObserver<ImageId> reply = new ReplyObserver<>();
            stub.submitImage(i, reply);
            while(!reply.isCompleted()){
                logger.warning("waiting for server answer to complete...");
                Thread.sleep(200);
            }
            System.out.println(reply.getReplies().get(0));
        } catch (IOException | InterruptedException e) {
            logger.warning(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}