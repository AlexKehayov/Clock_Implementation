import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Clock {
    private volatile int hours;
    private volatile int minutes;
    private volatile int seconds;
    private volatile boolean flag = true;
    private volatile boolean killClock = false;


    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isKillClock() {
        return killClock;
    }

    public void setKillClock(boolean killClock) {
        this.killClock = killClock;
    }

    public Clock(int hours, int minutes, int seconds){
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Clock)) return false;
        Clock clock = (Clock) o;
        return getHours() == clock.getHours() &&
                getMinutes() == clock.getMinutes() &&
                getSeconds() == clock.getSeconds();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHours(), getMinutes(), getSeconds());
    }

    public static Clock setClock(){
        String timePattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";

        Pattern pattern = Pattern.compile(timePattern);
        Matcher matcher;

        int hours=0;
        int minutes=0;
        int seconds=0;

        Scanner scan = new Scanner(System.in);
        System.out.println("Input hours, minutes and seconds (24h) in format hh:mm:ss");
        String time = scan.nextLine();

        matcher = pattern.matcher(time);

        if(matcher.matches()){
            String[] parts = time.split(":");
            hours = Integer.parseInt(parts[0]);
            minutes = Integer.parseInt(parts[1]);
            seconds = Integer.parseInt(parts[2]);
        }
        else throw new IllegalArgumentException("Illegal time format");


        return new Clock(hours, minutes, seconds);
    }

    public void minAdjust() {

        Scanner scan = new Scanner(System.in);
        int minutes;
        int hours;
        int clockMins;
        int clockHrs;
        int totalMins;
        int totalHrs;

            while (true) {
                try {
                scan.nextLine();
                System.out.println("Enter minutes to add (use '-' to subtract)");
                this.setFlag(false);
                minutes = Integer.parseInt(scan.nextLine());
                if(minutes==0){
                    this.setKillClock(true);
                    break;
                }
                hours=0;

                if (minutes > 59 || minutes < -59) {
                    hours = minutes / 60;
                    minutes -= hours * 60;
                }

                clockMins = this.getMinutes();
                clockHrs = this.getHours();

                totalHrs = clockHrs + hours;

                totalMins = minutes + clockMins;
                if (totalMins > 59 || totalMins < -59) {
                    totalHrs += totalMins / 60;
                    totalMins -= (totalMins / 60) * 60;
                }

                if (totalMins < 0) {
                    totalHrs--;
                    totalMins = 60 + totalMins;
                }

                while (totalHrs < 0) {
                    totalHrs = 24 + totalHrs;
                }

                while (totalHrs > 23) {
                    totalHrs -= 24;
                }

                this.setMinutes(totalMins);
                this.setHours(totalHrs);

            } catch(NumberFormatException e){
                System.out.println("Bad input (" + e.getMessage() + ")");
                //e.printStackTrace();
            } finally {
                    this.setFlag(true);
                    synchronized (this) {
                        notify();
                    }
                }
        }
    }

    public void clockWork() throws InterruptedException {
        while(killClock==false){
            synchronized (this) {
                if (!this.isFlag()) wait();
            }
            System.out.println(this.getHours() + ":" + this.getMinutes() + ":" + this.getSeconds());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.setSeconds(this.getSeconds()+1);
            if(this.getSeconds()==60){
                this.setSeconds(0);
                this.setMinutes(this.getMinutes()+1);
                if(this.getMinutes()==60){
                    this.setMinutes(0);
                    this.setHours(this.getHours()+1);
                    if(this.getHours()==24){
                        this.setHours(0);
                    }
                }
            }
        }
    }
}
