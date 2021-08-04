/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-05-2021

  This is my implementation of Lab1-Task2.
  This program simulates a FSM which simulates a traffic lights system.

  Currently uses local variables to store a button press.
  But since the system implements a delay for visibility, 
  so a button press can be ignored if sent within the delay.
*/

#include <stdint.h>
#include "lab1_task2.h"

enum Task2_States { Task2_start, Task2_stop, Task2_warn, Task2_go, Task2_end } Task2_State;

void FSM(void); // definition of the FSM
void init(void); // initializes both LED and Switches
void LED_init(void); // initializes the LEDs
void extern_switch_init(void); // initializes the Switches
unsigned long switch_input(void); // reads from the Switches
void delayFunction(void); // gives a delay around 0.3 seconds (for visibility and to check button presses)
void checkButton(void); // checks button input and matches the variables below accordingly (happens before the delay)

unsigned long masterControl = 0x0; // indicates if the button is(was) pressed
unsigned long pedestrianControl = 0x0; // indicates if the button is(was) pressed

void main(void){
  init(); // initialize LEDs and Switches
  Task2_State = Task2_start; // initialize the fsm
  
  // run the FSM
  while(1){
    FSM();
  }
}

void FSM(void) {
  switch(Task2_State) { // Transitions
     case Task2_start:
        if(masterControl){
          Task2_State = Task2_end;
        } else { 
          Task2_State = Task2_go;
        }
        break;

     case Task2_stop:
        if(masterControl){
          Task2_State = Task2_end;
        } else { 
          Task2_State = Task2_go;
        }
        break;

     case Task2_warn:
        if(masterControl){
          Task2_State = Task2_end;
        } else { 
          Task2_State = Task2_stop;
        }
        break;
        
     case Task2_go:
        if (masterControl) {
          Task2_State = Task2_end; 
        } else if (pedestrianControl){
          Task2_State = Task2_warn;
        } else {
           Task2_State = Task2_stop;
        }
        break;
        
     case Task2_end:
        if (!masterControl) {
           Task2_State = Task2_start;
        } else {
           Task2_State = Task2_end;
        }
        break;

     default:
        Task2_State = Task2_start;
        break;
  }

  switch(Task2_State) { // State actions
     case Task2_stop: // turn on red and turn off others
        GPIODATA_L |= 0x4;
        GPIODATA_L &= 0x4;
        break;

     case Task2_warn: // turn on yellow and turn off others
        GPIODATA_L |= 0x8;
        GPIODATA_L &= 0x8;
        break;
        
     case Task2_go: // turn on green and turn off others
        GPIODATA_L |= 0x10;
        GPIODATA_L &= 0x10;
        break;
        
     case Task2_end: // turn off all
        GPIODATA_L &= ~0x1C;
        break;
        
     default:
        break;
   }
   
   checkButton(); // check button press before delay
   delayFunction();
}

void init(void){
  LED_init();
  extern_switch_init();
}

void LED_init(void) {
  volatile unsigned short delay = 0;
  RCGCGPIO |= 0x400; // activate clock for Port L
  delay++;
  delay++;
  
  GPIOAMSEL_L &= ~0x1C; // disable analog function of PL2, PL3, PL4
  GPIOAFSEL_L &= ~0x1C; // set PL2, PL3, PL4 to regular port function
  GPIODIR_L |= 0x1C; // set PL2, PL3, PL4 to output
  GPIODEN_L |= 0x1C; // enable digital output on PL2, PL3, PL4
}

void extern_switch_init(void) {
  volatile unsigned short delay = 0;
  RCGCGPIO |= 0x400; // activate clock for Port L
  delay++;
  delay++;
  
  GPIOAMSEL_L &= ~0x3; // disable analog function of PL0, PL1
  GPIOAFSEL_L &= ~0x3; // set PL0, PL1 to regular port function
  GPIODIR_L &= ~0x3; // set PL0, PL1 to input
  GPIODEN_L |= 0x3; // enable digital output on PL0, PL1
}

unsigned long switch_input(void) {
  return (GPIODATA_L & 0x03); // 0x2 or 0x1 (pressed) or 0 (not pressed)
}

void delayFunction(void){
  int j;
  for (j = 0; j < 1000000; j++) {}
}

void checkButton(void){
  if(switch_input() == 0x1){
    masterControl = ~masterControl;
  } else if ((switch_input() == 0x2) && Task2_State != Task2_stop){
    pedestrianControl = ~pedestrianControl;
  }
}