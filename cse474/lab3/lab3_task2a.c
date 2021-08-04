/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-22-2021

  This is my implementation of Lab3-Task2a.
  Sends temperature information via UART.
  Use on-board buttons to change the frequency 12 <-> 120.
*/

#include <stdio.h>
#include <stdint.h>
#include "lab3_task2a.h"

uint32_t ADC_value;
void ADC0SS3_Handler(void); // given function
void readButton(); // reads input from on-board buttons and sets frequency accordingly
enum frequency freq = PRESET2; // 60 MHz (Default)

int main(void) {
  // Select system clock frequency preset
  PLL_Init(freq);        // Set system clock frequency to 60 MHz (Default)
  UART_Init();           // Initialize the UART configuration
  Button_Init();         // Initialize the 4 onboard LEDs (GPIO)
  ADCReadPot_Init();     // Initialize ADC0 to read from the potentiometer
  TimerADCTriger_Init(); // Initialize Timer0A to trigger ADC0
  
  while(1) {
    // Reading buttons and setting temperature
    readButton();
  }

  return 0;
}

void ADC0SS3_Handler(void) {
  ADC_value = ADCSSFIFO3; // save ADC value
  float temperature = (147.5 - ((247.5 * ADC_value) / 4096.0)); // find temperature
  // sending data via UART
  char buffer[20]; // creating buffer to send
  int size = sprintf(buffer, "Temp in C: %.2f\n\r", temperature); // populating buffer
  for(int i = 0; i < size; ++i){ // sending buffer
    while((UARTFR & 0x20) != 0){} // wait for transmitter to clear
    UARTDR = buffer[i]; // send data, char by char
  }
  
  ADCISC |= 0x8; // reset ADC
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