/*
ProcessA.c will implement the message passsing calls
 */

#include "syscall.h"
//#include "stdio.h"


int main()
{

char Message[255];
char Answer[255];
char Result[255];
int Buffer=-1;  /*initially initialised to zero */                     

	
    Buffer=SendMessage("processB","Hello Process B",Buffer); 
   //Print("Process A sendMessage called to processB");
	
	WaitAnswer(Result,Answer,Buffer);
  //Print("Process A wait message received");
  
    /* not reached */
}
