package org.voyiger;

/*

397
binary:
110001101
octal:
615
hexadecimal
18D

6/17 homework: Computer Number Systems, part 2
Take the binary, octal, decimal, and hex representations of your number from last class.
Perform the following operations on each of them and show your work.
For the binary number: Add 110101.
For the octal number: Subtract 177.
For the decimal number: Multiply by 21 and convert your answer to hexadecimal.
For the hex number: Divide by 2A. Express your answer in the form of a quotient and remainder (x R y).
 */
public class HomeWork620 {

    public static void main(String[] args) {
        //int binary = Integer.parseInt("110001101", 2  );
        int n = 397;
        System.out.println(n );
        System.out.println( Integer.toBinaryString( n ) );
        System.out.println("+");
        System.out.println( n + 0b110101);

        System.out.println("octal " + Integer.toOctalString( n ));
        System.out.println(n - 0177);

        System.out.println("decimal " + n);
        System.out.println(Integer.toHexString(  n * 21 ));

        System.out.println(Integer.toHexString( n ));



    }
}
