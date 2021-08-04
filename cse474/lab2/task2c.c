/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-12-2021

  This is my implementation of Lab2-Task1c.
  This program simulates a FSM which simulates a traffic lights system.
  
  Difference from Lab1-Task2 is the use of timers and interrupts.
*/

#include <stdint.h>
#include "lab2_task2c.h"

void ext_init(); // initializes the external LEDs and buttons (the registers they connect to)
void timer_init(); // initializes the timers
void Timer0A_Handler(); // action during interrupts
void FSM(); // definition of the FSM

enum FSM_States { start, stop, warn, go, end } FSM_State; // enums for the FSM
unsigned long masterControl = 0x0; // indicates if the on/off button is(was) pressed (0x0 = not pressed)
unsigned long pedestrianControl = 0x0; // indicates if the pedestrian mode button is(was) pressed (0x0 = not pressed)

volatile uint32_t flag = 0; // indicates how many seconds have passed
volatile uint32_t inMaster = 0; // indicates how long the on/off button has been pressed
volatile uint32_t inPedestrian = 0; // indicates how long the pedestrian button has been pressed

int main(void) {
  // initialize registers (external LED/buttons & timer)
  ext_init();
  timer_init();
  FSM_State = start;
  
  // infinite loop (runs the FSM)
  while(1){};
  
  return 0;
}

void ext_init() {
  volatile unsigned short delay = 0;
  RCGCGPIO |= 0x400; // activate clock for Port L
  delay++;
  delay++;
  
  GPIOAMSEL_L &= ~0x1F; // disable analog function of P0, P1, PL2, PL3, PL4
  GPIOAFSEL_L &= ~0x1F; // set P0, P1, PL2, PL3, PL4 to regular port function
  GPIODIR_L |= 0x1C; // set PL2, PL3, PL4 to output
  GPIODEN_L |= 0x1F; // enable digital output on PL0, PL1, PL2, PL3, PL4
}

void timer_init() {
  RCGCTIMER |= 0x1; // Enable Timer 0
  GPTMCTL &= 0x0; // Disable Timer A on Timer 0
  GPTMCFG = 0x0; // Reset & Select 32-bit configuration
  GPTMTAMR |= 0x2; // Enable Periodic timer mode
  GPTMTAMR &= ~(0x10); // Set to count down mode
  GPTMTAILR = 0xF42400; // Load 16 million on to timer
  GPTMIMR |= 0x1; // Mask Timer A with an interrupt
  NVIC_EN0 |= (1 << 19); // Enable interrupt no.19
  GPTMCTL |= 0x1; // Enable Timer A on Timer 0
}

void Timer0A_Handler(){
  flag++; // indicates that 1 second has passed
  
  // checking buttons and add a second to press time
  if((GPIODATA_L & 0x03) == 0x1) {
    inMaster++;
  } else if ((GPIODATA_L & 0x03) == 0x2) {
    inPedestrian++;
  } else if ((GPIODATA_L & 0x03) != 0x1) {
    inMaster = 0;
  } else if ((GPIODATA_L & 0x03) != 0x2) {
    inPedestrian = 0;
  }
  
  // checking for 2+ seconds of button press
  if(inMaster == 2) {
    masterControl = ~masterControl;
    inMaster = 0;
    flag = 5;
  } else if((inPedestrian == 2) && (FSM_State == go)) {
    pedestrianControl = ~pedestrianControl;
    inPedestrian = 0;
    flag = 5;
  }
  
  if(flag > 4){
    FSM();
  }
  
  GPTMICR |= 0x1; // reset timer
}

void FSM() {
  switch(FSM_State) { // Transitions
     case start:
        FSM_State = end;
        break;

     case stop:
        flag = 0;
        if (!masterControl) {
          FSM_State = end;
        } else { 
          FSM_State = go;
        }
        break;

     case warn:
        flag = 0;
        if (!masterControl) {
          FSM_State = end;
        } else { 
          FSM_State = stop;
        }
        break;
        
     case go:
        if (!masterControl) {
          FSM_State = end;
        } else if (pedestrianControl) {
          pedestrianControl = ~pedestrianControl;
          FSM_State = warn;
        } else {
          FSM_State = stop;
        }
        flag = 0;
        break;
        
     case end:
        flag = 0;
        if (masterControl) {
           FSM_State = stop;
        }
        break;

     default:
        break;
  }

  switch(FSM_State) { // State actions
     case stop: // turn on red and turn off others
        GPIODATA_L |= 0x4;
        GPIODATA_L &= 0x4;
        break;

     case warn: // turn on yellow and turn off others
        GPIODATA_L |= 0x8;
        GPIODATA_L &= 0x8;
        break;
        
     case go: // turn on green and turn off others
        GPIODATA_L |= 0x10;
        GPIODATA_L &= 0x10;
        break;
        
     case end: // turn off all
        GPIODATA_L &= ~0x1C;
        break;
        
     default:
        break;
   }
}