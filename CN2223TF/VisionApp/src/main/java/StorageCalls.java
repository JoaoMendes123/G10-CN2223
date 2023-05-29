import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

public class StorageCalls {
    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private final String bucketName;
    private final Storage storage;
    private final String MAPS_DIRECTORY = "maps/";

    public StorageCalls(Storage storage, String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
        if (storage.get(bucketName) == null) {
            createBucket(bucketName);
        }
    }

    private void createBucket(String bucketName) {
        storage.create(
                BucketInfo.newBuilder(bucketName)
                        // See here for possible values: http://g.co/cloud/storage/docs/storage-classes
                        .setStorageClass(StorageClass.STANDARD)
                        // Possible values: http://g.co/cloud/storage/docs/bucket-locations#location-mr
                        .setLocation("europe-west1")
                        .build());
        logger.info("created new bucket " + bucketName);
    }

    public String uploadImageToBucket(byte[] content, String resultName,String requestID) throws Exception {
        String blobName = MAPS_DIRECTORY + "static_map_" +
                resultName.replaceAll(" ", "").concat("_") +
                requestID + ".png";//ex: maps/static_map_locationname_1234.jpg
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("png").build();

        InputStream image = new ByteArrayInputStream(content);
        if (content.length > 1_000_000) {
            // When content is not available or large (1MB or more) it is recommended
            // to write it in chunks via the blob's channel writer.
            try (WriteChannel writer = storage.writer(blobInfo)) {
                byte[] buffer = new byte[1024];
                try (InputStream input = image) {
                    int limit;
                    while ((limit = input.read(buffer)) >= 0) {
                        try {
                            writer.write(ByteBuffer.wrap(buffer, 0, limit));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                return blobName;
            }
        } else {
            // create the blob in one request.
            storage.create(blobInfo, content);
            return blobName;
        }
    }
}
