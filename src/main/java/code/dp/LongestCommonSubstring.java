package code.dp;

import java.util.List;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LongestCommonSubstring {

    private class Solution {
        private int get (String A, String B) {
            int result = 0;
            int[][] L = new int[2][B.length() + 1];
            int curRow = 0;
            for (int i = 0; i <= A.length(); i++) {
                for (int j = 0; j <= B.length(); j++) {
                    if (i == 0 || j == 0) L[curRow][j] = 0;
                    else if (A.charAt(i - 1) == B.charAt(j - 1)) {
                        L[curRow][j] = L[(1 - curRow)][(j - 1)] + 1;
                        result = Math.max(result, L[curRow][j]);
                    } else L[curRow][j] = 0;
                }
                curRow = 1 - curRow;
            }
            return result;
        }
    }

    class Solution2 {
        private int P = 113;
        private int MOD = 1_000_000_007;
        private int Pinv = BigInteger.valueOf(P).modInverse(BigInteger.valueOf(MOD)).intValue();

        private int[] rolling(int[] source, int length) {
            int[] ans = new int[source.length - length + 1];
            long h = 0, power = 1;
            if (length == 0) return ans;
            for (int i = 0; i < source.length; ++i) {
                h = (h + source[i] * power) % MOD;
                if (i < length - 1) {
                    power = (power * P) % MOD;
                } else {
                    ans[i - (length - 1)] = (int) h;
                    h = (h - source[i - (length - 1)]) * Pinv % MOD;
                    if (h < 0) h += MOD;
                }
            }
            return ans;
        }

        private boolean check(int guess, int[] A, int[] B) {
            Map<Integer, List<Integer>> hashes = new HashMap();
            int k = 0;
            for (int x: rolling(A, guess)) {
                hashes.computeIfAbsent(x, z -> new ArrayList()).add(k++);
            }
            int j = 0;
            for (int x: rolling(B, guess)) {
                for (int i: hashes.getOrDefault(x, new ArrayList<>()))
                    if (Arrays.equals(Arrays.copyOfRange(A, i, i + guess),
                            Arrays.copyOfRange(B, j, j + guess))) {
                        return true;
                    }
                j++;
            }
            return false;
        }

        public int findLength(int[] A, int[] B) {
            int lo = 0, hi = Math.min(A.length, B.length) + 1;
            while (lo < hi) {
                int mi = (lo + hi) / 2;
                if (check(mi, A, B)) lo = mi + 1;
                else hi = mi;
            }
            return lo - 1;
        }
    }

    public static void main(String[] args) {
        System.out.println(new LongestCommonSubstring().
                new Solution().get("abcba", "acab"));
    }

}
