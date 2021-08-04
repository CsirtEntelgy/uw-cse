/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-05-2021

  This is my implementation of Lab1-Task1a.
  This program turns on the LED lights on sequentially (from D4 -> D1)
  and turns them off sequentially (from D4 -> D1).
*/

#include <stdint.h>
#include "lab1_task1a.h"

void delayFunction(); // gives a delay around 0.3 seconds (for visibility)

int main(void){
  
  while(1){
    volatile unsigned short delay = 0;
    RCGCGPIO |= 0x1020; // Enable PortF & PortN GPIO
    delay++; // Delay 2 more cycles before access Timer registers
    delay++; // Refer to Page. 756 of Datasheet for info
    
    GPIODIR_F = 0x11; // Set PF0 & PF4 to output
    GPIODEN_F = 0x11; // Set PF0 & PF4 to digital port
    
    GPIODIR_N = 0x3; // Set PN0 & PN1 to output
    GPIODEN_N = 0x3; // Set PN0 & PN1 to digital port
    
    GPIODATA_F = 0x01; // Set PF0 to 1
    delayFunction();
    GPIODATA_F = 0x11; // Set PF4 to 1
    delayFunction();
    GPIODATA_N = 0x1; // Set PN0 to 1
    delayFunction();
    GPIODATA_N = 0x3; // Set PN1 to 1
    delayFunction();
    GPIODATA_F = 0x10; // Set PF0 to 0
    delayFunction();
    GPIODATA_F = 0x0; // Set PF4 to 0
    delayFunction();
    GPIODATA_N = 0x2; // Set PN0 to 0
    delayFunction();
    GPIODATA_N = 0x0; // Set PN1 to 0
    delayFunction();
  }
  
  return 0;
}

void delayFunction(){
  int j;
  for (j = 0; j < 1000000; j++) {}
}