import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {
        ExecutorService executor = null;
       try{
            executor = Executors.newFixedThreadPool(2);

            Clock clock1 = Clock.setClock();
            System.out.println("Press enter to adjust minutes");

            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        clock1.clockWork();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        //e.printStackTrace();
                    }
                }
            });

            executor.submit(new Runnable() {
                @Override
                public void run() {
                    clock1.minAdjust();

                }
            });

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.DAYS);


       }catch(IllegalArgumentException e){
           System.out.println(e.getMessage());
           //e.printStackTrace();
       }catch (InterruptedException e) {
           executor.shutdownNow();
           System.out.println(e.getMessage());
           //e.printStackTrace();
       }catch (Exception e){
           System.out.println(e.getMessage());
           //e.printStackTrace();
       }
    }
}
