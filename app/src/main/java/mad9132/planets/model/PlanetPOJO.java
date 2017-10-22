package mad9132.planets.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO class for a Planet JSON object.
 *
 * Before You Begin - install the Parceable plugin for Android Studio (1)
 *
 * Copy 'n paste the JSON structure for a Planet: https://planets.mybluemix.net/planets/0
 *
 * Next, use this online tool to generate this POJO class: http://www.jsonschema2pojo.org/
 * Apply these settings:
 *   Package: mad9132.planets.model
 *   Class name: PlanetPOJO
 *   Target language: Java
 *   Source type: JSON
 *   Annotation style: None
 *   Check:
 *      Use primitive types
 *      Use double numbers
 *      Include getters and setters
 *
 * Finally, implement the Parceable interface (click 'PlanetPOJO' > Code > Generate... > Parceable
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 *
 * Reference
 * 1) "Model response data with POJO classes", Chapter 3. Requesting Data over the Web, Android
 *     App Development: RESTful Web Services by David Gassner
 */
public class PlanetPOJO implements Parcelable {

    private int planetId;
    private String name;
    private String overview;
    private String image;
    private String description;
    private double distanceFromSun;
    private int numberOfMoons;

    public int getPlanetId() {
        return planetId;
    }

    public void setPlanetId(int planetId) {
        this.planetId = planetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDistanceFromSun() {
        return distanceFromSun;
    }

    public void setDistanceFromSun(double distanceFromSun) {
        this.distanceFromSun = distanceFromSun;
    }

    public int getNumberOfMoons() {
        return numberOfMoons;
    }

    public void setNumberOfMoons(int numberOfMoons) {
        this.numberOfMoons = numberOfMoons;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.planetId);
        dest.writeString(this.name);
        dest.writeString(this.overview);
        dest.writeString(this.image);
        dest.writeString(this.description);
        dest.writeDouble(this.distanceFromSun);
        dest.writeInt(this.numberOfMoons);
    }

    public PlanetPOJO() {
    }

    protected PlanetPOJO(Parcel in) {
        this.planetId = in.readInt();
        this.name = in.readString();
        this.overview = in.readString();
        this.image = in.readString();
        this.description = in.readString();
        this.distanceFromSun = in.readDouble();
        this.numberOfMoons = in.readInt();
    }

    public static final Creator<PlanetPOJO> CREATOR = new Creator<PlanetPOJO>() {
        @Override
        public PlanetPOJO createFromParcel(Parcel source) {
            return new PlanetPOJO(source);
        }

        @Override
        public PlanetPOJO[] newArray(int size) {
            return new PlanetPOJO[size];
        }
    };
}