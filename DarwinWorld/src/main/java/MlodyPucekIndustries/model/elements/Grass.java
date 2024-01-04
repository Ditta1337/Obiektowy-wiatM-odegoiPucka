package MlodyPucekIndustries.model.elements;

import MlodyPucekIndustries.model.utils.Vector2D;

public class Grass implements WorldElement{
    private final Vector2D position;

    public Grass(Vector2D position){
        this.position = position;
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public boolean isAt(Vector2D position) {
        return this.position.equals(position);
    }

    @Override
    public String toString() {
        return ".";
    }
}
