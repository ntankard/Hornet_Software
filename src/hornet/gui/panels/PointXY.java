package hornet.gui.panels;

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
    }

    protected void calculatePoint(int width, int height){
        int fieldOfViewDistance = 1500; //this will be what we deem to be the range that we can see/would like to view
        x = (((Math.cos(Math.toRadians(yaw)) * distance)+(width))/fieldOfViewDistance)*width;
        y = (((Math.sin(Math.toRadians(yaw)) * distance)+(height))/fieldOfViewDistance)*height;
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