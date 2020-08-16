package commandLine;

public class Command {

    public void consumer(Consumer<Integer> a) {
        a.accept(10);
    }
    public void provideConsumer() {
        consumer(this::test1);   // method reference
        consumer(x -> test2(x)); // lambda
    }

    public static void help(String[] command){
        String msg = "Help Manual\r\n"+
                "ls   : Listing current thread\r\n" +
                "help : Bring helping manual\r\n" +
                "stop : Stop the server\r\n";
        System.out.println(msg);
    }

}
