public class Point3D {

    private double convYaw;
    private double convDistance;
    private double convPitch;

    public Point3D(String raw) {
        String[] tokens;
        tokens = raw.split(" ");
        double yaw = Double.parseDouble(tokens[0]);
        double distance = Double.parseDouble(tokens[1]);
        double pitch = Double.parseDouble(tokens[2]);
        calculatePoint(yaw, distance, pitch);
    }

    public void calculatePoint(double yaw, double distance, double pitch) {

        convYaw = (yaw/45) * 400;
        convDistance = Math.cos(Math.toRadians(pitch)) * distance;
        convPitch = (-(pitch/30) + 1) * 400;
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
}