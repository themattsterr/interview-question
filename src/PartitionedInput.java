import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * immutable object used to store list of operands
 * static helper method used to split string it list of possible operands 
 * 
 * Author: Matt Rouse
 * Date: 04/17/2022
 */
public class PartitionedInput implements Comparable<PartitionedInput>
{
    private List<Long> operands;

    public PartitionedInput()
    {
        this.operands = new ArrayList<>();
    }

    public PartitionedInput(List<Long> operands) 
    {
        this.operands = new ArrayList<>(operands);
    }

    public PartitionedInput(PartitionedInput partitionedInput)
    {
        this.operands = new ArrayList<>(partitionedInput.operands);
    }

    public static List<PartitionedInput> getPossibleOperands(String input)
    {
        ArrayList<PartitionedInput> partitionedInputs = new ArrayList<>();
        partitionInput(input, 0, new PartitionedInput(), partitionedInputs);
        return partitionedInputs;
    }

    private static void partitionInput(String input, int index, PartitionedInput partitionedInput, List<PartitionedInput> partitionedInputs)
    {
        // termination condition for recursive method
        if (index == input.length())
        {
            partitionedInputs.add(partitionedInput);
            return;
        }

        // starting at the given index, iterate through each character of string
        // calling this method again to split the remaining string
        for (int i = index; i < input.length(); i++)
        {
            long nextOperand = Long.valueOf(input.substring(index, i + 1));

            partitionInput(
                input, 
                i + 1, 
                partitionedInput.addOperand(nextOperand), 
                partitionedInputs);
        }
    }

    public PartitionedInput addOperand(long value)
    {
        PartitionedInput pi = new PartitionedInput(this);
        pi.operands.add(value);
        return pi;
    }

    public PartitionedInput removeNextOperand()
    {
        // create a new partitioned input from this one and remove next operand
        // return resulting partitioned input
        PartitionedInput pi = new PartitionedInput(this);
        if (!pi.operands.isEmpty())
        {
            pi.operands.remove(0);
        }
        return pi;
    }

    public Long getNextOperand()
    {
        return operands.isEmpty() ? Long.MAX_VALUE : operands.get(0);
    }

    public List<Long> getOperands()
    {
        return Collections.unmodifiableList(operands);
    }

    public String toString()
    {
        return String.format("list:%s", operands);
    }

    @Override
    public int compareTo(PartitionedInput o) 
    {
        return Integer.compare(this.operands.size(), o.operands.size());
    }
}
