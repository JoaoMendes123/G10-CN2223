import Contract.*;
import Contract.Image;
import Contract.Void;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class Main {
    private static final int SERVER_PORT = 7001;

    private static boolean RUN_LOCAL = false;
    private static ManagedChannel channel;
    private static Logger logger = Logger.getLogger(Main.class.getName());
    private static ContractGrpc.ContractStub stub;
    private static boolean isConnected = false;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Connect to localhost?\nPress [Y] to confirm");
        if(in.hasNextLine()){
            if(in.nextLine().equalsIgnoreCase("Y")) RUN_LOCAL = true;
        }
        connectToServer();
        printMenu(in);
    }


    /**
     * Gets running instances available and picks a random ip to connect to.
     */
    public static void connectToServer() {
        String[] ips;
        try {
            ips = Utils.getRunningVMsIps();
            IpReply alive;
            do {
                if(!RUN_LOCAL){
                    if(ips.length == 0){
                        System.out.println("Couldn't find any instances running...");
                        System.exit(1);
                    }
                }
                int idx = (int)(Math.random()*ips.length);
                String pick = RUN_LOCAL ? "localhost" : ips[idx];
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
                ips = Utils.getRunningVMsIps();
            }while (!alive.getAlive());
        } catch (MalformedURLException | InterruptedException | io.grpc.StatusRuntimeException e) {
            logger.warning("Check if server is on...");
            throw new RuntimeException(e);
        }

    }
    public static void printMenu(Scanner in) {
        while(true){
            System.out.println("### Menu ###");
            System.out.println("0 - submitImage()");
            System.out.println("1 - getLandMarks()");
            System.out.println("2 - getLandMarksWithT()");
            System.out.println("3 - getMap()");
            var pick = in.nextInt();
            switch (pick) {
                case 0:
                    submitImage();
                    break;
                case 1:
                    getLandmarks();
                    break;
                case 2:
                    getLandmarksWithT();
                    break;
                case 3:
                    getMap();
                    break;
                case 99:
                    shutdown();
                    return;
            }
        }
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
    }

    public static void submitImage() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Provide absolute path to image :");
            while(in.hasNextLine()) {
                Path p = Paths.get(in.nextLine());
                String type;
                if(Files.exists(p)){
                    type = Files.probeContentType(p);
                     if( type == null || !type.equals("image/jpeg") && !type.equals("image/png") ) {
                         System.out.println("File is not a recognized image type...");
                     }
                     else{
                         String name = p.getFileName().toString();
                         byte[] image = Files.readAllBytes(p);
                         executeSubmitImageRequest(image, name, type);
                         break;
                     }
                 }else {
                    System.out.println("Couldn't find file");
                }
                System.out.println("Provide absolute path to image :");
            }

        } catch (IOException | InterruptedException e) {
            logger.warning("User Input : " + e.getMessage());

        }

    }
    public static void executeSubmitImageRequest(byte[] image, String name, String type) throws InterruptedException {
        Image i = Image.newBuilder()
                .setImage(ByteString.copyFrom(image))
                .setName(name)
                .setType(type)
                .build();
        ReplyObserver<ImageId> reply = new ReplyObserver<>();
        stub.submitImage(i, reply);
        while(!reply.isCompleted()){
            logger.warning("waiting for server answer to complete...");
            Thread.sleep(200);
        }
        if(!reply.OnSuccess()){
            System.out.println("Something went wrong");
        }
        System.out.println(reply.getReplies().get(0));
    }

    public static void getLandmarks(){
        try {
            Scanner in = new Scanner(System.in);
            ReplyObserver<LandmarkProtoResult> reply = new ReplyObserver<>();
            while(!reply.isSuccess()) {
                System.out.println("Please enter requestId:");
                if(in.hasNext()){
                    ImageId id = ImageId.newBuilder().setId(in.next()).build();
                    stub.getLandmarksFromRequest(id,reply);
                    while(!reply.isCompleted()){
                        logger.warning("waiting for server answer to complete...");
                        Thread.sleep(200);
                    }
                }
                if(!reply.isSuccess()) reply = new ReplyObserver<>();
            }
            for (LandmarkProtoResult res: reply.getReplies()) {
                System.out.format("Landmark Name: %s\nCoordinates: \n\t\tLatitude =%,.10f\n\t\tLongitude =%,.10f\nscore:%,.10f\n",
                    res.getName(),
                    res.getLatitude(),
                    res.getLongitude(),
                    res.getPercentage()
                );
            }
        } catch (InterruptedException e) {
            logger.warning(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public static void getLandmarksWithT(){
        try {
            Scanner in = new Scanner(System.in);
            ReplyObserver<LandmarkProtoResult> reply = new ReplyObserver<>();
            while(!reply.isSuccess()) {
                System.out.println("Enter minimum score");
                if(in.hasNextDouble()){
                    GetImageNamesWithTRequest t = GetImageNamesWithTRequest.newBuilder().setT(in.nextDouble()).build();
                    stub.getNamesFromTImage(t, reply);
                    while(!reply.isCompleted()){
                        logger.warning("waiting for server answer to complete...");
                        Thread.sleep(200);
                    }
                }
                if(!reply.isSuccess()) reply = new ReplyObserver<>();
            }
            var results = reply.getReplies();
            if(results.isEmpty()){
                System.out.println("No documents found for that condition");
            }
            for (LandmarkProtoResult res: results) {
                System.out.format("Landmark Name: %s\nCoordinates:\n\t\tLatitude =%,.10f\n\t\tLongitude =%,.10f\nscore:%,.10f\n",
                    res.getName(),
                    res.getLatitude(),
                    res.getLongitude(),
                    res.getPercentage()
                );
            }
        } catch (InterruptedException e) {
            logger.warning(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private static void getMap() {
        try{
            System.out.print("Please insert the map's id: ");

            Scanner in = new Scanner(System.in);

            String id = in.nextLine();

            //TODO change this
            System.out.println("Please insert the idx of the result");

            int idx = Integer.parseInt(in.nextLine());

            ReplyObserver<GetImageMap> reply = new ReplyObserver<>();
            MapChoice map = MapChoice.newBuilder().setId(id).setChoice(idx).build();

            stub.getMap(map, reply);
            while(!reply.isCompleted()){
                logger.warning("waiting for server answer to complete...");
                Thread.sleep(200);

            }
            GetImageMap imageMap = reply.getReplies().get(0);

            byte[] buffer = imageMap.getMap().toByteArray();





        } catch (InterruptedException e) {
            logger.warning(e.getMessage());
        }
    }
}