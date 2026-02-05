public class StudentCode6 {
    // Student: Frank
    // Assignment: String manipulation
    
    public static void main(String[] args) {
        String text = "Hello World";
        
        System.out.println("Original: " + text);
        System.out.println("Reversed: " + reverseString(text));
        System.out.println("Is Palindrome: " + isPalindrome(text));
        System.out.println("Uppercase: " + text.toUpperCase());
        System.out.println("Lowercase: " + text.toLowerCase());
        System.out.println("Length: " + text.length());
        System.out.println("Character count: " + countCharacters(text));
        System.out.println("Word count: " + countWords(text));
        
        // Test palindrome
        String palindrome = "racecar";
        System.out.println("\n'" + palindrome + "' is palindrome: " + isPalindrome(palindrome));
    }
    
    public static String reverseString(String str) {
        StringBuilder reversed = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--) {
            reversed.append(str.charAt(i));
        }
        return reversed.toString();
    }
    
    public static boolean isPalindrome(String str) {
        String cleaned = str.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        String reversed = reverseString(cleaned);
        return cleaned.equals(reversed);
    }
    
    public static int countCharacters(String str) {
        return str.replaceAll("\\s+", "").length();
    }
    
    public static int countWords(String str) {
        String trimmed = str.trim();
        if (trimmed.isEmpty()) {
            return 0;
        }
        return trimmed.split("\\s+").length;
    }
    
    public static int countVowels(String str) {
        int count = 0;
        String vowels = "aeiouAEIOU";
        for (char c : str.toCharArray()) {
            if (vowels.indexOf(c) != -1) {
                count++;
            }
        }
        return count;
    }
}
