import java.util.Arrays;

public class StudentCode5 {
    // Student: Eve
    // Assignment: Array operations and sorting
    
    public static void main(String[] args) {
        int[] data = {64, 34, 25, 12, 22, 11, 90};
        
        System.out.println("Before sorting:");
        displayArray(data);
        
        sortArray(data);
        
        System.out.println("\nAfter sorting:");
        displayArray(data);
        
        // Statistics
        System.out.println("\nMinimum: " + getMin(data));
        System.out.println("Maximum: " + getMax(data));
        System.out.println("Total: " + getSum(data));
        System.out.println("Mean: " + getMean(data));
    }
    
    public static void sortArray(int[] array) {
        int length = array.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    // Swap elements
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
    
    public static void displayArray(int[] array) {
        for (int element : array) {
            System.out.print(element + " ");
        }
        System.out.println();
    }
    
    public static int getMin(int[] array) {
        int minimum = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minimum) {
                minimum = array[i];
            }
        }
        return minimum;
    }
    
    public static int getMax(int[] array) {
        int maximum = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maximum) {
                maximum = array[i];
            }
        }
        return maximum;
    }
    
    public static int getSum(int[] array) {
        int total = 0;
        for (int num : array) {
            total += num;
        }
        return total;
    }
    
    public static double getMean(int[] array) {
        return (double) getSum(array) / array.length;
    }
}
