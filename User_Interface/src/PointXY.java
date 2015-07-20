//tested and works
public class PointXY {

    private double x;
    private double y;
    private double angle;
    private double distance;


    public PointXY (String raw) {
        String[] tokens = raw.split(" ");
        angle = Double.parseDouble(tokens[0]);
        distance = Double.parseDouble(tokens[1]);
        calculatePoint();
    }

    private void calculatePoint(){
        x = Math.cos(Math.toRadians(angle)) * distance;
        y = Math.sin(Math.toRadians(angle)) * distance;
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