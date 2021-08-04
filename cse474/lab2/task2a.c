/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-12-2021

  This is my implementation of Lab2-Task2a.
  This program turns on the LED lights on sequentially (from D4 -> D1)
  and turns them off sequentially (from D4 -> D1).

  The difference is the use of timers and interrupts
*/

#include <stdint.h>
#include "lab2_task2a.h"

void LED_init(); // initializes the LEDs so they can be turned on or off
void LED_on(int target); // turns on a LED based on given integer (1 ~ 4)
void LED_off(int target); // turns off a LED based on given integer (1 ~ 4)
void timer_init(); // initializes the timers
void Timer0A_Handler(); // action during interrupts
void LED_pattern(); // Flashes the LEDs in a pattern

volatile uint32_t flag = 0; // indicates which LED to change

int main(){
  // initialize registers (LED & timer)
  LED_init();
  timer_init();
  
  // display LED pattern (infinite loop)
  while(1){}
  
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
  GPTMIMR |= 0x1; // Mask Timer A with an interrupt
  NVIC_EN0 |= (1 << 19); // Enable interrupt no.19
  GPTMCTL |= 0x1; // Enable Timer A on Timer 0
}

void Timer0A_Handler(){
  LED_pattern();
  flag++;
  GPTMICR |= 0x1; // reset timer
}

void LED_pattern(){
  flag %= 8;
  switch(flag){
    case 0:
      LED_on(1);
      break;
    case 1:
      LED_on(2);
      break;
    case 2:
      LED_on(3);
      break;
    case 3:
      LED_on(4);
      break;
    case 4:
      LED_off(1);
      break;
    case 5:
      LED_off(2);
      break;
    case 6:
      LED_off(3);
      break;
    case 7:
      LED_off(4);
      break;
  }
}