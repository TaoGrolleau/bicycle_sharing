public class StationModel {
    public String stop_id;
    public String stop_name;
    public String stop_lat;
    public String stop_lon;

    protected StationModel(){
    }

    protected String getStopID(){
        return this.stop_id;
    }

    protected String getStopName(){
        return this.stop_name;
    }

    protected String getStopLat(){
        return this.stop_lat;
    }

    protected String getStopLon(){
        return this.stop_lon;
    }

    protected void setStopID(String stop_id){
        this.stop_id = stop_id;
    }

    protected void setStopName(String stop_name){
        this.stop_name = stop_name;
    }

    protected void setStopLat(String stop_lat){
        this.stop_lat = stop_lat;
    }

    protected void setStopLon(String stop_lon){
        this.stop_lon = stop_lon;
    }

    protected void printSM(){
        System.out.println(this.stop_id + " " + this.stop_name + " " + this.stop_lat + " " + this.stop_lon);
    }
}
