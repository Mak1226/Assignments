package processor.pipeline;

import processor.Processor;
import processor.Clock;
import generic.*;

public class InstructionFetch implements Element {

	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	MA_RW_LatchType MA_RW_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_IF_LatchType eX_IF_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch) 
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	public void performIF() 
	{
	if (IF_EnableLatch.isIF_enable()) 
		{
			if (IF_EnableLatch.is_IF_Busy())
				return;
			
			if (EX_IF_Latch.is_EX_IF_enable()) 
			{
				int branchPC = EX_IF_Latch.get_PC();
				containingProcessor.getRegisterFile().setProgramCounter(branchPC);
				EX_IF_Latch.set_EX_IF_enable(false);
			}

			int curr_PC = containingProcessor.getRegisterFile().getProgramCounter();
			Simulator.setNoOfInstructions(Simulator.getNoOfInstructions() + 1);
			Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
								Clock.getCurrentTime() + containingProcessor.getL1iCache().getCacheLatency(),
								this,
								containingProcessor.getL1iCache(),
								curr_PC)
				);
				IF_EnableLatch.set_IF_Busy(true);
				containingProcessor.getRegisterFile().setProgramCounter(curr_PC + 1);
		}
	}
	@Override
	public void handleEvent(Event eve) 
	{
		if (IF_OF_Latch.is_OF_Busy()) {
			eve.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(eve);
		} 
		else  
		{
			MemoryResponseEvent event = (MemoryResponseEvent) eve;
			IF_OF_Latch.setInstruction(event.getValue());
			IF_EnableLatch.set_IF_Busy(false);
			IF_OF_Latch.setOF_enable(true);
		}
	}
}
