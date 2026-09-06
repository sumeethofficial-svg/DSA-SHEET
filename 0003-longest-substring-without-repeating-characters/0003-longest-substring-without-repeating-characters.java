class Solution {
    public int lengthOfLongestSubstring(String s) {
        int[] lastIndex = new int[128];
        Arrays.fill(lastIndex, -1);
        
        int maxLength = 0;
        int windowStart = 0;
        
        for (int windowEnd = 0; windowEnd < s.length(); windowEnd++) 
        {
            char currentChar = s.charAt(windowEnd);
            
            if (lastIndex[currentChar] >= windowStart) 
            {
                windowStart = lastIndex[currentChar] + 1;
            }
            
            lastIndex[currentChar] = windowEnd;
            maxLength = Math.max(maxLength, windowEnd - windowStart + 1);
        }
        
        return maxLength;
    }
}