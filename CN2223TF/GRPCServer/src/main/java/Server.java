import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.pubsub.v1.TopicName;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.Scanner;

public class Server {
    public static final int SERVER_PORT = 7001;

    public static final String BUCKET_NAME = "cn2223tf_bucket";

    final static String TOPIC_ID = "cn2223tf-topic";
    final static String PROJECT_ID = "cn2223-t1-g10";
    final static String COLLECTION_NAME = "cn2223tf-collection";

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
            //add firestore
            FirestoreOptions fOptions = FirestoreOptions.newBuilder().setCredentials(credentials).build();
            Firestore db = fOptions.getService();
            CollectionReference colRef = db.collection(COLLECTION_NAME);
            FirestoreCalls fireStore = new FirestoreCalls(colRef);

            svc = ServerBuilder.forPort(SERVER_PORT)
                    .addService(new ContractMockImplementation(storCalls, pubSubCalls, fireStore))
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
