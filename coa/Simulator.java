package generic;

import processor.Clock;
import processor.Processor;

import java.io.*;
import java.nio.ByteBuffer;
import generic.Statistics;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		try {
			// create an object file
			System.out.println(assemblyProgramFile);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(assemblyProgramFile));

			// read one byte at a time
			int pc = 0; //Stores current PC value

			int ch; //used for reading lines from file
			int lc = 0; //used to keep track of number of lines read from assembly file
			byte[] ln = new byte[4]; //temporary location to store an instruction from assembly file

			int x = -1; //Bytes 0 to x of memory have global data stored in them;
			int y = x + 1; //Bytes x + 1 to y of memory have text/code segment stored in them

			//below code sets PC to the memory address of the first instruction
			ch = bis.read(ln, 0, 4);
			if(ch != -1)
			{
				pc = ByteBuffer.wrap(ln).getInt();
				processor.getRegisterFile().setProgramCounter(pc);
			}

			//writing global data to main memory
			while (true) 
			{
				if(lc >= pc)
					break;
				ch = bis.read(ln, 0, 4);
				y = ++x;
				lc++;
				int n = ByteBuffer.wrap(ln).getInt(); //temporary integer to store one integer of global data
				processor.getMainMemory().setWord(x, n);
			}

			//writing instructions to main memory
			ch = bis.read(ln, 0, 4);
			while (ch != -1)
			{
				y++;
				lc++;
				int m = ByteBuffer.wrap(ln).getInt(); //temporary integer to store one instruction
				processor.getMainMemory().setWord(y, m);
				ch = bis.read(ln, 0, 4);
			}
			int r0 =0, r1 = 1, r2 = 2;
			int val = (int)Math.pow(2,16);
			val--;
			processor.getRegisterFile().setValue(r0, 0);
			processor.getRegisterFile().setValue(r1, val);
			processor.getRegisterFile().setValue(r2, val);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void simulate()
	{
		Statistics stc = new Statistics();
		stc.setNumberOfInstructions(0);
		stc.setNumberOfCycles(0);

		while(simulationComplete == false)
		{
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			processor.getRWUnit().performRW();
			Clock.incrementClock();

			
			int inst = stc.getNumberOfInstructions() + 1;
			int noc = stc.getNumberOfCycles() + 1;
			stc.setNumberOfInstructions(inst);
			stc.setNumberOfCycles(noc);
		}
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
