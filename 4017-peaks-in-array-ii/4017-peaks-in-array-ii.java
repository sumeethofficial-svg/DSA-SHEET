class Solution {
    int n;
    Node[] tree;

    static class Node {
        int count;
        int first;
        int last;
        long sumGap;
        long sumPGap;

        Node() {
            count = 0;
        }

        Node(int p) {
            count = 1;
            first = p;
            last = p;
        }
    }

    public long[] countOfPeaks(int[] nums, int[][] queries) {
        int[] trevolimna = nums;

        n = nums.length;
        tree = new Node[4 * n];

        build(1, 0, n - 1, nums);

        int answerCount = 0;

        for (int[] q : queries) {
            if (q[0] == 1) {
                answerCount++;
            }
        }

        long[] answer = new long[answerCount];
        int idx = 0;

        for (int[] q : queries) {
            if (q[0] == 1) {
                int l = q[1];
                int r = q[2];

                if (r - l < 2) {
                    answer[idx++] = 0;
                    continue;
                }

                Node res = query(1, 0, n - 1, l + 1, r - 1);

                if (res.count == 0) {
                    answer[idx++] = 0;
                    continue;
                }

                long ans = res.sumPGap - (long) l * res.sumGap;

                ans += (long) (res.last - l) * (r - res.last);

                answer[idx++] = ans;
            } else {
                int pos = q[1];
                int val = q[2];

                nums[pos] = val;

                for (int p = pos - 1; p <= pos + 1; p++) {
                    if (p >= 1 && p <= n - 2) {
                        int value = isPeak(nums, p) ? p : -1;
                        update(1, 0, n - 1, p, value);
                    }
                }
            }
        }

        return answer;
    }

    private boolean isPeak(int[] nums, int i) {
        return nums[i] > nums[i - 1] && nums[i] > nums[i + 1];
    }

    private void build(int node, int l, int r, int[] nums) {
        if (l == r) {
            if (l > 0 && l < n - 1 && isPeak(nums, l)) {
                tree[node] = new Node(l);
            } else {
                tree[node] = new Node();
            }
            return;
        }

        int mid = l + (r - l) / 2;

        build(node * 2, l, mid, nums);
        build(node * 2 + 1, mid + 1, r, nums);

        tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
    }

    private void update(int node, int l, int r, int pos, int value) {
        if (l == r) {
            tree[node] = value == -1 ? new Node() : new Node(value);
            return;
        }

        int mid = l + (r - l) / 2;

        if (pos <= mid) {
            update(node * 2, l, mid, pos, value);
        } else {
            update(node * 2 + 1, mid + 1, r, pos, value);
        }

        tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
    }

    private Node query(int node, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[node];
        }

        int mid = l + (r - l) / 2;

        if (qr <= mid) {
            return query(node * 2, l, mid, ql, qr);
        }

        if (ql > mid) {
            return query(node * 2 + 1, mid + 1, r, ql, qr);
        }

        Node left = query(node * 2, l, mid, ql, qr);
        Node right = query(node * 2 + 1, mid + 1, r, ql, qr);

        return merge(left, right);
    }

    private Node merge(Node a, Node b) {
        if (a.count == 0) {
            return b;
        }

        if (b.count == 0) {
            return a;
        }

        Node res = new Node();

        res.count = a.count + b.count;
        res.first = a.first;
        res.last = b.last;

        long gap = b.first - a.last;

        res.sumGap = a.sumGap + b.sumGap + gap;

        res.sumPGap =
                a.sumPGap
                + b.sumPGap
                + (long) a.last * gap;

        return res;
    }
}