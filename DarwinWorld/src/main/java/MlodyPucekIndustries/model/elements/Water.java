package MlodyPucekIndustries.model.elements;

import MlodyPucekIndustries.model.utils.Vector2D;

public class Water implements WorldElement {
    private final Vector2D position;
    private final double depth;

    public Water(Vector2D position, double depth){
        this.position = position;
        this.depth = depth;
    }

    public double getDepth(){
        return depth;
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
        return "#";
    }
}
