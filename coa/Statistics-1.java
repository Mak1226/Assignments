package generic;

import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	static int numberOfOFStageInstructions;
	static int numberOfBranchesTaken;
	static int numberOfRWStageInstructions;
	static float ipc;

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);

			//Printing statistics in the statFile
			writer.println("No. of Instructions executed = " + numberOfInstructions);
			writer.println("No. of Cycles taken = " + numberOfCycles);
			writer.print("IPC + " + ipc);
			// writer.println("No. of OF_Stages Stalled = " + (numberOfInstructions - numberOfRWStageInstructions));
			// writer.println("No. of Wrong Branches Instructions = " + numberOfBranchesTaken);
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public static void setNumberOfInstructions(int numberOfInstructions) 
	{
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public static void setNumberOfCycles(int numberOfCycles) 
	{
		Statistics.numberOfCycles = numberOfCycles;
	}
	public static void setNumberOfOFStageInstructions(int numberOfInstructions)
	{
		Statistics.numberOfOFStageInstructions = numberOfInstructions; 
	}
	public static void setNumberOfBranchesTaken(int numberOfBranches)
	{
		Statistics.numberOfBranchesTaken = numberOfBranches; 
	}
	public static void setNumberOfRWStageInstructions(int numberOfInstructions)
	{
		Statistics.numberOfRWStageInstructions = numberOfInstructions; 
	}
	public static void setipc()
	{
		ipc = (float)numberOfInstructions / (float) numberOfCycles;
	}

	public static int getNumberOfInstructions() 
	{ 
		return numberOfInstructions; 
	}
	public static int getNumberOfCycles() 
	{ 
		return numberOfCycles; 
	}
	public static int getNumberOfOFStageInstructions() 
	{ 
		return numberOfOFStageInstructions; 
	}
	public static int getNumberOfBranchesTaken() 
	{ 
		return numberOfBranchesTaken; 
	}
	public static int getNumberOfRWStageInstructions() 
	{ 
		return numberOfRWStageInstructions; 
	}
	public static float getipc()
	{
		return ipc;
	}

}
