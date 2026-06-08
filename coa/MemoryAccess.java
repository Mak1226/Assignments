package processor.pipeline;
import generic.Instruction;
import processor.Processor;
import generic.Instruction.OperationType;
import generic.*;
import processor.Clock;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_IF_LatchType EX_IF_Latch;
	public EX_MA_LatchType EX_MA_Latch;
	public MA_RW_LatchType MA_RW_Latch;
	public Instruction instruction;
	
	public MemoryAccess(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_IF_LatchType eX_IF_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
	}
	
	public void performMA()
	{
		
		if(EX_MA_Latch.is_MA_Busy() == false)
		{
			OF_EX_Latch.set_EX_Busy(false);

			if(EX_MA_Latch.is_MA_Locked() == false && EX_MA_Latch.isMA_enable() == true)
			{
				Instruction cur_ins = EX_MA_Latch.getInstruction();
				int alu = EX_MA_Latch.get_Alu_Result();
				OperationType cur_op = cur_ins.getOperationType();
				//int curr_PC = cur_ins.getProgramCounter();
				
				switch(cur_op)
				{
					case load:
					//int ld = containingProcessor.getMainMemory().getWord(alu);
					//MA_RW_Latch.set_LdResult(ld);
                    MemoryReadEvent mre = new MemoryReadEvent(Clock.getCurrentTime() + containingProcessor.getL1dCache().getCacheLatency(), this, containingProcessor.getL1dCache(), alu);
                    Simulator.getEventQueue().addEvent(mre);
					
					EX_MA_Latch.set_MA_Busy(true);
					break;
					
                    case store:
					int str = containingProcessor.getRegisterFile().getValue(cur_ins.getSourceOperand1().getValue());
					//containingProcessor.getMainMemory().setWord(alu, str);
					MemoryWriteEvent mwe = new MemoryWriteEvent(Clock.getCurrentTime() + containingProcessor.getL1dCache().getCacheLatency(), this, containingProcessor.getL1dCache(), alu, str);
                    Simulator.getEventQueue().addEvent(mwe);
                    
                    EX_MA_Latch.set_MA_Busy(true);
                    break;

                    case end:
					IF_EnableLatch.setIF_enable(false);
					break;
				}
				
				MA_RW_Latch.set_Alu_Result(alu);
				MA_RW_Latch.setInstruction(cur_ins);
				EX_MA_Latch.set_MA_enable(false);
				MA_RW_Latch.set_RW_enable(true);
			}
			else if(EX_MA_Latch.is_MA_Locked() == true)
			{
				MA_RW_Latch.set_RW_Lock(true);
				MA_RW_Latch.setInstruction(null);
				EX_MA_Latch.set_MA_Lock(false);
			}
		}
		else if(EX_MA_Latch.is_MA_Busy())
		
			OF_EX_Latch.set_EX_Busy(true);
		
		
	}
    
    @Override
	public void handleEvent(Event eve) {
		if(eve.getEventType() == Event.EventType.MemoryResponse) {
			MemoryResponseEvent event = (MemoryResponseEvent) eve;
            MA_RW_Latch.set_LdResult(event.getValue());
			MA_RW_Latch.setInstruction(instruction);

			EX_MA_Latch.set_MA_Busy(false);
			MA_RW_Latch.set_RW_enable(true);
			OF_EX_Latch.set_EX_Busy(false);
			EX_MA_Latch.set_MA_enable(false);
		}
	}
}


