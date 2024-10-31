import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger counter3 = new AtomicInteger();
    public static AtomicInteger counter4 = new AtomicInteger();
    public static AtomicInteger counter5 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindrome = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text) && !isoneLetter(text)) {
                    incrementCounter(text.length());
                }
            }
        });
        palindrome.start();

        Thread oneLetter = new Thread(() -> {
            for (String text : texts) {
                if (isoneLetter(text)) {
                    incrementCounter(text.length());
                }
            }
        });
        oneLetter.start();

        Thread increasing = new Thread(() -> {
            for (String text : texts) {
                if (!isPalindrome(text) && isIncreasing(text)) {
                    incrementCounter(text.length());
                }
            }
        });
        increasing.start();

        oneLetter.join();
        increasing.join();
        palindrome.join();

        System.out.println("Красивых слов с длиной 3: " + counter3);
        System.out.println("Красивых слов с длиной 4: " + counter4);
        System.out.println("Красивых слов с длиной 5: " + counter5);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean isPalindrome(String text) {
        return text.contentEquals(new StringBuilder(text).reverse());
    }

    public static boolean isoneLetter(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(i - 1))
                return false;
        }
        return true;
    }

    public static boolean isIncreasing(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1))
                return false;
        }
        return true;
    }

    public static void incrementCounter(int textLength) {
        if (textLength == 3) {
            counter3.getAndIncrement();
        } else if (textLength == 4) {
            counter4.getAndIncrement();
        } else {
            counter5.getAndIncrement();
        }
    }
}