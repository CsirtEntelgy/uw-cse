/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-30-2021

  This is my implementation of Lab4-Task1c.
  Populates the LED screen with temperature info.
  The frequency of sampling can be changed with on-screen buttons.
*/

#include <stdint.h>
#include "lab4_task1c.h"
#include "SSD2119_Display.h"
#include "SSD2119_Touch.h"

uint32_t ADC_value; // holding ADC value
void ADC0SS3_Handler(void); // handler for ADC event
void makeButton(); // creates buttons on the LED display
void readButton(); // reads input from on-screen buttons and sets frequency accordingly
enum frequency freq = PRESET2; // 60 MHz (Default)

int main() {
  LCD_Init();            // Initialize the LCD display
  Touch_Init();          // Initialize the LCD touch function
  
  makeButton(); // creating on-screen buttons
  
  PLL_Init(freq);        // Set system clock frequency to 60 MHz (Default)
  ADCReadPot_Init();     // Initialize ADC0 to read from the potentiometer
  
  while(1) {}

  return 0;
}

void ADC0SS3_Handler(void) {
  // STEP 4: Implement the ADC ISR.
  // 4.1: Clear the ADC0 interrupt flag
  ADCISC |= 0x8;
  // 4.2: Save the ADC value to global variable ADC_value
  ADC_value = ADCSSFIFO3;
  // Calculating temperature
  float temperatureC = (147.5 - ((247.5 * ADC_value) / 4096.0));
  float temperatureF = temperatureC * 1.8 + 32;
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
  // Reading buttons
  readButton();
}

void makeButton(){
  LCD_DrawFilledRect(290, 210, 30, 30, Color4[4]); // red rectangle for 12 freq
  LCD_DrawFilledRect(290, 170, 30, 30, Color4[5]); // purple rectangle for 120 freq
}

void readButton(){
  int x = Touch_ReadX();
  int y = Touch_ReadY();
  
  printf("(%d, %d)\n", x, y);
  
  if( (x > 2000) && (x < 2100) && (y > 600) && (y < 700) ){
    // change to 12 frequency
    freq = PRESET3; // button 1 = 12 mHz
    PLL_Init(freq); // adjust frequency
  } else if ( (x > 2000) && (x < 2100) && (y > 800) && (y < 900) ){
    // change to 120 frequency
    freq = PRESET1; // button 2 = 120 mHz
    PLL_Init(freq); // adjust frequency
  }
}

