import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * equation generator object constructed with inputs of the challenge
 * generate method returns the first possible equation solving the challenge
 * 
 * Author: Matt Rouse
 * Date: 04/17/2022
 */
public class ConcurrentEquationGenerator 
{
    private final ReentrantLock outputLock = new ReentrantLock();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final String input;
    private final long targetResult;
    private String generatedEquation;
    private AtomicInteger tasksRemaining;

    public ConcurrentEquationGenerator(String input, long target)
    {
        this.input = input;
        this.targetResult = target;
        this.generatedEquation = String.format("Unable to generate equation for %s, %d.", input, target);
    }

    public String generate()
    {
        // generate list of all possible operands given the input string
        List<PartitionedInput> operandsList = PartitionedInput.getPossibleOperands(this.input);

        // comparator for PartitionedInput object overriden to sort the list so that inputs with fewer operands are processed first
        Collections.sort(operandsList);

        // highest number of tasks submitted will be equal to the number of possible partitioned inputs
        tasksRemaining = new AtomicInteger(operandsList.size());

        try 
        {
            // loop through all possible combination of operands and submit a task for each one
            for (PartitionedInput partitionedInput : operandsList) 
            {
                // if executor has been shutdown, no need to continue checking
                if(executorService.isShutdown())
                    break;
    
                // submits a runnable task to a concurrent exectuor service that will call the operate method on the operands
                executorService.submit(() -> 
                {
                    operate(partitionedInput.removeNextOperand(), partitionedInput.getNextOperand(), "");
                    tasksRemaining.decrementAndGet();
                });
            }

            // check periodically the number of partioned inputs remaining to be tested or if executor has been shutdown
            while(tasksRemaining.get() > 0)
            {
                if (executorService.isTerminated())
                    break;

                Thread.sleep(500);
            }
        }
        catch (RejectedExecutionException e) 
        {
            // will only throw if executor shutdown
        }
        catch (InterruptedException e)
        {
            // this is only thrown by the Thread.sleep call, should never happen but you never know
            generatedEquation = generatedEquation.concat(" Error: " + e.getMessage());
        }
        finally
        {
            // terminate all threads so that executor service can be garbage collected
            executorService.shutdownNow();
        }

        return generatedEquation;
    }

    private void operate(PartitionedInput operands, long result, String equation)
    {
        // if the equation string is empty start with the first operand operating on
        String equationString = equation.isEmpty() ? String.valueOf(result) : equation;

        // if all operands operated on, check if the result is equal to the target
        if(operands.getOperands().isEmpty())
        {
            if (result == this.targetResult) 
            {
                // acquire lock so that only one thread will set the generated equation value at a time
                // then check if service has already been shutdown, if equation already generated
                outputLock.lock();
                if (!executorService.isShutdown()) 
                {
                    this.generatedEquation = String.format("%s = %d", equationString, this.targetResult);
                    // this call will attempt to stop all running threads and cancel any submitted
                    executorService.shutdownNow();
                }
                // releases lock so that other threads unable to stop from shutdown now can finish running
                outputLock.unlock();
            }
            return;
        }
        else
        {
            // recursively call this method on each of the operands in the provided partitioned input
            long nextOperand = operands.getNextOperand();

            // call this method with each possible operator and append the operater to the equation string
            operate(
                operands.removeNextOperand(), 
                result + nextOperand, 
                String.format("%s + %d", equationString, nextOperand));

            operate(
                operands.removeNextOperand(), 
                result - nextOperand, 
                String.format("%s - %d", equationString, nextOperand));

            operate(
                operands.removeNextOperand(), 
                result * nextOperand, 
                String.format("%s * %d", equationString, nextOperand));

            // guard against a divide by zero scenario and a fractional intermediate result (might be able to remove this if all the values are stored in double)
            if (nextOperand != 0 && result % nextOperand == 0)
            {
                operate(
                    operands.removeNextOperand(), 
                    result / nextOperand, 
                    String.format("%s / %d", equationString, nextOperand));
            }
        }
    }
}
