/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-22-2021

  This is my implementation of Lab3-Task2b.
  This program echos each character typed in the PUTTY terminal back
  to the putty terminal. Uses UART for reading and sending data.
*/

#include <stdio.h>
#include <stdint.h>
#include "lab3_task2b.h"

uint32_t ADC_value;
void ADC0SS3_Handler(void); // given function
void RTS(); // Return-to-Sender function for UART communication (back to PUTTY)
enum frequency freq = PRESET2; // 60 MHz (Default)

int main(void) {
  // Select system clock frequency preset
  PLL_Init(freq);        // Set system clock frequency to 60 MHz (Default)
  UART_Init();           // Initialize the UART configuration
  ADCReadPot_Init();     // Initialize ADC0 to read from the potentiometer
  TimerADCTriger_Init(); // Initialize Timer0A to trigger ADC0
  
  while(1) {}

  return 0;
}

void ADC0SS3_Handler(void) {
  RTS(); // RTS functionality
  ADCISC |= 0x8; // reset ADC
}

void RTS(){
  char read; // to store the data from putty
  char buffer[30]; // buffer to send back
  
  if((UARTFR & 0x10) == 0){ // if receiver isn't empty (i.e. data is fed)
    
    read = UARTDR; // storing data
    int size = sprintf(buffer, "Echo: %c\n\r", read); // populating buffer
    
    while((UARTFR & 0x20) != 0){} // wait for transmitter to clear
    for(int i = 0; i < size; ++i){ // sending buffer
      while((UARTFR & 0x20) != 0){} // wait for transmitter to clear
      UARTDR = buffer[i]; // send data, char by char
    }
  }
}