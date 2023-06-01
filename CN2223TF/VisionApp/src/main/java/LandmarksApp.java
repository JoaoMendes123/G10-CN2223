import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.pubsub.v1.*;

import java.io.IOException;
import java.util.Scanner;


public class LandmarksApp {

    final static String PROJECT_ID = "cn2223-t1-g10";
    final static String TOPIC_ID = "cn2223tf-topic";
    final static String SUBSCRIPTION_NAME = "cn2223tf-requests";
    final static String COLLECTION_NAME = "cn2223tf-collection";

    public static final String BUCKET_NAME = "cn2223tf_bucket";
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("API Key missing");
            System.out.println("Usage: java -jar LandmarkDetector.jar <API_KEY>");
            System.exit(1);
        }
        //Prepare Firestore
        Credentials cred = GoogleCredentials.getApplicationDefault();
        FirestoreOptions fOptions = FirestoreOptions.newBuilder().setCredentials(cred).build();
        Firestore db = fOptions.getService();
        CollectionReference colRef = db.collection(COLLECTION_NAME);
        //Landmark App creates a subscription to the topic and subscribes to it
        //If it already exists subscribes to it
        //Work-queue pattern
        TopicName topic = createTopic();
        SubscriptionName sub = createSubscriptionToTopic(topic);
        //Prepare Storage
        StorageOptions sOption = StorageOptions.getDefaultInstance();
        Storage storage = sOption.getService();

        MessageHandler handler = new MessageHandler(
                new LandmarksDetector(args[0]),
                new FirestoreCalls(colRef),
                new StorageCalls(storage,BUCKET_NAME)
        );
        Subscriber subscriber = createSubscriber(sub.getSubscription(), handler);
        subscriber.startAsync().awaitRunning();
        //I don't know if subscription is ever deleted, might be a problem.
        System.out.println("LandmarkApp is Running....");
        Scanner in = new Scanner(System.in);
        do{
            subscriber.awaitTerminated();
        }while(!in.hasNextInt());
    }


    /**
     * subscribes to provided subName with given handler
     * @param subName - name of the subscription
     * @param handler - handler to process new messages
     * @return - subscriber object
     */
    private static Subscriber createSubscriber(String subName, MessageHandler handler) {
        ProjectSubscriptionName subTo = ProjectSubscriptionName.of(PROJECT_ID, subName);

        ExecutorProvider executorProvider = InstantiatingExecutorProvider.newBuilder()
                .setExecutorThreadCount(1).build(); // um só thread no handler .build();
        return Subscriber.newBuilder(subTo, handler)
                .setExecutorProvider(executorProvider)
                .build();
    }
    /**
     * Creates a subscription with name : LandmarksApp
     * Or gets subscription if already exists
     * @param tName - TopicName to create subscription
     */
    private static SubscriptionName createSubscriptionToTopic(TopicName tName){
        SubscriptionName subName = SubscriptionName.of(PROJECT_ID,SUBSCRIPTION_NAME);
        try(SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            PushConfig pConf = PushConfig.getDefaultInstance();
             try {
                 subscriptionAdminClient.createSubscription(subName, tName, pConf, 0);
            }catch (com.google.api.gax.rpc.AlreadyExistsException ex){
                 subscriptionAdminClient.getSubscription(subName);
             }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return subName;
    }

    /**
     * Creates topic with given topic name or gets it if it already exists
     * @return - topic Name of created topic
     */
    private static TopicName createTopic(){
        TopicName topicName = TopicName.of(PROJECT_ID, TOPIC_ID);
        try(TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
            try {
                topicAdminClient.createTopic(topicName);
            }catch (com.google.api.gax.rpc.AlreadyExistsException ex){
                topicAdminClient.getTopic(topicName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return topicName;
    }
}