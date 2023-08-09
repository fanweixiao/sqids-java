import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SqidsBlocklistTest {
    @Test
    public void testDefaultBlocklist() throws Exception {
        Sqids sqids = new Sqids(new SqidsOptions.Builder().build());

        assertEquals(Arrays.asList(200044), sqids.decode("sexy"));
        assertEquals("d171vI", sqids.encode(new int[]{200044}));
    }

    @Test
    public void testEmptyBlocklist() throws Exception {
        Sqids sqids = new Sqids(new SqidsOptions.Builder().blocklist(new HashSet<>()).build());

        assertEquals(Arrays.asList(200044), sqids.decode("sexy"));
        assertEquals("sexy", sqids.encode(new int[]{200044}));
    }

    @Test
    public void testCustomBlocklist() throws Exception {
        Set<String> blocklist = new HashSet<>(Arrays.asList("AvTg"));
        Sqids sqids = new Sqids(new SqidsOptions.Builder().blocklist(blocklist).build());

        assertEquals(Arrays.asList(200044), sqids.decode("sexy"));
        assertEquals("sexy", sqids.encode(new int[]{200044}));

        assertEquals(Arrays.asList(100000), sqids.decode("AvTg"));
        assertEquals("7T1X8k", sqids.encode(new int[]{100000}));
        assertEquals(Arrays.asList(100000), sqids.decode("7T1X8k"));
    }

    @Test
    public void testBlocklist() throws Exception {
        Set<String> blocklist = new HashSet<>(Arrays.asList("8QRLaD", "7T1cd0dL", "UeIe", "imhw", "LfUQ"));
        Sqids sqids = new Sqids(new SqidsOptions.Builder().blocklist(blocklist).build());

        assertEquals("TM0x1Mxz", sqids.encode(new int[]{1, 2, 3}));
        assertEquals(Arrays.asList(1, 2, 3), sqids.decode("TM0x1Mxz"));

        assertEquals(Arrays.asList(1, 2, 3), sqids.decode("8QRLaD"));
        assertEquals(Arrays.asList(1, 2, 3), sqids.decode("7T1cd0dL"));
        assertEquals(Arrays.asList(1, 2, 3), sqids.decode("RA8UeIe7"));
        assertEquals(Arrays.asList(1, 2, 3), sqids.decode("WM3Limhw"));
        assertEquals(Arrays.asList(1, 2, 3), sqids.decode("LfUQh4HN"));
    }

    @Test
    public void testShortBlocklistWord() throws Exception {
        Set<String> blocklist = new HashSet<>(Arrays.asList("pPQ"));
        Sqids sqids = new Sqids(new SqidsOptions.Builder().blocklist(blocklist).build());

        assertEquals(Arrays.asList(1000), sqids.decode(sqids.encode(new int[]{1000})));
    }
}