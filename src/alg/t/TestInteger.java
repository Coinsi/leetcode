package alg.t;

public class TestInteger {
    public static void main(String[] args) {
        integerTest();
    }

    public static void integerTest() {

        Integer i1 = 40;
        Integer i2 = 40;
        Integer i3 = 0;
        Integer i4 = new Integer(40);
        Integer i5 = new Integer(40);
        Integer i6 = new Integer(0);

        System.err.println(i1 == i2);
        System.err.println(i1 == i2 + i3);
        System.err.println(i4 == i5);
        System.err.println(i4 == i5 + i6);

    }

}
