/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-09-2021

  This is my implementation of Lab2-Task1b.
  This program simulates a FSM which simulates a traffic lights system.
  
  Difference from Lab1-Task2 is the use of timers.
*/

#include <stdint.h>
#include "lab2_task1b.h"

void ext_init(); // initializes the external LEDs and buttons (the registers they connect to)
void timer_init(); // initializes the timers
void gptm_polling(); // implements a "1-Hz-Rate" using timers (1 second delay)
void check_switch_and_delay(); // reads from the external switches while implementing a 5 seconds delay
void FSM(); // definition of the FSM

enum FSM_States { start, stop, warn, go, end } FSM_State; // enums for the FSM
unsigned long masterControl = 0x0; // indicates if the on/off button is(was) pressed (0x0 = not pressed)
unsigned long pedestrianControl = 0x0; // indicates if the pedestrian mode button is(was) pressed (0x0 = not pressed)

int main(void) {
  // initialize registers (external LED/buttons & timer)
  ext_init();
  timer_init();
  FSM_State = start;
  
  while(1){
    FSM();
  }
  
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
  GPTMCTL |= 0x1; // Enable Timer A on Timer 0
}

void gptm_polling() {
  while(!(GPTMRIS & 0x1)){}
  GPTMICR |= 0x1; // reset timer
}

void check_switch_and_delay() {
  int master = 0;
  int pedestrian = 0;
  
  for (int i = 0; i < 5; i++) {
    // internal counters for checking buttons before&after delay
    int inMaster = 0;
    int inPedestrian = 0;
    
    // checking buttons before delay
    if((GPIODATA_L & 0x03) == 0x1) {
      inMaster = 1;
    } else if ((GPIODATA_L & 0x03) == 0x2) {
      inPedestrian = 1;
    } 
    
    gptm_polling(); // 1 second delay
    
    // checking buttons after delay
    if(((GPIODATA_L & 0x03) == 0x1) && inMaster) {
      master++;
    } else {
      master = 0;
    }
    if(((GPIODATA_L & 0x03) == 0x2) && inPedestrian) {
      pedestrian++;
    } else {
      pedestrian = 0;
    } 
    
    // checking for 2+ seconds of button press
    if(master == 2) {
      masterControl = ~masterControl;
      master = 0;
    } else if((pedestrian == 2) && (FSM_State == go)) {
      pedestrianControl = ~pedestrianControl;
      i += 5;
    }
  }
}

void FSM() {
  switch(FSM_State) { // Transitions
     case start:
        FSM_State = end;
        break;

     case stop:
        check_switch_and_delay();
        if (!masterControl) {
          FSM_State = end;
        } else { 
          FSM_State = go;
        }
        break;

     case warn:
        check_switch_and_delay();
        if (!masterControl) {
          FSM_State = end;
        } else { 
          FSM_State = stop;
        }
        break;
        
     case go:
        check_switch_and_delay();
        if (!masterControl) {
          FSM_State = end; 
        } else if (pedestrianControl) {
          FSM_State = warn;
          pedestrianControl = ~pedestrianControl;
        } else {
          FSM_State = stop;
        }
        break;
        
     case end:
        check_switch_and_delay();
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

