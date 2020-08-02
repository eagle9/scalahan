package org.voyiger;

import java.util.Random;

/*
Pick a random decimal number between 100 and 999 and convert it to
binary, octal, and hexadecimal while showing work. That's it!
 */
public class HomeWork {
    public static void main(String[] args) {
        Random rand = new Random(  );
        int start = 100, end = 999;
        int n = rand.nextInt(end - start) + start;
        n = 64 + 11;
        System.out.println((char)('A' + 2));
        System.out.println(n);
        System.out.println("binary:");

        System.out.println(convert( n, 1));
        System.out.println("octal:");
        System.out.println(convert( n, 3 ));
        System.out.println("hexadecimal");
        System.out.println(convert( n, 4 ));
    }

    public static String convert(int n, int b) {
        StringBuilder sb = new StringBuilder(  );

        int mask = 0;
        for (int i = 0; i < b; i++) {
            mask |= 1 << i;
        }
        while (n > 0) {
            int digit = n & mask;

            n = n>>b;
            if (digit < 10) {
                sb.insert( 0, digit );
            }else {
                char c = (char)('A' + (digit-10));
                sb.insert( 0, c );
            }

        }
        return sb.toString();
    }
}
