/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-30-2021

  This is my implementation of Lab4-Task2a.
  Implements the traffic light assignment from Lab2,
  but with LED display (thus without external components).
*/

#include <stdint.h>
#include "lab4_task2a.h"
#include "SSD2119_Display.h"
#include "SSD2119_Touch.h"

void FSM(); // definition of the FSM
void makeButton(); // creates buttons on the LED display
void readButton(); // reads input from on-screen buttons
void ADC0SS3_Handler(void); // handler for ADC event

enum frequency freq = PRESET2; // 60 MHz (Default)
enum FSM_States { start, stop, warn, go, end } FSM_State; // enums for the FSM

unsigned long masterControl = 0x0; // represents on/off button (0x0 = not pressed)
unsigned long pedestrianControl = 0x0; // represents pedestrian button (0x0 = not pressed)

volatile uint32_t cycle = 0; // indicates how many ADC cycles have passed
volatile uint32_t flag = 0; // indicates how many seconds have passed
volatile uint32_t inMaster = 0; // indicates how long the on/off button has been pressed
volatile uint32_t inPedestrian = 0; // indicates how long the pedestrian button has been pressed

int main() {
  LCD_Init();            // Initialize the LCD display
  Touch_Init();          // Initialize the LCD touch function
  
  makeButton();          // creating on-screen buttons
  FSM_State = start;     // initializing FSM's state
  
  PLL_Init(freq);        // Set system clock frequency to 60 MHz (Default)
  ADCReadPot_Init();     // Initialize ADC0
  TimerADCTriger_Init(); // Initialize Timer0A to trigger ADC0
  
  while(1){}

  return 0;
}

void ADC0SS3_Handler(void) {
  
  // adjusting for 1 second
  // I'm trying to use minimal timers
  // so I'm adjusting the 60MHz PLL clock rate to 1 second
  // SOLUTION: record 1 second every 3.75 ADC cycle (going to use 4 cycles)
  
  cycle++;
  if(cycle == 4){
    flag++; // indicates that 1 second has passed
    readButton(); // Reading buttons
    cycle = 0; // reset cycle count
  }
  
  // checking for 2+ seconds of button press
  if(inMaster >= 2) {
    masterControl = ~masterControl;
    inMaster = 0;
    flag = 5;
  } else if((inPedestrian >= 2) && (FSM_State == go)) {
    pedestrianControl = ~pedestrianControl;
    inPedestrian = 0;
    flag = 5;
  }
  
  if(flag > 4){ // represents state change every 5 seconds
    FSM(); // Run FSM
  }
  
  ADCISC |= 0x8; // Reset ADC0
}

void makeButton(){
  LCD_DrawFilledRect(290, 210, 30, 30, Color4[4]); // red rectangle for on/off button
  LCD_DrawFilledRect(290, 170, 30, 30, Color4[5]); // purple rectangle for pedestrian button
  
  LCD_DrawRect(30, 100, 50, 50, Color4[12]); // red light
  LCD_DrawRect(110, 100, 50, 50, Color4[14]); // yellow light
  LCD_DrawRect(190, 100, 50, 50, Color4[10]); // green light
}

void readButton(){
  int x = Touch_ReadX();
  int y = Touch_ReadY();
  
  if( (x > 2000) && (x < 2100) && (y > 600) && (y < 700) ){
    // Master button is pressed
    inMaster++;
  } else if ( (x > 2000) && (x < 2100) && (y > 800) && (y < 900) ){
    // Pedestrian button is pressed
    inPedestrian++;
  } else {
    inMaster = 0;
    inPedestrian = 0;
  }
}

void FSM(){
  switch(FSM_State) { // Transitions
     case start:
        FSM_State = end;
        break;

     case stop:
        flag = 0;
        if (!masterControl) {
          FSM_State = end;
        } else { 
          FSM_State = go;
        }
        break;

     case warn:
        flag = 0;
        if (!masterControl) {
          FSM_State = end;
        } else { 
          FSM_State = stop;
        }
        break;
        
     case go:
        if (!masterControl) {
          FSM_State = end;
        } else if (pedestrianControl) {
          pedestrianControl = ~pedestrianControl;
          FSM_State = warn;
        } else {
          FSM_State = stop;
        }
        flag = 0;
        break;
        
     case end:
        flag = 0;
        if (masterControl) {
           FSM_State = stop;
        }
        break;

     default:
        break;
  }

  switch(FSM_State) { // State actions
     case stop: // turn on red and turn off others
        LCD_DrawFilledRect(30, 100, 50, 50, Color4[12]); // red light
        LCD_DrawFilledRect(111, 101, 49, 49, Color4[0]); // yellow light
        LCD_DrawFilledRect(191, 101, 49, 49, Color4[0]); // green light
        break;

     case warn: // turn on yellow and turn off others
        LCD_DrawFilledRect(31, 101, 49, 49, Color4[0]); // red light
        LCD_DrawFilledRect(110, 100, 50, 50, Color4[14]); // yellow light
        LCD_DrawFilledRect(191, 101, 49, 49, Color4[0]); // green light
        break;
        
     case go: // turn on green and turn off others
        LCD_DrawFilledRect(31, 101, 49, 49, Color4[0]); // red light
        LCD_DrawFilledRect(111, 101, 49, 49, Color4[0]); // yellow light
        LCD_DrawFilledRect(190, 100, 50, 50, Color4[10]); // green light
        break;
        
     case end: // turn off all
        LCD_DrawFilledRect(31, 101, 49, 49, Color4[0]); // red light
        LCD_DrawFilledRect(111, 101, 49, 49, Color4[0]); // yellow light
        LCD_DrawFilledRect(191, 101, 49, 49, Color4[0]); // green light
        break;
        
     default:
        break;
   }
}