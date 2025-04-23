package alg.test;

public class Bean{
    private float a;
    public float getA() {
        return a;
    }

    public static void main(String[] args) {
        Bean bean = new Bean();
       float b = bean.getA();
       System.out.println(b);
    }
}
