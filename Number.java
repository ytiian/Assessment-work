import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Number {
    public static void main(String[] args){
        Scanner scanner=new Scanner(System.in);
        Task.target=scanner.nextInt();
        ExecutorService es= Executors.newFixedThreadPool(5);
        for(long i=0;i<1000000;i++){
            es.submit(new Task(i*1000,(i+1)*1000-1));
        }
        es.shutdown();
        System.out.println("子任务插入完成。");
        while (!es.isTerminated()){

        }
        System.out.println(Counter.count);
    }
}
class Task implements Runnable{
    protected static int target;
    protected long st;
    protected long ed;
    public Task(long x,long y){
        st=x;
        ed=y;
    }

    public void run(){
        for(long i=st;i<=ed;i++){
            if(contain(i,target)){
                synchronized(Counter.lock) {
                    Counter.count += i;
                }
            }
        }
    }
    private static boolean contain(long num,int x){
        return String.valueOf(num).contains(String.valueOf(x));
    }
}
class Counter{
    public static final Object lock =new Object();
    public static long count=0;
}