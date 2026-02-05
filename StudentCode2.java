public class StudentCode2 {
    // Student: Bob
    // Assignment: Calculate factorial
    
    public static void main(String[] args) {
        int num = 5;
        long answer = factorial(num);
        System.out.println("Factorial of " + num + " is: " + answer);
        
        // Test with multiple values
        for (int i = 1; i <= 10; i++) {
            System.out.println(i + "! = " + factorial(i));
        }
    }
    
    public static long factorial(int n) {
        if (n <= 1) {
            return 1;
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result = result * i;
        }
        return result;
    }
    
    public static boolean checkPrime(int number) {
        if (number <= 1) return false;
        if (number == 2) return true;
        if (number % 2 == 0) return false;
        
        for (int i = 3; i <= Math.sqrt(number); i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
