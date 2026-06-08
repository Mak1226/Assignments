package processor.pipeline;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
//import generic.Statistics;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch,
			EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public boolean IsConflict(int res1, int res2) {
		Instruction extin = OF_EX_Latch.getInstruction();
		Instruction macin = EX_MA_Latch.getInstruction();
		Instruction rwin = MA_RW_Latch.getInstruction();
		int desEx, desMa, desRw;
		desEx = desMa = desRw = -2;
		boolean dvex, dvma, dvrw, ret;
		dvex = dvma = dvrw = ret = false;

		if (extin != null) {
			String str = extin.getOperationType().name();
			switch (str) {
				case "div":
				case "divi":
					dvex = true;

					break;

				default:
					break;
			}

			if (extin.getDestinationOperand() != null) {
				desEx = extin.getDestinationOperand().getValue();
			}
		}
		if (macin != null) {
			String str = macin.getOperationType().name();
			switch (str) {
				case "div":
				case "divi":
					dvma = true;

					break;

				default:
					break;
			}

			if (macin.getDestinationOperand() != null) {
				desMa = macin.getDestinationOperand().getValue();
			}
		}
		if (rwin != null) {
			String str = rwin.getOperationType().name();
			switch (str) {
				case "div":
				case "divi":
					dvrw = true;

					break;

				default:
					break;
			}

			if (rwin.getDestinationOperand() != null) {
				desRw = rwin.getDestinationOperand().getValue();
			}
		}
		if (res1 == desEx || res1 == desMa || res1 == desRw || res2 == desEx || res2 == desMa || res2 == desRw) {
			ret = true;
		}
		if ((dvex || dvma || dvrw) && (res1 == 31 || res2 == 31)) {
			ret = true;
		}
		if (ret) {
			IF_EnableLatch.setIF_enable(false);
			OF_EX_Latch.setEX_Lock(true);
		}
		return ret;
	}

	public void performOF() {
		if (IF_OF_Latch.isOF_Busy() == false) 
		{
			if (IF_EnableLatch.isIF_Busy())
				OF_EX_Latch.setEX_Lock(true);
			else 
			{
				if (IF_OF_Latch.isOF_enable() == true) 
				{
					OperationType[] operationType = OperationType.values();

					Operand rs1 = new Operand();
					Operand rs2 = new Operand();
					Operand rd = new Operand();
					Operand op = new Operand();

					// Statistics stc = new Statistics();
					// int Ins = stc.getNumberOfInstructions() + 1;
					// stc.setNumberOfInstructions(Ins);
					int instruction = IF_OF_Latch.getInstruction();

					int opcode = instruction >>> 27;
					OperationType operation = OperationType.values()[opcode];

					int pc = containingProcessor.getRegisterFile().getProgramCounter() - 1;
					Instruction inst = new Instruction();
					int nhk, imm_val;

					if (opcode >= 24 && opcode <= 28)
						IF_EnableLatch.setIF_enable(false);
					
					int r1, r2, r3;
					r1 = r2 = r3 = -1;
					inst.setOperationType(operation);
					inst.setProgramCounter(pc);
					switch (operation) 
					{
						case add:
						case sub:
						case mul:
						case div:
						case and:
						case or:
						case xor:
						case slt:
						case sll:
						case srl:
						case sra:
							rs1.setOperandType(OperandType.Register);
							nhk = instruction << 5;
							nhk = nhk >>> 27;
							r1 = nhk;
							rs1.setValue(nhk);
							rs2.setOperandType(OperandType.Register);
							nhk = instruction << 10;
							nhk = nhk >>> 27;
							r2 = nhk;
							rs2.setValue(nhk);
							rd.setOperandType(OperandType.Register);
							nhk = instruction << 15;
							nhk = nhk >>> 27;
							r3 = nhk;
							rd.setValue(nhk);
							if (IsConflict(r1, r2))

								break;

							inst.setSourceOperand1(rs1);
							inst.setSourceOperand2(rs2);
							inst.setDestinationOperand(rd);
							break;

						case end:

							IF_EnableLatch.setIF_enable(false);
							break;
						case jmp:

							imm_val = instruction << 10;
							imm_val = imm_val >> 10;
							int reg = instruction << 5;
							reg = reg >>> 27;
							if (imm_val != 0) {
								op.setOperandType(OperandType.Immediate);
								op.setValue(imm_val);

							} else {
								op.setOperandType(OperandType.Register);
								op.setValue(reg);
							}

							inst.setDestinationOperand(op);
							break;

						case beq:
						case bne:
						case blt:
						case bgt:
							rs1.setOperandType(OperandType.Register);
							nhk = instruction << 5;
							nhk = nhk >>> 27;
							r1 = nhk;
							rs1.setValue(nhk);
							rs2.setOperandType(OperandType.Register);
							nhk = instruction << 10;
							nhk = nhk >>> 27;
							r2 = nhk;
							rs2.setValue(nhk);
							rd.setOperandType(OperandType.Immediate);
							imm_val = instruction << 15;
							imm_val = imm_val >> 15;
							rd.setValue(imm_val);
							if (IsConflict(r1, r2))
								break;

							inst.setSourceOperand1(rs1);
							inst.setSourceOperand2(rs2);
							inst.setDestinationOperand(rd);
							break;

						default:
							rs1.setOperandType(OperandType.Register);
							nhk = instruction << 5;
							nhk = nhk >>> 27;
							r1 = nhk;
							rs1.setValue(nhk);
							rs2.setOperandType(OperandType.Immediate);
							imm_val = instruction << 15;
							imm_val = imm_val >> 15;

							rs2.setValue(imm_val);
							rd.setOperandType(OperandType.Register);
							nhk = instruction << 10;
							nhk = nhk >>> 27;
							rd.setValue(nhk);

							if (IsConflict(r1, r2))
								break;
							inst.setSourceOperand1(rs1);
							inst.setSourceOperand2(rs2);
							inst.setDestinationOperand(rd);
							break;
					}

					OF_EX_Latch.setInstruction(inst);
					OF_EX_Latch.setEX_enable(true);

					if (OF_EX_Latch.isEX_Locked() == false) {
						IF_OF_Latch.setOF_enable(false);
				}
			}
		}
	}
}
