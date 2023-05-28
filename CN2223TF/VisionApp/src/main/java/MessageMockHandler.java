import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;

import java.util.logging.Logger;

public class MessageMockHandler implements MessageReceiver {
    private final LandmarksDetector lDetector;
    private final Logger logger = Logger.getLogger(MessageMockHandler.class.getName());
    //TODO Will use LandMarksDetector
    //

    public MessageMockHandler(LandmarksDetector lDetector) {
        this.lDetector = lDetector;
    }

    @Override
    public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer ackReplyConsumer) {
        //Process message and log to Firestore.
        try {
            //pretend some work
            logger.info("Received following message: " + pubsubMessage.getData().toStringUtf8());
            //go to storage and get image
            //send it to landmarksDetector
            //save image in storage maps directory
            //save on firestore name and localizations of identified places on the image
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ackReplyConsumer.ack();
    }

}
