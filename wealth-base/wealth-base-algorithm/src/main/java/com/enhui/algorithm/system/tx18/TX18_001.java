package com.enhui.algorithm.system.tx18;

public class TX18_001 {

    /**
     * 在【1，10】的格子上移动，从3到8，一共可以走15步，有几种走法
     */
    public static void main(String[] args) {
        // 格子范围【1，n】
        int n = 3;
        // 开始位置：cur
        int cur = 1;
        // 终止位置：end
        int end = 2;
        // 移动步数：rest
        int rest = 3;

        System.out.println(ways1(n, cur, end, rest));

        System.out.println(ways2(n, cur, end, rest));

        System.out.println(ways3(n, cur, end, rest));
    }

    private static int ways3(int n, int cur, int end, int rest) {
        int[][] dp = new int[n + 1][rest + 1];

        dp[end][0] = 1;
        // 按列处理，先第0列，在第一列，第二列
        for (int j = 1; j <= rest; j++) {
            dp[1][j] = dp[2][j - 1];
            for (int i = 2; i < n; i++) {
                dp[i][j] = dp[i + 1][j - 1] + dp[i - 1][j - 1];
            }
            dp[n][j] = dp[n - 1][j - 1];
        }
        return dp[cur][rest];
    }

    private static int ways2(int n, int cur, int end, int rest) {
        // 缓存：在i位置，剩j步时，方案数
        int[][] cache = new int[n + 1][rest + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= rest; j++) {
                cache[i][j] = -1;
            }
        }
        return recursionAddCache(n, cur, end, rest, cache);
    }

    private static int ways1(int n, int cur, int end, int rest) {
        return recursion(n, cur, end, rest);
    }


    /**
     * 递归方案：返回从start到end，走m步的方案数量
     *
     * @param n    范围
     * @param cur  初始位置
     * @param end  终点位置
     * @param rest 剩余步数
     * @return 移动方案数量
     */
    private static int recursion(int n, int cur, int end, int rest) {
        if (rest == 0) {
            return cur == end ? 1 : 0;
        }
        // 碰到左边，只能右走
        if (cur == 1) {
            return recursion(n, cur + 1, end, rest - 1);
        }
        // 碰到右边，只能左走
        if (cur == n) {
            return recursion(n, cur - 1, end, rest - 1);
        }
        int left = recursion(n, cur - 1, end, rest - 1);
        int right = recursion(n, cur + 1, end, rest - 1);
        return left + right;
    }

    private static int recursionAddCache(int n, int cur, int end, int rest, int[][] cache) {
        if (cache[cur][rest] != -1) {
            return cache[cur][rest];
        }
        int ans = 0;
        if (rest == 0) {
            ans = cur == end ? 1 : 0;
        } else if (cur == 1) {
            // 碰到左边，只能右走
            ans = recursionAddCache(n, cur + 1, end, rest - 1, cache);
        } else if (cur == n) {
            // 碰到右边，只能左走
            ans = recursionAddCache(n, cur - 1, end, rest - 1, cache);
        } else {
            // 中间位置，两边走的方案加起来
            int left = recursionAddCache(n, cur - 1, end, rest - 1, cache);
            int right = recursionAddCache(n, cur + 1, end, rest - 1, cache);
            ans = left + right;
        }
        cache[cur][rest] = ans;
        return ans;
    }
}
