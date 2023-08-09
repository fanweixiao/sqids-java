import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SqidsEncodingTest {
    @Test
    public void testSimple() throws Exception {
        Sqids sqids = new Sqids();

        int[] numbers = {1, 2, 3};
        String id = "8QRLaD";

        assertEquals(id, sqids.encode(numbers));
        assertArrayEquals(numbers, sqids.decode(id));
    }

    @Test
    public void testDifferentInputs() throws Exception {
        Sqids sqids = new Sqids();

        int[] numbers = {
            0,
            0,
            0,
            1,
            2,
            3,
            100,
            1_000,
            100_000,
            1_000_000,
            sqids.maxValue(),
        };
        assertArrayEquals(numbers, sqids.decode(sqids.encode(numbers)));
    }

    @Test
    public void testIncrementalNumbers() throws Exception {
        Sqids sqids = new Sqids();

        String[] ids = {
            "bV",
            "U9",
            "g8",
            "Ez",
            "V8",
            "ul",
            "O3",
            "AF",
            "ph",
            "n8",
        };
        int[][] numbers = {
            {0},
            {1},
            {2},
            {3},
            {4},
            {5},
            {6},
            {7},
            {8},
            {9},
        };

        for (int i = 0; i < ids.length; i++) {
            assertEquals(ids[i], sqids.encode(numbers[i]));
            assertArrayEquals(numbers[i], sqids.decode(ids[i]));
        }
    }

    @Test
    public void testIncrementalNumbersSameIndex0() throws Exception {
        Sqids sqids = new Sqids();

        String[] ids = {
            "SrIu",
            "nZqE",
            "tJyf",
            "e86S",
            "rtC7",
            "sQ8R",
            "uz2n",
            "7Td9",
            "3nWE",
            "mIxM",
        };
        int[][] numbers = {
            {0, 0},
            {0, 1},
            {0, 2},
            {0, 3},
            {0, 4},
            {0, 5},
            {0, 6},
            {0, 7},
            {0, 8},
            {0, 9},
        };

        for (int i = 0; i < ids.length; i++) {
            assertEquals(ids[i], sqids.encode(numbers[i]));
            assertArrayEquals(numbers[i], sqids.decode(ids[i]));
        }
    }

    @Test
    public void testIncrementalNumbersSameIndex1() throws Exception {
        Sqids sqids = new Sqids();

        String[] ids = {
            "SrIu",
            "nbqh",
            "t4yj",
            "eQ6L",
            "r4Cc",
            "sL82",
            "uo2f",
            "7Zdq",
            "36Wf",
            "m4xT",
        };
        int[][] numbers = {
            {0, 0},
            {1, 0},
            {2, 0},
            {3, 0},
            {4, 0},
            {5, 0},
            {6, 0},
            {7, 0},
            {8, 0},
            {9, 0},
        };

        for (int i = 0; i < ids.length; i++) {
            assertEquals(ids[i], sqids.encode(numbers[i]));
            assertArrayEquals(numbers[i], sqids.decode(ids[i]));
        }
    }

    @Test
    public void testMultiInput() throws Exception {
        Sqids sqids = new Sqids();

        int[] numbers = new int[100];
        for (int i = 0; i < 100; i++) {
            numbers[i] = i;
        }
        assertArrayEquals(numbers, sqids.decode(sqids.encode(numbers)));
    }

    @Test
    public void testEncodingNoNumbers() throws Exception {
        Sqids sqids = new Sqids();
        assertEquals("", sqids.encode(new int[0]));
    }

    @Test
    public void testDecodingEmptyString() throws Exception {
        Sqids sqids = new Sqids();
        assertArrayEquals(new int[0], sqids.decode(""));
    }

    @Test
    public void testDecodingAnIDWithAnInvalidCharacter() throws Exception {
        Sqids sqids = new Sqids();
        assertArrayEquals(new int[0], sqids.decode("*"));
    }

    @Test
    public void testEncodeOutOfRangeNumbers() throws Exception {
        Sqids sqids = new Sqids();
        String err = "Encoding supports numbers between " + sqids.minValue() + " and " + sqids.maxValue();

        assertThrows(Exception.class, () -> sqids.encode(new int[]{sqids.minValue() - 1}), err);
        assertThrows(Exception.class, () -> sqids.encode(new int[]{sqids.maxValue() + 1}), err);
    }
}