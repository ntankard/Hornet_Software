package hornet.lidar;

/**
 * Created by Angela on 19/08/2015.
 */
public class Point {

    private Float _angle;
    private Float _distance;
    private Float _rotation;

    public Point(Float angle, Float distance, Float rotation)
    {
        _angle = angle;
        _distance = distance;
        _rotation = rotation;
    }

    public Float get_distance() {
        return _distance;
    }

    public Float get_angle() {
        return _angle;
    }

    public Float get_rotation() {
        return _rotation;
    }

}
