public class Point3D {

    private double yaw;
    private double distance;
    private double pitch;
    private double convYaw;
    private double convDistance;
    private double convPitch;

    public Point3D(String raw) {
        String[] tokens;
        tokens = raw.split(" ");
        yaw = Double.parseDouble(tokens[0]);
        distance = Double.parseDouble(tokens[1]);
        pitch = Double.parseDouble(tokens[2]);
    }

    public void calculatePoint(int minDegree, int maxDegree, int width, int height) {
        //might have to be moved to LidarView to get a rotating view
        convYaw = (yaw - minDegree)/((maxDegree - minDegree)/2) * width/2;
        convDistance = Math.cos(Math.toRadians(pitch)) * distance;
        convPitch = (-(pitch/30) + 1) * height/2;
    }

    public int getYaw(){
        int y = (int) Math.round(convYaw);
        return y;
    }

    public int getDistance(){
        int d = (int) Math.round(convDistance);
        return d;
    }

    public int getPitch(){
        int p = (int) Math.round(convPitch);
        return p;
    }

    public boolean inRange(double minDegree, double maxDegree) {
        if(yaw >= minDegree && yaw <= maxDegree) {
            return true;
        }
        return false;
    }
}