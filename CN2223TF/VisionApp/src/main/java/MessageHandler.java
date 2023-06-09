import DocumentObjects.LandmarkResult;
import DocumentObjects.LoggingDocument;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import com.google.type.LatLng;
import java.util.List;
import java.util.logging.Logger;

public class MessageHandler implements MessageReceiver {
    private final LandmarksDetector lDetector;
    private final Logger logger = Logger.getLogger(MessageHandler.class.getName());
    private final FirestoreCalls firestoreCalls;
    private final StorageCalls storageCalls;


    public MessageHandler(LandmarksDetector lDetector, FirestoreCalls firestoreCalls, StorageCalls storageCalls) {
        this.lDetector = lDetector;
        this.firestoreCalls = firestoreCalls;
        this.storageCalls = storageCalls;
    }

    @Override
    public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer ackReplyConsumer) {
        //Process message and log to Firestore.
        //Message structure: cn2223tf_bucket;images/FotoName.jpg;a454
        try {
            String msg = pubsubMessage.getData().toStringUtf8();
            String[] messageFields = msg.split(";");
            //pretend some work
            logger.info("Received following message: " + msg);

            List<LandmarkResult> results = lDetector.detectLandmark(messageFields[0], messageFields[1]);
            //save on firestore name and localizations of identified places on the image
            //get maps

            for (LandmarkResult r: results) {
                byte[] mapBytes = lDetector.getStaticMap(
                        LatLng.newBuilder()
                        .setLatitude(r.coordinates.latitude)
                        .setLongitude(r.coordinates.longitude)
                        .build()
                );
                //save maps in storage maps directory
                if(mapBytes.length > 0){
                   r.map_blob_name = storageCalls.uploadImageToBucket(mapBytes, r.name, messageFields[2]);
                }
            }
            LoggingDocument doc = new LoggingDocument(messageFields[2], messageFields[0], messageFields[1],results);
            firestoreCalls.insertDocument(doc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            ackReplyConsumer.ack();
        }
    }
}
