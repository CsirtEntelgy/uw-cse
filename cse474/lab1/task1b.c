/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-05-2021

  This is my implementation of Lab1-Task1b.
  This program turns on the LED lights according to button inputs.
  Pressing button 1 will light up LED01,
  and pressing button 2 will light up LED02.
*/

#include <stdint.h>
#include "lab1_task1b.h"

int main(void){
  
  volatile unsigned short delay = 0;
  RCGCGPIO |= 0x1100; // Enable PortJ & PortN
  delay++; // Delay 2 more cycles before access Timer registers
  delay++; // Refer to Page. 756 of Datasheet for info
  
  GPIODIR_N = 0x3; // Set PN0 & PN1 to output
  GPIODEN_N = 0x3; // Set PN0 & PN1 to digital port
  
  GPIODIR_J = GPIODIR_J & 0xFC; // Set PJ0 and PJ1 to be input
  GPIODEN_J = 0x3; // Enable digital port
  GPIOPUR_J = 0x3; // Enable Pull-up on PJ0 and PJ1
  
  while(1){
    if(GPIODATA_J & 0x01){ // if button 1 isn't pressed
      GPIODATA_N = 0x0;    // don't turn LED 1 on (turn off)
    } else {               // if button 1 is pressed
      GPIODATA_N = 0x2;    // turn on LED 1
    }
    
    if(GPIODATA_J & 0x02){ // if button 2 isn't pressed
      GPIODATA_N = 0x0;    // don't turn LED 2 on (turn off)
    } else {               // if button 2 is pressed
      GPIODATA_N = 0x1;    // turn on LED 2
    }
  }
  
  return 0;
}