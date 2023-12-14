package MlodyPucekIndustries.model.utils;

import MlodyPucekIndustries.model.maps.MoveValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class RandomPositionGenerator implements Iterable<Vector2d> {

    private final int maxWidth;
    private final int maxHeight;
    private final int grassCount;
    private final MoveValidator validator;
    private int generatedCount = 0;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount, MoveValidator validator) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.grassCount = grassCount;
        this.validator = validator;
    }

    @Override
    public Iterator<Vector2d> iterator() {
        List<Vector2d> positions = new ArrayList<>();

        for (int x = 0; x <= maxWidth; x++) {
            for (int y = 0; y <= maxHeight; y++) {
                if (validator.canMoveTo(new Vector2d(x, y))) {
                    positions.add(new Vector2d(x, y));
                }
            }
        }

        Collections.shuffle(positions);
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return generatedCount < grassCount;
            }

            @Override
            public Vector2d next() {
                if (hasNext()) {
                    generatedCount++;
                    return positions.get(generatedCount);

                } else {
                    throw new IndexOutOfBoundsException("No more positions to generate");
                }
            }
        };
    }
}
