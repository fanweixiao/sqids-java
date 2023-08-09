import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqidsTest {
    @Test
    public void testMinLengthSimple() throws Exception {
        Sqids sqids = new Sqids(new SqidsOptions.Builder().minLength(SqidsOptions.getDefaultOptions().getAlphabet().length()).build());

        int[] numbers = {1, 2, 3};
        String id = "75JILToVsGerOADWmHlY38xvbaNZKQ9wdFS0B6kcMEtnRpgizhjU42qT1cd0dL";

        assertEquals(id, sqids.encode(numbers));
        assertArrayEquals(numbers, sqids.decode(id));
    }

    @Test
    public void testMinLengthIncrementalNumbers() throws Exception {
        Sqids sqids = new Sqids(new SqidsOptions.Builder().minLength(SqidsOptions.getDefaultOptions().getAlphabet().length()).build());

        Map<String, int[]> ids = new HashMap<>();
        ids.put("jf26PLNeO5WbJDUV7FmMtlGXps3CoqkHnZ8cYd19yIiTAQuvKSExzhrRghBlwf", new int[]{0, 0});
        ids.put("vQLUq7zWXC6k9cNOtgJ2ZK8rbxuipBFAS10yTdYeRa3ojHwGnmMV4PDhESI2jL", new int[]{0, 1});
        ids.put("YhcpVK3COXbifmnZoLuxWgBQwtjsSaDGAdr0ReTHM16yI9vU8JNzlFq5Eu2oPp", new int[]{0, 2});
        ids.put("OTkn9daFgDZX6LbmfxI83RSKetJu0APihlsrYoz5pvQw7GyWHEUcN2jBqd4kJ9", new int[]{0, 3});
        ids.put("h2cV5eLNYj1x4ToZpfM90UlgHBOKikQFvnW36AC8zrmuJ7XdRytIGPawqYEbBe", new int[]{0, 4});
        ids.put("7Mf0HeUNkpsZOTvmcj836P9EWKaACBubInFJtwXR2DSzgYGhQV5i4lLxoT1qdU", new int[]{0, 5});
        ids.put("APVSD1ZIY4WGBK75xktMfTev8qsCJw6oyH2j3OnLcXRlhziUmpbuNEar05QCsI", new int[]{0, 6});
        ids.put("P0LUhnlT76rsWSofOeyRGQZv1cC5qu3dtaJYNEXwk8Vpx92bKiHIz4MgmiDOF7", new int[]{0, 7});
        ids.put("xAhypZMXYIGCL4uW0te6lsFHaPc3SiD1TBgw5O7bvodzjqUn89JQRfk2Nvm4JI", new int[]{0, 8});
        ids.put("94dRPIZ6irlXWvTbKywFuAhBoECQOVMjDJp53s2xeqaSzHY8nc17tmkLGwfGNl", new int[]{0, 9});

        for (Map.Entry<String, int[]> entry : ids.entrySet()) {
            String id = entry.getKey();
            int[] numbers = entry.getValue();

            assertEquals(id, sqids.encode(numbers));
            assertArrayEquals(numbers, sqids.decode(id));
        }
    }

    @Test
    public void testMinLengths() throws Exception {
        Sqids s = new Sqids();

        for (int minLength : new int[]{0, 1, 5, 10, SqidsOptions.getDefaultOptions().getAlphabet().length()}) {
            for (int[] numbers : new int[][]{
                    {s.minValue()},
                    {0, 0, 0, 0, 0},
                    {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                    {100, 200, 300},
                    {1_000, 2_000, 3_000},
                    {1_000_000},
                    {s.maxValue()},
            }) {
                Sqids sqids = new Sqids(new SqidsOptions.Builder().minLength(minLength).build());

                String id = sqids.encode(numbers);
                assertTrue(id.length() >= minLength);
                assertArrayEquals(numbers, sqids.decode(id));
            }
        }
    }

    @Test
    public void testOutOfRangeInvalidMinLength() throws Exception {
        Sqids s = new Sqids();
        String err = "Minimum length has to be between " + s.minValue() + " and " + SqidsOptions.getDefaultOptions().getAlphabet().length();

        assertThrows(Exception.class, () -> new Sqids(new SqidsOptions.Builder().minLength(-1).build()), err);
        assertThrows(Exception.class, () -> new Sqids(new SqidsOptions.Builder().minLength(SqidsOptions.getDefaultOptions().getAlphabet().length() + 1).build()), err);
    }
}