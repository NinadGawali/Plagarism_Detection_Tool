public class StudentCode1 {
    // Student: Alice
    // Assignment: Calculate factorial
    
    public static void main(String[] args) {
        int number = 5;
        long result = calculateFactorial(number);
        System.out.println("Factorial of " + number + " is: " + result);
        
        // Test with multiple values
        for (int i = 1; i <= 10; i++) {
            System.out.println(i + "! = " + calculateFactorial(i));
        }
    }
    
    public static long calculateFactorial(int n) {
        if (n <= 1) {
            return 1;
        }
        long factorial = 1;
        for (int i = 2; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }
    
    public static boolean isPrime(int num) {
        if (num <= 1) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;
        
        for (int i = 3; i <= Math.sqrt(num); i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}
