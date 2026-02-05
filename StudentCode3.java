public class StudentCode3 {
    // Student: Charlie
    // Assignment: Calculate factorial
    
    public static void main(String[] args) {
        System.out.println("Factorial Calculator");
        System.out.println("====================");
        
        int value = 5;
        long factorialResult = computeFactorial(value);
        System.out.println("The factorial of " + value + " equals " + factorialResult);
        
        // Display factorial table
        System.out.println("\nFactorial Table:");
        for (int x = 1; x <= 10; x++) {
            System.out.printf("%2d! = %d\n", x, computeFactorial(x));
        }
    }
    
    public static long computeFactorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        
        long product = 1;
        for (int counter = 2; counter <= n; counter++) {
            product *= counter;
        }
        return product;
    }
    
    public static boolean isPrimeNumber(int n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        
        for (int divisor = 5; divisor * divisor <= n; divisor += 6) {
            if (n % divisor == 0 || n % (divisor + 2) == 0) {
                return false;
            }
        }
        return true;
    }
}
