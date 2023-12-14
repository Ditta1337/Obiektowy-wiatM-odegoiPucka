package MlodyPucekIndustries.model.elements;

import MlodyPucekIndustries.model.utils.Vector2d;

public interface WorldElement {
   public Vector2d getPosition();

   public boolean isAt(Vector2d position);

    public void setPosition(Vector2d position);
}
