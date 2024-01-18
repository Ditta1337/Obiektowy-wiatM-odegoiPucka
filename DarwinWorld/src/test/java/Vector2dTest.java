import org.junit.jupiter.api.Test;
import MlodyPucekIndustries.model.utils.Vector2D;
import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {
    @Test
    public void testEquals() {
        Vector2D vector1 = new Vector2D(1, 1);
        Vector2D vector2 = new Vector2D(1, 0);
        Vector2D vector3 = new Vector2D(1, 1);
        boolean result1 = vector1.equals(vector2);
        boolean result2 = vector1.equals(vector3);

        assertFalse(result1);
        assertTrue(result2);
    }

    @Test
    public void testToString() {
        Vector2D vector = new Vector2D(3, 5);
        String expected = "(3,5)";
        String result = vector.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testPrecedes() {
        Vector2D vector1 = new Vector2D(2, 2);
        Vector2D vector2 = new Vector2D(1, 1);
        Vector2D vector3 = new Vector2D(3, 3);
        boolean result1 = vector1.precedes(vector2);
        boolean result2 = vector1.precedes(vector3);


        assertFalse(result1);
        assertTrue(result2);
    }

    @Test
    public void testFollows() {
        Vector2D vector1 = new Vector2D(2, 2);
        Vector2D vector2 = new Vector2D(1, 1);
        Vector2D vector3 = new Vector2D(3, 3);
        boolean result1 = vector1.follows(vector2);
        boolean result2 = vector1.follows(vector3);

        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testAdd() {
        Vector2D vector1 = new Vector2D(2, 2);
        Vector2D vector2 = new Vector2D(1, 1);
        Vector2D expected = new Vector2D(3, 3);
        Vector2D result = vector1.add(vector2);

        assertEquals(expected, result);
    }

    @Test
    public void testSubtract() {
        Vector2D vector1 = new Vector2D(2, 2);
        Vector2D vector2 = new Vector2D(2, 2);
        Vector2D expected = new Vector2D(0, 0);
        Vector2D result = vector1.subtract(vector2);

        assertEquals(expected, result);
    }

    @Test
    public void testUpperRight() {
        Vector2D vector1 = new Vector2D(1, 0);
        Vector2D vector2 = new Vector2D(0, 1);
        Vector2D expected = new Vector2D(1, 1);
        Vector2D result = vector1.upperRight(vector2);

        assertEquals(expected, result);
    }

    @Test
    public void testLowerLeft() {
        Vector2D vector1 = new Vector2D(1, 0);
        Vector2D vector2 = new Vector2D(0, 1);
        Vector2D expected = new Vector2D(0, 0);
        Vector2D result = vector1.lowerLeft(vector2);

        assertEquals(expected, result);
    }

    @Test
    public void testOpposite() {
        Vector2D vector1 = new Vector2D(1, 1);
        Vector2D expected = new Vector2D(-1,-1);
        Vector2D result = vector1.opposite();

        assertEquals(expected, result);
    }

}