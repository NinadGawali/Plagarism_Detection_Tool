public class StudentCode8 {
    // Student: Henry
    // Assignment: Fibonacci sequence
    
    public static void main(String[] args) {
        int count = 10;
        
        System.out.println("First " + count + " Fibonacci numbers:");
        for (int i = 0; i < count; i++) {
            System.out.print(fib(i) + " ");
        }
        System.out.println();
        
        // Using recursion
        System.out.println("\nRecursive approach:");
        for (int i = 0; i < count; i++) {
            System.out.print(fibRecursive(i) + " ");
        }
        System.out.println();
    }
    
    public static long fib(int n) {
        if (n <= 1) {
            return n;
        }
        
        long first = 0, second = 1;
        for (int i = 2; i <= n; i++) {
            long next = first + second;
            first = second;
            second = next;
        }
        return second;
    }
    
    public static long fibRecursive(int n) {
        if (n <= 1) {
            return n;
        }
        return fibRecursive(n - 1) + fibRecursive(n - 2);
    }
    
    public static boolean checkFibonacci(int number) {
        int first = 0, second = 1;
        while (first < number) {
            int next = first + second;
            first = second;
            second = next;
        }
        return first == number;
    }
}
