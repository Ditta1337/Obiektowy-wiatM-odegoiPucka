package MlodyPucekIndustries.model.elements;

import MlodyPucekIndustries.model.utils.Vector2D;

public interface WorldElement {
    Vector2D getPosition();
    boolean isAt(Vector2D position);
}
