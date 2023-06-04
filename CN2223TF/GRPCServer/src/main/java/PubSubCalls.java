import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class PubSubCalls {
    private final String projectID;
    private final String topicName;

    private final Logger logger = Logger.getLogger(PubSubCalls.class.getName());

    public PubSubCalls(String topicName, String projectID){
        this.topicName = topicName;
        this.projectID = projectID;
    }

    public void sendMessage(String blobId, String bucketName, String requestId) {
        try {
            String message = bucketName + ";" +
                    blobId + ";" +
                    requestId;
            TopicName tName = TopicName.ofProjectTopicName(projectID, topicName);
            Publisher publisher = Publisher.newBuilder(tName).build();
            ByteString msgData = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(msgData)
                    .build();
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            String msgID = future.get();
            logger.info("Message published on topic " + topicName + " with id: " + msgID);
            publisher.shutdown();

        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
