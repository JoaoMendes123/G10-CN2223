import com.google.cloud.WriteChannel;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.storage.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class StorageCalls {
    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private final String bucketName;
    private final Storage storage;

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

    public String uploadBlobToBucket(byte[] content, String imageName, String type) throws Exception {
        String generatedID = Integer.toString((int)(Math.random()*100));
        String[] aux = imageName.split("\\.");
        String blobName= aux[0] + generatedID + "." +aux[1];
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(type).build();

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
            }
        } else {
            // create the blob in one request.
            storage.create(blobInfo, content);
        }
        return blobName;
    }

    public void changePermissionOnBlobToPublic(String bucketName, String blobName) {

        BlobId blobId = BlobId.of(bucketName, blobName);
        Blob blob = storage.get(blobId);
        //set blob to public
        Acl.Entity entity = Acl.User.ofAllUsers();
        Acl.Role role = Acl.Role.READER;
        Acl acl = Acl.newBuilder(entity, role).build();
        blob.createAcl(acl);
        logger.info("Blob " + blobName + " is now public");
    }

}
