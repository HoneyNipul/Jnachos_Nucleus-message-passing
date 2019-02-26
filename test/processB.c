/*
ProcessB.c will implement the message passsing calls
 */

#include "syscall.h"
//#include <stdio.h>
int main()
{
char Sender[255];
char Message[255];
char Answer[255];
char Result[255];
int Buffer=-1; /*initially initialised to zero */

    	Buffer=WaitMessage(Sender,Message,Buffer); 
	//Print(" The Receiver processB received message");
	
 	//Print(" The Receiver processB sending  answer: ");
	SendAnswer(Result,Answer,Buffer);

    /* not reached */
}
