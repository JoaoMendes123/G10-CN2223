import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.Scanner;

public class Server {
    public static final int SERVER_PORT = 7001;

    public static final String BUCKET_NAME = "cn2223tf_bucket";

    final static String TOPIC_ID = "cn2223tf-topic";
    final static String PROJECT_ID = "cn2223-t1-g10";

    public static io.grpc.Server svc;

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();

            StorageOptions sOption = StorageOptions.getDefaultInstance();
            //add storage
            Storage storage = sOption.getService();
            StorageCalls storCalls = new StorageCalls(storage, BUCKET_NAME);
            //add pub/sub
            PubSubCalls pubSubCalls = new PubSubCalls(TOPIC_ID, PROJECT_ID);
            //create publisher for requests

            svc = ServerBuilder.forPort(SERVER_PORT)
                    .addService(new ContractMockImplementation(storCalls, pubSubCalls))
                    .build();
            svc.start();
            System.out.println("Server is listening on port "+ SERVER_PORT);
            Scanner in = new Scanner(System.in);
            do{
                svc.awaitTermination();
            }while(!in.hasNextInt());
        }catch(Exception e){
            throw e;
        }
    }
}
