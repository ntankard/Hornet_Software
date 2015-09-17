package hornet.lidar;

/**
 * Created by Angela on 21/08/2015.
 */
public class XYPoint {

    private double _xP;  // Point x percentage
    private double _yP;  // Point y percentage

    public XYPoint(SweepPoint p, float max, float offset) {

        double x = p.get_distance() * Math.cos((Math.toRadians((double) p.get_angle() + 90)));
        double y = p.get_distance() * Math.sin((Math.toRadians((double) p.get_angle() + 90)));

        _xP = ((x/(max*2)) * 100)+50;
        _yP = ((-y/(max*2)) * 100)+50;
    }

    public double get_xP() {
        return _xP;
    }

    public double get_yP() {
        return _yP;
    }
}
