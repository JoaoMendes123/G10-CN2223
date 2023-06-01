package FirestoreObjects;


import Contract.LandmarkProtoResult;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.type.LatLng;
import com.google.type.LatLngOrBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LandmarkResult {
    public String name;
    public Coordinates coordinates;
    public double score;

    public String map_blob_name;



    public LandmarkResult(String name, LatLng coordinates, double score){
        this.name = name;
        this.coordinates = new Coordinates(coordinates.getLatitude(), coordinates.getLongitude());
        this.score = score;
    }


    public static List<LandmarkResult> fromSnapshot(DocumentSnapshot doc){
        Map<String,Object> loggedDoc = doc.getData();
        ArrayList<Map<String, Object>> results = (ArrayList<Map<String, Object>>)loggedDoc.get("results");
        ArrayList<LandmarkResult> res = new ArrayList<>();
        for (Map<String, Object> idx: results) {
            double score = (double)(idx.get("score"));
            String name = (String)idx.get("name");
            Map<String, Object> coordinates = (Map<String, Object>)idx.get("coordinates");
            double latitude = (double)coordinates.get("latitude");
            double longitude = (double)coordinates.get("longitude");
            LatLng coord = LatLng.newBuilder().setLatitude(latitude).setLongitude(longitude).build();
            res.add(new LandmarkResult(name,coord,score));
        }
        return res;
    }

    public static LandmarkProtoResult toProtoObject(LandmarkResult landmarkResult){
        return LandmarkProtoResult.newBuilder()
                .setLatitude(landmarkResult.coordinates.latitude)
                .setLongitude(landmarkResult.coordinates.longitude)
                .setName(landmarkResult.name)
                .setPercentage(landmarkResult.score).build();
    }


}
