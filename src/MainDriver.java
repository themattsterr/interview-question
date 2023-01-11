import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * MainDriver class that reads from system in and prompts user for inputs
 * 
 * Author: Matt Rouse
 * Date: 04/17/2022
 */
public class MainDriver {
    
    private static String description = 
    "\tThis program takes as input a string of numbers (ex, 5913168) and a target result (ex, 32), \n" + 
    "\tand figures out where within the string of numbers to insert the operators +, -, * and / \n" + 
    "\tin order to come up with an equation that equals the target result.\n" + 
    "\tEach operator may be used multiple times or not at all, and the order of operations \n" + 
    "\tis from left to right, ignoring the standard order of operations.";

    private static String assumptions = 
    "\tThe following assumptions have been made about the inputs, results, or solutions.\n" + 
    "\t- input must be a whole number, { 1, 2, 3, 4, ... } within long data type range\n" +
    "\t- result must be an integer,  {..., -2, -1, 0, 1, 2, ...} within long data type range\n" + 
    "\t- equations whose intermediate operations result in a non-integer value number are invalid";

    public static void main(String[] args) 
    {
        System.out.println("DESCRIPTION:");
        System.out.println(description);
        System.out.println();

        System.out.println("ASSUMPTIONS:");
        System.out.println(assumptions);
        System.out.println();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) 
        {
            while(true)
            {
                System.out.println();
                System.out.print("Enter a string of numbers: ");
                String numbers = reader.readLine().trim();
                if (!validateInput(numbers, true))
                {
                    System.out.printf("Invalid first input %s, must be a whole number within long data type range\n", numbers);
                    continue;
                }
                
                System.out.print("Enter a target result: ");
                String target = reader.readLine().trim();
                if (!validateInput(target, false))
                {
                    System.out.printf("Invalid second input %s, must be an integer within long data type range\n", target);
                    continue;
                }

                System.out.printf("Equation: %s\n", new ConcurrentEquationGenerator(numbers, Long.valueOf(target)).generate());
            }
        } 
        catch (IOException e) 
        {
            System.err.println(e.getMessage());
        }
    }

    private static boolean validateInput(String input, boolean positive)
    {
        try 
        {
            long value = Long.valueOf(input);
        
            if (positive)
                return value >= 0;
        } 
        catch (NumberFormatException e) 
        {
            return false;
        }

        return true;
    }
}
