/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-12-2021

  This is my implementation of Lab2-Task2b.
  The program will make LED1 blink at a 1 second interval,
  if button 2 is pressed, the timer will stop and LED 2 will turn on,
  and if button 1 is pressed, the timer will resume and LED 2 will turn off.
  
  Difference from Lab1-Task1b is the use of timers and interrupts.
*/

#include <stdint.h>
#include "lab2_task2b.h"

void LED_button_init(); // initializes the LEDs so they can be turned on or off
void LED_on(int target); // turns on a LED based on given integer (1 ~ 2)
void LED_off(int target); // turns off a LED based on given integer (1 ~ 2)
void timer_init(); // initializes the timers
void Timer0A_Handler(); // action during interrupts
void LED_pattern(); // Flashes the LEDs in a pattern

volatile uint32_t flag = 0; // indicates which LED to change
volatile uint32_t button = 1; // indicates the current button state (1 or 2)

int main(){
  // initialize registers (LED & timer)
  LED_button_init();
  timer_init();
  
  // display LED pattern (infinite loop)
  while(1){}
  
  return 0;
}

void LED_button_init(){
  volatile unsigned short delay = 0;
  RCGCGPIO |= 0x1100; // Enable PortJ & PortN
  delay++; // Delay 2 more cycles before access Timer registers
  delay++; // Refer to Page. 756 of Datasheet for info
  
  GPIODIR_N |= 0x3; // Set PN0 & PN1 to output
  GPIODEN_N |= 0x3; // Set PN0 & PN1 to digital port
  GPIODIR_J &= ~(0x3); // Set PJ0 and PJ1 to be input
  GPIODEN_J |= 0x3; // Enable digital port
  GPIOPUR_J |= 0x3; // Enable Pull-up on PJ0 and PJ1
}

void LED_on(int target){
  if (target == 1) {
    GPIODATA_N |= 0x2; // turn LED 1 on
  } else if (target == 2) {
    GPIODATA_N |= 0x1; // turn LED 2 on
  }
}

void LED_off(int target){
  if (target == 1) {
    GPIODATA_N &= ~(1 << 1); // turn LED 1 off
  } else if (target == 2) {
    GPIODATA_N &= ~(1 << 0); // turn LED 2 off
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
  // if button 1 is pressed and system is in button 2 state
  if(!(GPIODATA_J & 0x01) && (button == 2)){
    LED_off(2); // turn off LED2
    button = 1; // set button state to 1
  } else if (!(GPIODATA_J & 0x02) && (button == 1)){ // if button 2 is pressed and system is in button 1 state
    LED_off(1); // turn off LED1
    LED_on(2); // turn on LED2
    button = 2;
  }  
  
  if(button == 1){
    LED_pattern();
    flag++;
  }
  
  GPTMICR |= 0x1; // reset timer
}

void LED_pattern(){
  flag %= 2;
  switch(flag){
    case 0:
      LED_on(1);
      break;
    case 1:
      LED_off(1);
      break;
  }
}