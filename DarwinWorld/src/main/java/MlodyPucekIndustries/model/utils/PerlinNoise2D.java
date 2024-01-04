package MlodyPucekIndustries.model.utils;

import java.util.LinkedList;

public class PerlinNoise2D {
    private final int mapWidth;
    private final int mapHeight;
    // private static final int[] p = getPermutation();

    public PerlinNoise2D(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    static final int[] p = new int[512], permutation = { 151,160,137,91,90,15,
            131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
            190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
            88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
            77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
            102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
            135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
            5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
            223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
            129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
            251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
            49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
            138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
    };
    static { for (int i=0; i < 256 ; i++) p[256+i] = p[i] = permutation[i]; }

    public double[][] getWaterLevels(double grassEdge, double deepEdge){
        double[][] waterLevels = new double[mapHeight][mapWidth];
        for(int i = 0; i < mapWidth; i++){
            for(int j = 0; j < mapHeight; j++){
                double di = (double) i / mapWidth;
                double dj = (double) j / mapHeight;
                double noise = PerlinNoise2D.noise(di * 4.37, dj * 5.47);
                noise = (noise + 1) / 2;
                waterLevels[j][i] = noise;
            }
        }

        getCorrectMap(waterLevels, grassEdge, deepEdge);

        return waterLevels;
    }

    public static double noise(double x, double y){
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
