package FirestoreObjects;


import Contract.LandmarkProtoResult;
import com.google.type.LatLng;

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

    public LandmarkResult(){}

    public static LandmarkProtoResult toProtoObject(LandmarkResult landmarkResult){
        return LandmarkProtoResult.newBuilder()
                .setLatitude(landmarkResult.coordinates.latitude)
                .setLongitude(landmarkResult.coordinates.longitude)
                .setName(landmarkResult.name)
                .setPercentage(landmarkResult.score).build();
    }


}
