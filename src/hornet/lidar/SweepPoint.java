package hornet.lidar;

/**
 * Created by Angela on 21/08/2015.
 */
public class SweepPoint {

    private float _angle;
    private  float _distance;

    public SweepPoint(float _angle, float _distance) {
        this._angle = _angle;
        this._distance = _distance;
    }

    public float get_angle() {
        return _angle;
    }

    public float get_distance() {
        return _distance;
    }


}
