package MlodyPucekIndustries;

import MlodyPucekIndustries.model.maps.TmpValidatorMap;
import MlodyPucekIndustries.model.elements.Animal;

public class World {
    public static void main(String[] args) {
        Animal ziomek = new Animal();
        TmpValidatorMap map = new TmpValidatorMap();
        for (int i = 0; i < 8; i++) {
            ziomek.move(1, map);
            System.out.println(ziomek);
        }
    }
}
