package alg.t;
import java.util.*;
class Solution {
    List<String> ans;
    Set<String> set = new HashSet<>();//哈希表去重
    int len;
    List<Integer> path = new ArrayList<>();
    String string;
    public List<String> removeInvalidParentheses(String s) {
        int left = 0, right = 0;
        for(int i = 0; i < s.length(); i++){//计算左右需要删掉的最小数量
            char c = s.charAt(i);
            if(c == '(') left++;
            else if(c == ')') {//注意中间可能有其他字符所以不能直接else，还需要判断一次右括号
                if(left == 0) right++;
                else left--;
            }
        }
        len = s.length();
        string = s;
        dfs(0, left, right);
        ans = new ArrayList<>(set);
        return ans;
    }
    public void dfs(int index, int left, int right)//当前位置是否去掉，选或不选
    {
        if(left == 0 && right == 0)//边界，括号去掉完了，获得去掉后的串，检查合法后加入哈希并return
        {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < len; i++)
                if(!path.contains(i)) sb.append(string.charAt(i));
            String cur = sb.toString();
            if(isValid(cur)) set.add(cur);
            return;
        }
        if(left + right > len - index) return;//剩余字符数量无法凑够删除数量，直接return
        char cur = string.charAt(index);
        if(cur == '(' && left > 0)//当前作为左括号的位置去掉
        {
            path.add(index);
            dfs(index + 1, left - 1, right);
            path.remove(path.size() - 1);
        }
        else if(cur == ')' && right > 0)//当前作为右括号的位置去掉
        {
            path.add(index);
            dfs(index + 1, left, right - 1);
            path.remove(path.size() - 1);
        }
        dfs(index + 1, left, right);//当前位置不去掉（不管是左右括号还是其他字符）
    }
    public boolean isValid(String s)//检查是否为有效括号串
    {
        int cnt = 0;
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(c == '(') cnt++;
            else if(c == ')') {
                cnt--;
                if(cnt < 0) return false;
            }
        }
        return cnt == 0;
    }
}
