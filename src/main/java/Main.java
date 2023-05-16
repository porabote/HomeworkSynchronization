import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static int maxRoutesCount = 1000;
    private static int maxRepeatedValue = 0;

    public static void main(String[] args) throws InterruptedException {


        Thread notificator = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                    System.out.println("Current max value " + maxRepeatedValue + " - " + sizeToFreq.get(maxRepeatedValue));
                }
            }
        });
        notificator.start();



        Runnable action = () -> {

            String route = generateRoute("RLRFR", 100);

            int countRightRepeated = 0;
            for (int i = 0; i < route.length(); i++) {
                if (route.charAt(i) == 'R')
                    countRightRepeated++;
            }


            synchronized (sizeToFreq) {
                if (!sizeToFreq.containsKey(countRightRepeated)) {
                    sizeToFreq.put(countRightRepeated, 1);
                } else {
                    sizeToFreq.put(countRightRepeated, sizeToFreq.get(countRightRepeated) + 1);
                }

                if (maxRepeatedValue == 0) {
                    maxRepeatedValue = countRightRepeated;
                } else if (sizeToFreq.get(countRightRepeated) > sizeToFreq.get(maxRepeatedValue)) {
                    maxRepeatedValue = countRightRepeated;
                }

                sizeToFreq.notify();

//                countFinishedProcesses++;
//                System.out.println(countFinishedProcesses);
//                if (countFinishedProcesses == maxRoutesCount) {
//                    sizeToFreq.notify();
//                }
            }
        };

        for (int i = 0; i < maxRoutesCount; i++) {
            Thread thread = new Thread(action);
            thread.start();
            thread.join();
        }

        notificator.interrupt();



        new Thread(() -> {

            System.out.println(sizeToFreq);
//            synchronized (sizeToFreq) {
//                try {
//                    sizeToFreq.wait();
//                } catch (InterruptedException e) {
//                    return;
//                }
//
//                System.out.println(sizeToFreq);
//            }

        }).start();

    }

    public static String generateRoute(String letters, int length) {

        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

}
