/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-09-2021

  This is my implementation of Lab2-Task1a.
  This program turns on the LED lights on sequentially (from D4 -> D1)
  and turns them off sequentially (from D4 -> D1).

  The difference is the use of timers.
*/

#include <stdint.h>
#include "lab2_task1a.h"

void LED_init(); // initializes the LEDs so they can be turned on or off
void LED_on(int target); // turns on a LED based on given integer (1 ~ 4)
void LED_off(int target); // turns off a LED based on given integer (1 ~ 4)
void timer_init(); // initializes the timers
void gptm_polling(); // implements a "1-Hz-Rate" using timers (1 second delay)
void LED_pattern(); // Flashes the LEDs in a pattern

int main(){
  // initialize registers (LED & timer)
  LED_init();
  timer_init();
  
  // display LED pattern (infinite loop)
  while(1){
    LED_pattern();
  }
  
  return 0;
}

void LED_init(){
  volatile unsigned short delay = 0;
  RCGCGPIO |= 0x1020; // Enable PortF & PortN GPIO
  delay++; // Delay 2 more cycles before access Timer registers
  delay++; // Refer to Page. 756 of Datasheet for info
  
  GPIODIR_F |= 0x11; // Set PF0 & PF4 to output
  GPIODIR_N |= 0x3; // Set PN0 & PN1 to output
  GPIODEN_F |= 0x11; // Set PF0 & PF4 to digital port
  GPIODEN_N |= 0x3; // Set PN0 & PN1 to digital port
}

void LED_on(int target){
  if(target == 1){
    GPIODATA_F = 0x1; // turn LED 1 on
  } else if (target == 2) {
    GPIODATA_F |= 0x10; // turn LED 2 on
  } else if (target == 3) {
    GPIODATA_N |= 0x1; // turn LED 3 on
  } else if (target == 4) {
    GPIODATA_N |= 0x2; // turn LED 4 on
  }
}

void LED_off(int target){
  if(target == 1){
    GPIODATA_F &= ~(1 << 0); // turn LED 1 off
  } else if (target == 2) {
    GPIODATA_F &= ~(1 << 4); // turn LED 2 off
  } else if (target == 3) {
    GPIODATA_N &= ~(1 << 0); // turn LED 3 off
  } else if (target == 4) {
    GPIODATA_N &= ~(1 << 1); // turn LED 4 off
  }
}

void timer_init(){
  RCGCTIMER |= 0x1; // Enable Timer 0
  GPTMCTL &= 0x0; // Disable Timer A on Timer 0
  GPTMCFG = 0x0; // Reset & Select 32-bit configuration
  GPTMTAMR |= 0x2; // Enable Periodic timer mode
  GPTMTAMR &= ~(0x10); // Set to count down mode
  GPTMTAILR = 0xF42400; // Load 16 million on to timer
  GPTMCTL |= 0x1; // Enable Timer A on Timer 0
}

void gptm_polling(){
  while(!(GPTMRIS & 0x1)){}
  GPTMICR |= 0x1; // reset timer
}

void LED_pattern(){
  LED_on(1);
  gptm_polling();
  LED_on(2);
  gptm_polling();
  LED_on(3);
  gptm_polling();
  LED_on(4);
  gptm_polling();
  LED_off(1);
  gptm_polling();
  LED_off(2);
  gptm_polling();
  LED_off(3);
  gptm_polling();
  LED_off(4);
  gptm_polling();
}