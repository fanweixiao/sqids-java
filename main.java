import java.util.*;
import java.util.Arrays;
import java.util.List;

public class Blocklist {
    public static List<String> blocklist = Arrays.asList("0rgasm", "1d10t", "1d1ot", "1di0t", "1diot", "1eccacu10", "1eccacu1o", "zoccola");
}

public class Sqids {
    private String alphabet;
    private int minLength;
    private Map<String, Boolean> blocklist;

    public Sqids(SqidsOptions options) throws Exception {
        if (options == null) {
            options = SqidsOptions.getDefaultOptions();
        }

        alphabet = options.getAlphabet();
        minLength = options.getMinLength();
        blocklist = options.getBlocklist();

        int minAlphabetLength = 5;
        if (alphabet.length() < minAlphabetLength) {
            throw new Exception("Alphabet length must be at least 5");
        }

        Set<Character> uniqueChars = new HashSet<>();
        for (char c : alphabet.toCharArray()) {
            uniqueChars.add(c);
        }
        if (uniqueChars.size() != alphabet.length()) {
            throw new Exception("Alphabet must contain unique characters");
        }

        if (minLength < minValue() || minLength > alphabet.length()) {
            throw new Exception("Minimum length has to be between " + minValue() + " and " + alphabet.length());
        }

        Map<String, Boolean> filteredBlocklist = new HashMap<>();
        String[] alphabetChars = alphabet.split("");
        for (String word : blocklist.keySet()) {
            if (word.length() >= 3) {
                String[] wordChars = word.split("");
                List<String> intersection = new ArrayList<>();
                for (String c : wordChars) {
                    if (contains(alphabetChars, c)) {
                        intersection.add(c);
                    }
                }
                if (intersection.size() == wordChars.length) {
                    filteredBlocklist.put(word.toLowerCase(), true);
                }
            }
        }

        this.blocklist = filteredBlocklist;
        this.alphabet = shuffle(alphabet);
    }

    public String encode(int[] numbers) throws Exception {
        if (numbers.length == 0) {
            return "";
        }

        List<Integer> inRangeNumbers = new ArrayList<>();
        for (int n : numbers) {
            if (n >= minValue() && n <= maxValue()) {
                inRangeNumbers.add(n);
            }
        }
        if (inRangeNumbers.size() != numbers.length) {
            throw new Exception("Encoding supports numbers between " + minValue() + " and " + maxValue());
        }

        return encodeNumbers(numbers, false);
    }

    public int[] decode(String id) throws Exception {
        List<Integer> ret = new ArrayList<>();

        if (id.equals("")) {
            return new int[0];
        }

        String[] alphabetChars = alphabet.split("");
        for (String c : id.split("")) {
            if (!contains(alphabetChars, c)) {
                return new int[0];
            }
        }

        String prefix = String.valueOf(id.charAt(0));
        int offset = alphabet.indexOf(prefix);
        String shiftedAlphabet = alphabet.substring(offset) + alphabet.substring(0, offset);
        String partition = String.valueOf(shiftedAlphabet.charAt(1));
        String slicedId = id.substring(1);

        int partitionIndex = slicedId.indexOf(partition);
        if (partitionIndex > 0 && partitionIndex < slicedId.length() - 1) {
            slicedId = slicedId.substring(partitionIndex + 1);
            shiftedAlphabet = shuffle(shiftedAlphabet);
        }

        while (slicedId.length() > 0) {
            String separator = String.valueOf(shiftedAlphabet.charAt(shiftedAlphabet.length() - 1));

            String[] chunks = slicedId.split(separator, -1);
            if (chunks.length > 0) {
                String alphabetWithoutSeparator = shiftedAlphabet.substring(0, shiftedAlphabet.length() - 1);
                int num = toNumber(chunks[0], alphabetWithoutSeparator);
                ret.add(num);

                if (chunks.length > 1) {
                    shiftedAlphabet = shuffle(shiftedAlphabet);
                }
            }

            slicedId = String.join(separator, Arrays.copyOfRange(chunks, 1, chunks.length));
        }

        int[] retArray = new int[ret.size()];
        for (int i = 0; i < ret.size(); i++) {
            retArray[i] = ret.get(i);
        }
        return retArray;
    }

    private int minValue() {
        return 0;
    }

    private int maxValue() {
        return Integer.MAX_VALUE;
    }

    private String encodeNumbers(int[] numbers, boolean partitioned) {
        int offset = 0;
        for (int i = 0; i < numbers.length; i++) {
            offset += alphabet.charAt(numbers[i] % alphabet.length()) + i;
        }
        offset %= alphabet.length();

        String shiftedAlphabet = alphabet.substring(offset) + alphabet.substring(0, offset);
        String prefix = String.valueOf(shiftedAlphabet.charAt(0));
        String partition = String.valueOf(shiftedAlphabet.charAt(1));
        shiftedAlphabet = shiftedAlphabet.substring(2);

        List<String> ret = new ArrayList<>();
        ret.add(prefix);

        for (int i = 0; i < numbers.length; i++) {
            String alphabetWithoutSeparator = shiftedAlphabet.substring(0, shiftedAlphabet.length() - 1);
            String id = toId(numbers[i], alphabetWithoutSeparator);
            ret.add(id);

            if (i < numbers.length - 1) {
                String separator = String.valueOf(shiftedAlphabet.charAt(shiftedAlphabet.length() - 1));

                if (partitioned && i == 0) {
                    ret.add(partition);
                } else {
                    ret.add(separator);
                }

                shiftedAlphabet = shuffle(shiftedAlphabet);
            }
        }

        String id = String.join("", ret);

        if (minLength > id.length()) {
            if (!partitioned) {
                int[] newNumbers = new int[numbers.length + 1];
                newNumbers[0] = 0;
                System.arraycopy(numbers, 0, newNumbers, 1, numbers.length);
                id = encodeNumbers(newNumbers, true);
            }

            if (minLength > id.length()) {
                id = prefix + shiftedAlphabet.substring(0, minLength - id.length()) + id.substring(1);
            }
        }

        if (isBlockedId(id)) {
            int[] newNumbers = numbers;

            if (partitioned) {
                if (numbers[0] + 1 > maxValue()) {
                    throw new Exception("Ran out of range checking against the blocklist");
                } else {
                    newNumbers[0] += 1;
                }
            } else {
                newNumbers = new int[numbers.length + 1];
                newNumbers[0] = 0;
                System.arraycopy(numbers, 0, newNumbers, 1, numbers.length);
            }

            id = encodeNumbers(newNumbers, true);
        }

        return id;
    }

    private String shuffle(String alphabet) {
        String[] chars = alphabet.split("");

        for (int i = 0, j = chars.length - 1; j > 0; i++, j--) {
            int r = (i * j + chars[i].charAt(0) + chars[j].charAt(0)) % chars.length;
            String temp = chars[i];
            chars[i] = chars[r];
            chars[r] = temp;
        }

        return String.join("", chars);
    }

    private String toId(int num, String alphabet) {
        List<String> id = new ArrayList<>();
        String[] chars = alphabet.split("");

        int result = num;
        while (result > 0) {
            id.add(0, chars[result % chars.length]);
            result = (int) Math.floor(result / chars.length);
        }

        return String.join("", id);
    }

    private int toNumber(String id, String alphabet) throws Exception {
        String[] chars = alphabet.split("");
        int ret = 0;

        for (String c : id.split("")) {
            int i = index(chars, c);
            if (i == -1) {
                throw new Exception("Invalid character in ID");
            }
            ret = ret * chars.length + i;
        }

        return ret;
    }

    private boolean isBlockedId(String id) {
        String lowercaseId = id.toLowerCase();

        for (String word : blocklist.keySet()) {
            if (word.length() <= lowercaseId.length()) {
                if (lowercaseId.length() <= 3 || word.length() <= 3) {
                    if (lowercaseId.equals(word)) {
                        return true;
                    }
                } else if (containsNumber(word)) {
                    if (lowercaseId.startsWith(word) || lowercaseId.endsWith(word)) {
                        return true;
                    }
                } else if (lowercaseId.contains(word)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean contains(String[] arr, String val) {
        for (String v : arr) {
            if (v.equals(val)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNumber(String s) {
        for (char c : s.toCharArray()) {
            if (c >= '0' && c <= '9') {
                return true;
            }
        }
        return false;
    }

    private int index(String[] arr, String val) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(val)) {
                return i;
            }
        }
        return -1;
    }
}