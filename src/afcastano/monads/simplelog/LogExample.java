package afcastano.monads.simplelog;

public class LogExample {

    private Log<Integer> start(int val) {
        return Log.trace(val, "initial value");
    }

    private Log<Integer> add2(Integer val) {
        return Log.trace(val + 2, "run operation by adding 2");
    }

    private Log<Double> dividedBy4(Integer val) {
        return Log.trace(val/4d, "divided by 4");
    }

    public void run() {
        Log<Double> log = start(5)
                .flatMap(this::add2) //7
                .flatMap(this::dividedBy4) //1.75
                .flatMap( val ->
                        Log.trace( val * 2, "Multiplied by two")
                ); //3.5

        System.out.println("Value: " + log.getValue());
        System.out.println("Trace: " + log.getTrace());
    }

    public static void main(String[] args) {
        new LogExample().run();
    }

}
