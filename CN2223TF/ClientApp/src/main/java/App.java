import Contract.*;
import Contract.Image;
import Contract.Void;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class App {

    private static Path DOWNLOAD_PATH = Path.of("./maps/");
    private static final int SERVER_PORT = 7001;

    private static ManagedChannel channel;
    private static Logger logger = Logger.getLogger(App.class.getName());
    private static ContractGrpc.ContractStub stub;

    private static Boolean RUN_LOCAL = false;
    private static Boolean PICK_RANDOM_IP = true;
    private static boolean isConnected = false;
    public static void main(String[] args) throws IOException {
        if(!Files.exists(DOWNLOAD_PATH))
            Files.createDirectories(DOWNLOAD_PATH);
        Scanner in = new Scanner(System.in);
        System.out.println("Connect to localhost? [y/n]");
        if(in.hasNextLine()){
            if(in.nextLine().equalsIgnoreCase("y")) RUN_LOCAL=true;
        }
        if(!RUN_LOCAL){
            System.out.println("Select a specific server?[y/n]");
            if(in.hasNextLine() && in.nextLine().equalsIgnoreCase("y")) PICK_RANDOM_IP= false;
        }
        while(!isConnected){
            connectToServer();
        }

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
                String pick = "localhost";
                if(!RUN_LOCAL){
                    if(ips.length == 0){
                        System.out.println("Couldn't find any instances running...");
                        System.exit(1);
                    }
                    int idx =  (int)(Math.random()*ips.length);
                    if(!PICK_RANDOM_IP){
                        System.out.println("Server Available:");
                        for (int i = 0; i < ips.length; i++){
                            System.out.format("\t%d - %s\n", i, ips[i]);
                        }
                        System.out.println("Select Server: [0..N]");
                        Scanner in = new Scanner(System.in);
                        if(in.hasNextInt()) {
                            idx = in.nextInt();
                        }
                    }
                    pick = ips[idx];
                }
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
            System.out.println("99 - exit");
            var pick = in.nextInt();
            switch (pick) {
                case 0:
                    submitImage(in);
                    break;
                case 1:
                    getLandmarks(in);
                    break;
                case 2:
                    getLandmarksWithT(in);
                    break;
                case 3:
                    getMap(in);
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

    public static void submitImage(Scanner in) {
        try {
            System.out.println("Provide absolute path to image :");
            while(in.hasNext()) {
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
        ReplyObserver reply = new ReplyObserver<ImageId>();
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

    public static void getLandmarks(Scanner in){
        try {
            System.out.println("Please enter requestId:");
            if(in.hasNext()) {
                ImageId id = ImageId.newBuilder().setId(in.next()).build();
                ReplyObserver<LandmarkProtoResult> reply = new ReplyObserver<>();
                stub.getLandmarksFromRequest(id,reply);
                while(!reply.isCompleted()){
                    logger.warning("waiting for server answer to complete...");
                    Thread.sleep(200);
                }
                int idx = 0;
                for (LandmarkProtoResult res: reply.getReplies()) {
                    System.out.format("%d\n\tLandmark Name: %s\n\tCoordinates: \n\t\tLatitude =%,.10f\n\t\tLongitude =%,.10f\n\tscore:%,.10f\n",
                            idx++,
                            res.getName(),
                            res.getLatitude(),
                            res.getLongitude(),
                            res.getPercentage()
                            );
                }
            }
        } catch (InterruptedException e) {
            logger.warning(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public static void getLandmarksWithT(Scanner in){
        try {
            System.out.println("Minimum certainty value: ");
            if(in.hasNextDouble()) {
                ReplyObserver<LandmarkProtoResult> reply = new ReplyObserver<>();
                GetImageNamesWithTRequest t = GetImageNamesWithTRequest.newBuilder().setT(in.nextDouble()).build();
                stub.getNamesFromTImage(t, reply);
                while(!reply.isCompleted()){
                    logger.warning("waiting for server answer to complete...");
                    Thread.sleep(200);
                }

                for (LandmarkProtoResult res: reply.getReplies()) {
                    System.out.format("\n\tLandmark Name: %s\n\tCoordinates: \n\t\tLatitude =%,.10f\n\t\tLongitude =%,.10f\n\tscore:%,.10f\n",
                            res.getName(),
                            res.getLatitude(),
                            res.getLongitude(),
                            res.getPercentage()
                    );
                }
            }
        } catch (InterruptedException e) {
            logger.warning(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private static void getMap(Scanner in) {
        try{
            System.out.print("Please insert request id: ");

            if(in.hasNext()){
                String id = in.next();
                ReplyObserver reply = new ReplyObserver<LandmarkProtoResult>();
                ImageId reqId = ImageId.newBuilder().setId(id).build();
                stub.getLandmarksFromRequest(reqId, reply);
                System.out.print("Please insert the idx of the result:[0..N]");
                int idx;
                if(in.hasNextInt()){
                    idx = in.nextInt();
                }else{
                    System.out.println("Has to be a Number!");
                    return;
                }
                while (!reply.isCompleted()){
                    logger.warning("waiting for server answer to complete");
                    Thread.sleep(300);
                }
                if(reply.getReplies().size() <= idx){
                    System.out.println("Number of results: " + reply.getReplies().size());
                    System.out.println("Your input: " + idx);
                    return;
                }
                reply = new ReplyObserver<GetImageMap>();
                MapChoice map = MapChoice.newBuilder().setId(id).setChoice(idx).build();
                stub.getMap(map, reply);
                while(!reply.isCompleted()){
                    logger.warning("waiting for server answer to complete...");
                    Thread.sleep(200);
                }
                GetImageMap imageMap = (GetImageMap) reply.getReplies().get(0);
                byte[] buffer = imageMap.getMap().toByteArray();
                String name = "static-map-" + id + "-"+idx+".png";
                Path target = DOWNLOAD_PATH.resolve(name);
                if(!target.toFile().exists()){
                    Path test = Files.createFile(target);
                    try(FileOutputStream fout=new FileOutputStream(test.toFile());){
                        fout.write(buffer);
                        fout.flush();
                    }
                }
                System.out.println("Downloaded file to: " + DOWNLOAD_PATH + "/" + target.getFileName());
            }
        } catch (InterruptedException e) {
            logger.warning(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}