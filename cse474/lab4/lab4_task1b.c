/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-30-2021

  This is my implementation of Lab4-Task1b.
  Populates the LED screen with temperature info.
  The frequency of sampling can be changed with on-board buttons.
*/

#include <stdint.h>
#include "lab4_task1b.h"
#include "SSD2119_Display.h"

uint32_t ADC_value; // holding ADC value
void ADC0SS3_Handler(void); // handler for ADC event
void readButton(); // reads input from on-board buttons and sets frequency accordingly
enum frequency freq = PRESET2; // 60 MHz (Default)

int main() {
  // Select system clock frequency preset
  LCD_Init();            // Initialize the LCD display
  PLL_Init(freq);        // Set system clock frequency to 60 MHz (Default)
  Button_Init();         // Initialize the 4 onboard LEDs (GPIO)
  ADCReadPot_Init();     // Initialize ADC0 to read from the potentiometer
  TimerADCTriger_Init(); // Initialize Timer0A to trigger ADC0

  float temperatureC; // storing temperature in C
  float temperatureF; // storing temperature in F
  
  while(1) {
    // Reading buttons
    readButton();
    // Calculating temperature
    temperatureC = (147.5 - ((247.5 * ADC_value) / 4096.0));
    temperatureF = temperatureC * 1.8 + 32;
    // Displaying temperature on LCD
    LCD_PrintString("The current temperature is ");
    LCD_PrintFloat(temperatureC);
    LCD_PrintString(" C, ");
    LCD_PrintFloat(temperatureF);
    LCD_PrintString(" F.\n");
    // Displaying frequency on LCD
    LCD_PrintString("The current clock frequency is ");
    LCD_PrintInteger(freq);
    LCD_PrintString(" MHz.\n");
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
  } else if(!(GPIODATA_J & 0x02)) { // if button 2 is pressed
    freq = PRESET1; // button 2 = 120 mHz
    PLL_Init(freq); // adjust frequency
  }
}

