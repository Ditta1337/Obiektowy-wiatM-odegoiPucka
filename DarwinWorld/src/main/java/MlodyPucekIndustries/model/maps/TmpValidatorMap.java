package MlodyPucekIndustries.model.maps;

import MlodyPucekIndustries.model.utils.Vector2d;


public class TmpValidatorMap implements MoveValidator {
    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }
}
