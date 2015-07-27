//tested and works
public class PointXY {

    protected double x;
    protected double y;
    protected double yaw;
    protected double distance;


    public PointXY (String raw) {
        String[] tokens;
        tokens = raw.split(" ");
        yaw = Double.parseDouble(tokens[0]);
        distance = Double.parseDouble(tokens[1]);
        calculatePoint();
    }

    protected void calculatePoint(){
        x = Math.cos(Math.toRadians(yaw)) * distance;
        y = Math.sin(Math.toRadians(yaw)) * distance;
    }

    public int getX(){
        int x1 = (int) Math.round(x);
        return x1;
    }

    public int getY(){
        int y1 = (int) Math.round(y);
        return y1;
    }
}