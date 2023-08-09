import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

public class UniquesTest {
    private static final int UPPER = 1_000_000;

    @Test
    public void testUniquesWithPadding() throws Exception {
        Sqids sqids = new Sqids(new SqidsOptions.Builder().minLength(SqidsOptions.getDefaultOptions().getAlphabet().length()).build());
        Set<String> set = new HashSet<>();

        for (int i = 0; i != UPPER; i++) {
            int[] numbers = {i};
            String id = sqids.encode(numbers);
            set.add(id);
            assertEquals(numbers, sqids.decode(id));
        }

        assertEquals(UPPER, set.size());
    }

    @Test
    public void testUniquesLowRanges() throws Exception {
        Sqids sqids = new Sqids();
        Set<String> set = new HashSet<>();

        for (int i = 0; i != UPPER; i++) {
            int[] numbers = {i};
            String id = sqids.encode(numbers);
            set.add(id);
            assertEquals(numbers, sqids.decode(id));
        }

        assertEquals(UPPER, set.size());
    }

    @Test
    public void testUniquesHighRanges() throws Exception {
        Sqids sqids = new Sqids();
        Set<String> set = new HashSet<>();

        for (int i = 100_000_000; i != 100_000_000 + UPPER; i++) {
            int[] numbers = {i};
            String id = sqids.encode(numbers);
            set.add(id);
            assertEquals(numbers, sqids.decode(id));
        }

        assertEquals(UPPER, set.size());
    }

    @Test
    public void testUniquesMulti() throws Exception {
        Sqids sqids = new Sqids();
        Set<String> set = new HashSet<>();

        for (int i = 0; i != UPPER; i++) {
            int[] numbers = {i, i, i, i, i};
            String id = sqids.encode(numbers);
            set.add(id);
            assertEquals(numbers, sqids.decode(id));
        }

        assertEquals(UPPER, set.size());
    }
}