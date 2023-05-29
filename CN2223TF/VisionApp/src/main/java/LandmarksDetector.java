import FirestoreDocumentObjects.LandmarkResult;
import com.google.cloud.vision.v1.*;
import com.google.type.LatLng;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//TODO
public class LandmarksDetector {
    final static int ZOOM = 15; // Streets
    final static String SIZE = "600x300";

    // Considera-se que o nomes de imagens correspondem aos nomes de BLOB
    // existentes num bucket de nome BUCKET_NAME no Storage do Projeto

    private final String API_KEY;

    private final Logger logger = Logger.getLogger(LandmarksDetector.class.getName());

    public LandmarksDetector(String apiKey){
        this.API_KEY = apiKey;
    }

    public List<LandmarkResult> detectLandmark(String bucketName, String blobName){
        try {
            String blobGsPath = "gs://"+bucketName+"/" + blobName;
            return detectLandmarksGcs(blobGsPath);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Detects landmarks in the specified remote image on Google Cloud Storage.

    private List<LandmarkResult> detectLandmarksGcs(String blobGsPath) throws IOException {
        logger.info("Detecting landmarks for: " + blobGsPath);
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(blobGsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LANDMARK_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    logger.warning("Error: " + res.getError().getMessage());
                    return null;
                }

                logger.info("Landmarks list size: " + res.getLandmarkAnnotationsList().size());
                ArrayList<LandmarkResult> results = new ArrayList<>(res.getLandmarkAnnotationsList().size());
                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getLandmarkAnnotationsList()) {
                    LocationInfo info = annotation.getLocationsList().listIterator().next();
                    LandmarkResult lResult = new LandmarkResult(
                            annotation.getDescription(),
                            info.getLatLng(),
                            annotation.getScore()
                    );
                    results.add(lResult);
                    logger.info(
                            "Landmark name:" + lResult.name
                                    + "\nLandmark coordinates: " + lResult.coordinates
                                    + "Landmark score: " + lResult.score + "\n"
                    );
                }

                return results;
            }
        }
        return new ArrayList<LandmarkResult>();
    }

    public byte[] getStaticMap(LatLng latLng) {
        String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?"
                + "center=" + latLng.getLatitude() + "," + latLng.getLongitude()
                + "&zoom=" + ZOOM
                + "&size=" + SIZE
                + "&key=" + API_KEY;
        logger.info(mapUrl);
        try {
            URL url = new URL(mapUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = conn.getInputStream();
            byte[] buffer = in.readAllBytes();
            in.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }


}
