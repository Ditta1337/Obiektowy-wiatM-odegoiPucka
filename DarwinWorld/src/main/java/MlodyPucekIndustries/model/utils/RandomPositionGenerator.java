package MlodyPucekIndustries.model.utils;

import MlodyPucekIndustries.model.maps.MoveValidator;

import java.util.*;


public class RandomPositionGenerator implements Iterable<Vector2D> {

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
    public Iterator<Vector2D> iterator() {
        List<Vector2D> positions = new ArrayList<>();

        for (int x = 0; x <= maxWidth; x++) {
            for (int y = 0; y <= maxHeight; y++) {
                if (validator.canMoveTo(new Vector2D(x, y))) {
                    positions.add(new Vector2D(x, y));
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
            public Vector2D next() {
                if (hasNext()) {
                    Vector2D position = positions.get(generatedCount);
                    generatedCount++;
                    return position;


                } else {
                    throw new IndexOutOfBoundsException("No more positions to generate");
                }
            }
        };
    }

    public static int[] permutationGenerator(int length){
        int[] permutation = new int[length];
        for(int i = 0; i < length; i++){
            permutation[i] = i;
        }
        for(int i = 0; i < length; i++){
            int randomIndex = (int) (Math.random() * length);
            int temp = permutation[i];
            permutation[i] = permutation[randomIndex];
            permutation[randomIndex] = temp;
        }
        return permutation;
    }
}
