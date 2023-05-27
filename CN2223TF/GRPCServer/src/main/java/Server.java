import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Server {
    public static final int SERVER_PORT = 7001;

    public static final String BUCKET_NAME = "cn2223tf_bucket";
    public static io.grpc.Server svc;

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
            StorageOptions sOption = StorageOptions.getDefaultInstance();
            String projectId = sOption.getProjectId();
            Storage storage = sOption.getService();
            StorageCalls storCalls = new StorageCalls(storage, BUCKET_NAME);
            //add pub/sub

            svc = ServerBuilder.forPort(SERVER_PORT)
                    .addService(new ContractMockImplementation(storCalls))
                    .build();
            svc.start();
            System.out.println("Server is listening on port "+ SERVER_PORT);
            Scanner in = new Scanner(System.in);
            do{
                svc.awaitTermination();
            }while(!in.hasNextInt());
            svc.shutdown();
        }catch(Exception e){
            throw e;
        }
    }

}
