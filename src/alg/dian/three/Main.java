package alg.dian.three;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int k = sc.nextInt();
        String s = sc.next();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int p = i + 1;
            char c = s.charAt(i);
            if(p % k == 0){
                if(Character.isLowerCase(c)){
                    res.append(Character.toUpperCase(c));
                }else{
                    res.append(Character.toLowerCase(c));
                }

            }else{
                res.append(getpos(c));
            }
        }
        System.out.println(res);
    }
    private static int getpos(char c){
        return Character.toLowerCase(c) - 'a' + 1;
    }
}
