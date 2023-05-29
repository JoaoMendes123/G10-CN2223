import FirestoreDocumentObjects.LandmarkResult;
import FirestoreDocumentObjects.LoggingDocument;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import com.google.type.LatLng;
import com.google.type.LatLngOrBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MessageMockHandler implements MessageReceiver {
    private final LandmarksDetector lDetector;
    private final Logger logger = Logger.getLogger(MessageMockHandler.class.getName());
    private final FirestoreCalls firestoreCalls;
    private final StorageCalls storageCalls;


    public MessageMockHandler(LandmarksDetector lDetector, FirestoreCalls firestoreCalls, StorageCalls storageCalls) {
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
            LoggingDocument doc = new LoggingDocument(messageFields[2], messageFields[0], messageFields[1], results);
            firestoreCalls.insertDocument(doc);
            //get maps
            for (LandmarkResult r: results) {
                byte[] mapBytes = lDetector.getStaticMap(
                        LatLng.newBuilder()
                        .setLatitude(r.coordinates.latitude)
                        .setLongitude(r.coordinates.longitude)
                        .build()
                );
                //save maps in storage maps directory
                if(mapBytes.length > 0)
                    storageCalls.uploadImageToBucket(mapBytes, r.name, doc.requestId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ackReplyConsumer.ack();
    }

}
