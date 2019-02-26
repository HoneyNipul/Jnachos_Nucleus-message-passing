/**
 * Copyright (c) 1992-1993 The Regents of the University of California.
 * All rights reserved.  See copyright.h for copyright notice and limitation 
 * of liability and disclaimer of warranty provisions.
 *  
 *  Created by Patrick McSweeney on 12/5/08.
 */
package jnachos.kern;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

import jnachos.machine.*;

/** The class handles System calls made from user programs. */
public class SystemCallHandler {
	/** The System call index for halting. */
	public static final int SC_Halt = 0;

	/** The System call index for exiting a program. */
	public static final int SC_Exit = 1;

	/** The System call index for executing program. */
	public static final int SC_Exec = 2;

	/** The System call index for joining with a process. */
	public static final int SC_Join = 3;

	/** The System call index for creating a file. */
	public static final int SC_Create = 4;

	/** The System call index for opening a file. */
	public static final int SC_Open = 5;

	/** The System call index for reading a file. */
	public static final int SC_Read = 6;

	/** The System call index for writing a file. */
	public static final int SC_Write = 7;

	/** The System call index for closing a file. */
	public static final int SC_Close = 8;

	/** The System call index for forking a forking a new process. */
	public static final int SC_Fork = 9;

	/** The System call index for yielding a program. */
	public static final int SC_Yield = 10;
	
	/** The System call index for SendMaessage a program. */
	public static final int SC_Print= 11;
	
	/** The System call index for SendMaessage a program. */
	public static final int SC_SendMessage = 13;
	
	/** The System call index for WaitMessage a program. */
	public static final int SC_WaitMessage = 14;
	
	/** The System call index for yielding a program. */
	public static final int SC_SendAnswer = 15;
	
	/** The System call index for yielding a program. */
	public static final int SC_WaitAnswer = 16;
	

	
	
	/**
	 * Entry point into the Nachos kernel. Called when a user program is
	 * executing, and either does a syscall, or generates an addressing or
	 * arithmetic exception.
	 * 
	 * For system calls, the following is the calling convention:
	 * 
	 * system call code -- r2 arg1 -- r4 arg2 -- r5 arg3 -- r6 arg4 -- r7
	 * 
	 * The result of the system call, if any, must be put back into r2.
	 * 
	 * And don't forget to increment the pc before returning. (Or else you'll
	 * loop making the same system call forever!
	 * 
	 * @pWhich is the kind of exception. The list of possible exceptions are in
	 *         Machine.java
	 **/
	public static void handleSystemCall(int pWhichSysCall) {

		System.out.println("SysCall:" + pWhichSysCall);
		//move your pc
		Machine.writeRegister(Machine.PrevPCReg,Machine.readRegister(Machine.PCReg));
		Machine.writeRegister(Machine.PCReg,Machine.readRegister(Machine.NextPCReg));
		Machine.writeRegister(Machine.NextPCReg,Machine.readRegister(Machine.NextPCReg)+4);

		switch (pWhichSysCall) {
		// If halt is received shut down
		case SC_Halt:
			Debug.print('a', "Shutdown, initiated by user program.");
			Interrupt.halt();
			break;
			
		case SC_Print:
			boolean oldL_1 = Interrupt.setLevel(false);
			Convertor convertor3=new Convertor();
			String print=convertor3.readMemory(Machine.readRegister(4));
			System.out.println("***"+print+"***");
			
			Interrupt.setLevel(oldL_1);
			break;


		case SC_Exit:
			// Read in any arguments from the 4th register
			int arg = Machine.readRegister(4);

			System.out.println("Current Process " + JNachos.getCurrentProcess().getName() + " exiting with code " + arg);
			
			String name = JNachos.getCurrentProcess().getActualName();
			JNachos.processPool.remove(name);

			// Finish the invoking process
			JNachos.getCurrentProcess().finish();
			break;
			
			/* Called by Process A*/
		case SC_SendMessage:
			//for each system call, firstly, you need to disable interrupt
			boolean oldL1 = Interrupt.setLevel(false);
			Convertor convertor1=new Convertor();
			String ProcessName = JNachos.getCurrentProcess().getActualName();
			//String p = new String(ProcessName);
			System.out.println(" SendMessage called by "+ProcessName);
			
			int receiver_1 = Machine.readRegister(4);
			int message_1 = Machine.readRegister(5);
			int bufferNumber = Machine.readRegister(6);
		
			String receiver=convertor1.readMemory(receiver_1);
			System.out.println(" Receiver : " +receiver);

			String message=convertor1.readMemory(message_1);
			System.out.println(" Message : " +message);
			
		
			for(int i=0;i<JNachos.bufferPool.length;i++)
			{
				if(JNachos.bufferPool[i].isAvailable())
				{
					//System.out.println(" The name of Process:"+ProcessName);
					JNachos.bufferPool[i].setSender(ProcessName);
					JNachos.bufferPool[i].setReceiver(receiver);
					JNachos.bufferPool[i].setMessage(message);
					JNachos.bufferPool[i].setAvailable(false);
					Machine.writeRegister(6, JNachos.bufferPool[i].getId());
					//System.out.println(" Buffer id :"+JNachos.bufferPool[i].getId());
					System.out.println(" Message :"+"\""+JNachos.bufferPool[i].getMessage()+"\""+ " added in the buffer with id " +JNachos.bufferPool[i].getId() );
					//System.out.println(" Adding buffer to the receiver's queue");
					String receiverProcess = JNachos.bufferPool[i].getReceiver();
					
					/** Buffer added in the receivers queue*/
					NachosProcess receiverProcessObj = JNachos.processPool.get(receiverProcess);
					receiverProcessObj.getProcessQueue().put(JNachos.bufferPool[i].getId(), JNachos.bufferPool[i]);
					
					Machine.writeRegister(2, JNachos.bufferPool[i].getId());
					
					receiverProcessObj.getmQueue().add(JNachos.bufferPool[i].id);
					if(receiverProcessObj.getmQueue().isEmpty())
					{
						System.out.println("Queue is still empty for receiver "+receiverProcess);
					}
					else
					{
						System.out.println("Buffer added into the queue of receiver "+receiverProcess);
					}
					
					break;
				}
				
			}
			
			Interrupt.setLevel(oldL1);
			
			break;
			
			

			/*Executed by process A*/
			case SC_WaitAnswer:
			boolean oldL4 = Interrupt.setLevel(false);
			
			String CurrentProcessName=JNachos.getCurrentProcess().getActualName();
			NachosProcess ProcessObj = JNachos.processPool.get(CurrentProcessName);
			
			System.out.println(" WaitAnswer called by "+CurrentProcessName);
		
			HashMap processHashMAp=ProcessObj.getProcessQueue();
			BufferEntry entry = new BufferEntry();
			int BufferNumber = Machine.readRegister(6);
			if(BufferNumber>=0)
			{
				System.out.println(CurrentProcessName+ " Waiting for answer");
			}
			
		
			boolean flag1 = true;
		
			while(flag1)
			{
		
				if(!processHashMAp.isEmpty())
				{
					
					 entry= (BufferEntry)processHashMAp.get(BufferNumber);
					 System.out.println(CurrentProcessName+ " After the sendAnswer from receiver");
					 	
					 if (JNachos.getProcessPool().containsKey(entry.getSender())) {
					 System.out.println(" Received answer from process "+entry.getSender());
					 }
					 else
					 {
						 System.out.println(" Dummy answer from system in response to non existing process ");
					 }
					 processHashMAp.remove(BufferNumber);
			
				}
			
				else
				{
					JNachos.getCurrentProcess().yield();
				}
			}
			//JNachos.getCurrentProcess().finish();
			Interrupt.setLevel(oldL4);
			break;

			
			
			
			
			/* Called by process B*/
			
		case SC_WaitMessage:
			//for each system call, firstly, you need to disable interrupt
			
			boolean oldL = Interrupt.setLevel(false);
			Convertor convertor=new Convertor();
			
			String CurrentProcessName_1=JNachos.getCurrentProcess().getActualName();
			NachosProcess ProcessObj_1 = JNachos.processPool.get(CurrentProcessName_1);
			System.out.println(" Wait message called by "+CurrentProcessName_1);
			
			HashMap processHashMAp_1=ProcessObj_1.getProcessQueue();
			BufferEntry entry_1 = new BufferEntry();
			int BufferNumber_1 = Machine.readRegister(6);
			
			boolean flag = true;
		
			while(flag)
			{
		
				if(!processHashMAp_1.isEmpty())
				{
					
					Set keys = processHashMAp_1.keySet();
			        Iterator itr = keys.iterator();
			 
			        	while(itr.hasNext())
			        {
			        	BufferNumber_1 = (int)itr.next();
			      
			        }
			        Machine.writeRegister(2,BufferNumber_1);
			        //System.out.println(" ////////Buffer Number is /////////"+BufferNumber_1);
					
					flag=false;
			
				}
				else
				{
					JNachos.getCurrentProcess().yield();
				}
			}
			
		
			//JNachos.getCurrentProcess().finish();
			Interrupt.setLevel(oldL);
			break;
			
			/*Executed by process B*/
		case SC_SendAnswer:
			boolean oldL3 = Interrupt.setLevel(false);
			String CurrentProcessName_2=JNachos.getCurrentProcess().getActualName();
			
			System.out.println(" SendAnswer called by "+CurrentProcessName_2);
			Convertor convertor2=new Convertor();
			
			NachosProcess ProcessObj_2 = JNachos.processPool.get(CurrentProcessName_2);
			
			
			HashMap processHashMAp_2=ProcessObj_2.getProcessQueue();
			BufferEntry entry_2 = new BufferEntry();
			int BufferNumber_2 = Machine.readRegister(6);
			entry_2 = (BufferEntry)processHashMAp_2.get(BufferNumber_2);
			String processSendAnswer=entry_2.getSender();
			processHashMAp_2.remove(BufferNumber_2);
			
			NachosProcess processToSendAnswer=JNachos.getProcessPool().get(processSendAnswer);
			HashMap senderQueue = processToSendAnswer.getProcessQueue();
			
			
			/** Now will update the values of BufferEntry and put it in the queue of sender **/
			entry_2.setAnswer("Received the message");
			entry_2.setResult(1);
			entry_2.setSender(CurrentProcessName_2);
			entry_2.setReceiver(processSendAnswer);
			
			senderQueue.put(BufferNumber_2, entry_2);
			
			
			
			//JNachos.getCurrentProcess().finish();
			Interrupt.setLevel(oldL3);
			break;
			
				
		
		default:
			Interrupt.halt();
			break;
		}
	}
}