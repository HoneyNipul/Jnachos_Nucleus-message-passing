package jnachos.kern;

import jnachos.machine.Machine;

public class Convertor {
	

	
	String readMemory(int Value)
	{
		String ReadValue="";
		Character c;
		int asciiValue = (Machine.readMem(Value, 1));
		c = (char)asciiValue; 
		while(c != '\0')
		{
			ReadValue +=c; 
			Value++;
			asciiValue = (Machine.readMem(Value, 1));
			c = (char)asciiValue; 
		}
		
		return ReadValue;
	}

	
	void writeMemory(int Address, String Value)
	{
		for(int i=0;i<Value.length();i++)
		{
			int ascci=Value.charAt(i);
		
		Machine.writeMem(Address, 1, ascci);
		Address++;
		}
		
		Machine.writeMem(Address, 1, '\0' );
		
		
	}
	
	
	
}
