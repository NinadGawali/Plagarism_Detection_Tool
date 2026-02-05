public class StudentCode7 {
    // Student: Grace
    // Assignment: Fibonacci sequence
    
    public static void main(String[] args) {
        int n = 10;
        
        System.out.println("Fibonacci sequence up to " + n + " terms:");
        for (int i = 0; i < n; i++) {
            System.out.print(fibonacci(i) + " ");
        }
        System.out.println();
        
        // Recursive version
        System.out.println("\nUsing recursion:");
        for (int i = 0; i < n; i++) {
            System.out.print(fibonacciRecursive(i) + " ");
        }
        System.out.println();
    }
    
    public static long fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        
        long a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            long temp = a + b;
            a = b;
            b = temp;
        }
        return b;
    }
    
    public static long fibonacciRecursive(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }
    
    public static boolean isFibonacci(int num) {
        int a = 0, b = 1;
        while (a < num) {
            int temp = a + b;
            a = b;
            b = temp;
        }
        return a == num;
    }
}
