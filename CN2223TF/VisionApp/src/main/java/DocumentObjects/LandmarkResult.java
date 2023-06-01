package DocumentObjects;


import com.google.type.LatLng;

public class LandmarkResult {
    public String name;

    public Coordinates coordinates;
    public float score;

    public String map_blob_name;


    public LandmarkResult(String name, LatLng coordinates, float score) {
        this.name = name;
        this.coordinates = new Coordinates(coordinates.getLatitude(), coordinates.getLongitude());
        this.score = score;
    }
}


