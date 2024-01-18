package MlodyPucekIndustries.model.maps;
import MlodyPucekIndustries.model.utils.Vector2D;

public interface MoveValidator {
    boolean canMoveTo(Vector2D position);

    Vector2D validPosition(Vector2D position);
}
