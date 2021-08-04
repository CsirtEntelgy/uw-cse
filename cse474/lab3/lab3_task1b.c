/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-22-2021

  This is my implementation of Lab3-Task1b.
  Displays the temperature via terminal.
  You can switch the frequency with on-board buttons to see the difference.
*/

#include <stdio.h>
#include <stdint.h>
#include "lab3_task1b.h"

uint32_t ADC_value;
void ADC0SS3_Handler(void); // given function
void readButton(); // reads input from on-board buttons and sets frequency accordingly
enum frequency freq = PRESET2; // 60 MHz (Default)

int main(void) {
  // Select system clock frequency preset
  PLL_Init(freq);        // Set system clock frequency to 60 MHz (Default)
  Button_Init();         // Initialize the 4 onboard LEDs (GPIO)
  ADCReadPot_Init();     // Initialize ADC0 to read from the potentiometer
  TimerADCTriger_Init(); // Initialize Timer0A to trigger ADC0

  float temperature; // storing temperature
  
  while(1) {
    // Reading buttons
    readButton();
    // Reading temperature
    temperature = (147.5 - ((247.5 * ADC_value) / 4096.0));
    // Displaying temperature
    printf("%.4f\n", temperature);
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

void readButton(){
  if(!(GPIODATA_J & 0x01)) { // if button 1 is pressed
    freq = PRESET3; // button 1 = 12 mHz
    PLL_Init(freq); // adjust frequency
    printf("changed to 12\n");
  } else if(!(GPIODATA_J & 0x02)) { // if button 2 is pressed
    freq = PRESET1; // button 2 = 120 mHz
    PLL_Init(freq); // adjust frequency
    printf("changed to 120\n");
  }
}