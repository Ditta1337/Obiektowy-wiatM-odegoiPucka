package MlodyPucekIndustries.model.utils;

import java.util.LinkedList;

public class PerlinNoise2D {
    private final int mapWidth;
    private final int mapHeight;
    private final int[] p = getPermutation(512);

    public PerlinNoise2D(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    private int[] getPermutation(int n) {
        int[] permutation = new int[n];
        for (int i = 0; i < n / 2; i++) {
            permutation[i] = i;
            permutation[i + n / 2] = i;
        }
        for (int i = 0; i < n; i++) {
            int j = (int) (Math.random() * (n - i)) + i;
            int temp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = temp;
        }
        return permutation;
    }

    public double[][] getWaterLevels(double grassEdge, double deepEdge){
        double[][] waterLevels = new double[mapHeight][mapWidth];
        for(int i = 0; i < mapWidth; i++){
            for(int j = 0; j < mapHeight; j++){
                double di = (double) i / mapWidth;
                double dj = (double) j / mapHeight;
                double noise = this.noise(di * 4.37, dj * 5.47);
                noise = (noise + 1) / 2;
                waterLevels[j][i] = noise;
            }
        }

        getCorrectMap(waterLevels, grassEdge, deepEdge);

        return waterLevels;
    }

    public double noise(double x, double y){
        int xi = (int) Math.floor(x) & 255;
        int yi = (int) Math.floor(y) & 255;
        int g1 = p[p[xi] + yi];
        int g2 = p[p[xi + 1] + yi];
        int g3 = p[p[xi] + yi + 1];
        int g4 = p[p[xi + 1] + yi + 1];

        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        double d1 = grad(g1, xf, yf);
        double d2 = grad(g2, xf - 1, yf);
        double d3 = grad(g3, xf, yf - 1);
        double d4 = grad(g4, xf - 1, yf - 1);

        double u = fade(xf);
        double v = fade(yf);

        double x1Inter = lerp(u, d1, d2);
        double x2Inter = lerp(u, d3, d4);

        return lerp(v, x1Inter, x2Inter);

    }

    private static double lerp(double amount, double left, double right) {
        return ((1 - amount) * left + amount * right);
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double grad(int hash, double x, double y){
        return switch (hash & 3) {
            case 0 -> x + y;
            case 1 -> -x + y;
            case 2 -> x - y;
            case 3 -> -x - y;
            default -> 0;
        };
    }

//    private static int[] getPermutation() {
//        int[] permutation = new int[1024];
//        for(int i = 0; i < 1024; i++){
//            permutation[i] = i;
//        }
//        for(int i = 0; i < 256; i++){
//            int randomIndex = (int) (Math.random() * 256);
//            int temp = permutation[i];
//            permutation[i] = permutation[randomIndex];
//            permutation[randomIndex] = temp;
//        }
//        return permutation;
//    }

    public void getCorrectMap(double[][] map, double grassEdge, double deepEdge) {
        boolean[][] toSkip = new boolean[mapHeight][mapWidth];


        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (!toSkip[y][x] && map[y][x] >= grassEdge) {
                    boolean[][] localLake = BFS(map, toSkip, x, y, grassEdge, deepEdge );
                    if (!lakeValidator(localLake, map, y, deepEdge)) {
                        removeShallowLake(localLake, map, y);
                    }
                }
            }
        }
    }

    private void removeShallowLake(boolean[][] localLake, double[][] map, int startY) {
        for (int y = startY; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++){
                if (localLake[y][x]) {
                    map[y][x] = 0;
                }
            }
        }
    }

    private boolean lakeValidator(boolean[][] localLake, double[][] map, int startY, double deepEdge) {
        for (int y = startY; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (localLake[y][x] && map[y][x] >= deepEdge) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean[][] BFS(double[][] map, boolean[][] toSkip, int startX, int startY, double grassEdge, double deepEdge) {
        boolean[][] visited = new boolean[mapHeight][mapWidth];
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        LinkedList<int[]> queue = new LinkedList<>();

        queue.add(new int[]{startX, startY});
        visited[startY][startX] = true;
        toSkip[startY][startX] = true;

        while (!queue.isEmpty()) {
            int[] element = queue.get(0);
            if (map[element[1]][element[0]] >= deepEdge) {
                map[element[1]][element[0]] = 1.0;
            }
            queue.remove(0);
            for (int[] direction : directions) {
                int new_x = element[0] + direction[0];
                int new_y = element[1] + direction[1];

                if (new_y < mapHeight && new_x < mapWidth && new_y >= 0 && new_x >= 0 && map[new_y][new_x] >= grassEdge && !visited[new_y][new_x]) {

                    visited[new_y][new_x] = true;
                    toSkip[new_y][new_x] = true;
                    int[] new_element = {new_x, new_y};
                    queue.add(new_element);
                }
            }
        }
        return visited;
    }
}
