package alg.fengtai.thread;

public class Main {
    private static int count = 0;
    private static final int total =50;

    public static void main(String[] args) {
        for (int i = 1; i <=4 ; i++) {
            int num = i;
            new Thread(
                    ()->{
                        for (int j = 0; j < total; j++) {

                        }
                    }
            ).start();

        }

    }
}


