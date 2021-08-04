/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-22-2021

  This is my implementation of Lab3-Task1a.
  Reads the resistance from the TIVA board and adjusts 
  the on-board LEDs accordingly. Uses ADC for control.
*/

#include <stdio.h>
#include <stdint.h>
#include "lab3_task1a.h"

uint32_t ADC_value;
void ADC0SS3_Handler(void); // given function
void LED_on(int target); // turns on a LED based on given integer (1 ~ 4)
void LED_off_all(); // turns off all LEDs (1 ~ 4)

int main() {
  // Select system clock frequency preset
  enum frequency freq = PRESET2; // 60 MHz
  PLL_Init(freq);        // Set system clock frequency to 60 MHz
  LED_Init();            // Initialize the 4 onboard LEDs (GPIO)
  ADCReadPot_Init();     // Initialize ADC0 to read from the potentiometer
  TimerADCTriger_Init(); // Initialize Timer0A to trigger ADC0
  
  float resistance; // storing resistance
  
  while(1) {
    // STEP 5: Change the pattern of LEDs based on the resistance.
    // 5.1: Convert ADC_value to resistance in kilo-ohm
    resistance = ADC_value / 4095.0 * 10.0;
    // 5.2: Change the pattern of LEDs based on the resistance
    if (resistance < 2.5) {
      LED_off_all();
      LED_on(1);
    } else if (resistance < 5.0) {
      LED_off_all();
      LED_on(1);
      LED_on(2);
    } else if (resistance < 7.5) {
      LED_off_all();
      LED_on(1);
      LED_on(2);
      LED_on(3);
    } else {
      LED_off_all();
      LED_on(1);
      LED_on(2);
      LED_on(3);
      LED_on(4);
    }
  }
  
  return 0;
}

void ADC0SS3_Handler(void) {
  // STEP 4: Implement the ADC ISR.
  // 4.1: Clear the ADC0 interrupt flag
  ADCISC |= 0x8;
  // 4.2: Save the ADC value to global variable ADC_value
  ADC_value = ADCSSFIFO3;
}

void LED_on(int target){
  if(target == 1){
    GPIODATA_N |= 0x2; // turn LED D1 on
  } else if (target == 2) {
    GPIODATA_N |= 0x1; // turn LED D2 on
  } else if (target == 3) {
    GPIODATA_F |= 0x10; // turn LED D3 on
  } else if (target == 4) {
    GPIODATA_F |= 0x1; // turn LED D4 on
  }
}

void LED_off_all(){
  GPIODATA_F &= 0x0;
  GPIODATA_N &= 0x0;
}