package alg.t;

public class TestBase {

    int add(int i) {
        i++;
        return i;
    }

    public static void main(String args[]) {
        TestBase test = new TestBase();
        int i = 0;
        test.add(i);
        i = i++;
        System.out.println(i);
    }
}