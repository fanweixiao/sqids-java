import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SqidsTest {
    @Test
    public void testSimple() throws Exception {
        Sqids sqids = new Sqids(new SqidsOptions.Builder().alphabet("0123456789abcdef").build());

        int[] numbers = {1, 2, 3};
        String id = "4d9fd2";

        assertEquals(id, sqids.encode(numbers));
        assertArrayEquals(numbers, sqids.decode(id));
    }

    @Test
    public void testShortAlphabet() throws Exception {
        Sqids sqids = new Sqids(new SqidsOptions.Builder().alphabet("abcde").build());

        int[] numbers = {1, 2, 3};
        assertArrayEquals(numbers, sqids.decode(sqids.encode(numbers)));
    }

    @Test
    public void testLongAlphabet() throws Exception {
        Sqids sqids = new Sqids(new SqidsOptions.Builder().alphabet("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_+|{}[];:'\"/?.>,<`~").build());

        int[] numbers = {1, 2, 3};
        assertArrayEquals(numbers, sqids.decode(sqids.encode(numbers)));
    }

    @Test
    public void testRepeatingAlphabetCharacters() {
        assertThrows(Exception.class, () -> new Sqids(new SqidsOptions.Builder().alphabet("aabcdefg").build()), "Alphabet must contain unique characters");
    }

    @Test
    public void testTooShortOfAnAlphabet() {
        assertThrows(Exception.class, () -> new Sqids(new SqidsOptions.Builder().alphabet("abcd").build()), "Alphabet length must be at least 5");
    }
}