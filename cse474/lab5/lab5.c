/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 08-03-2021

  This is my implementation of Lab5.
  I decided to implement PWM control, which can be used to dim lights.
  The LED of choice is the on-board LED4 (PF0).
  The brightness of LED is determined by the resistance reading of ADC.
  The brightness is inversly proportional to resistance.
  (High resistance = dimmer light)
*/

#include <stdint.h>
#include "lab5.h"
#include "PLL_Header.h"
#include "tm4c1294ncpdt.h"

uint32_t ADC_value;

int main(void) {
  PLL_Init();                              // Set frequency to 60 MHz
  LED_Init();                              // Initialize the LED4 (PF0)
  PWM_Init();                              // Init the PWM0, module0
  ADCReadPot_Init();                       // Init the ADC
  TimerADCTriger_Init();                   // Init the timer0
  
  while(1){}
  
  return 0;
}

void LED_Init(void){
  SYSCTL_RCGCGPIO_R |= 0x20;               // Enable PortF
  GPIO_PORTF_AMSEL_R |= 0x1;               // Analog function on PF0
  GPIO_PORTF_AFSEL_R |= 0x1;               // Alternate function on PF0
  GPIO_PORTF_PCTL_R &= ~(0xF);             // Reset MUX Control on PF0
  GPIO_PORTF_PCTL_R |= 0x6;                // Set MUX Control on PF0 (M0PWM0)
  GPIO_PORTF_DIR_R |= 0x1;                 // Set as output
  GPIO_PORTF_DEN_R |= 0x1;                 // Enable Digital
}

void PWM_Init(void){
  SYSCTL_RCGCPWM_R |= 0x1;                 // Enable PWM Module
  while((SYSCTL_PRPWM_R & 0x1) != 0x1 ){}; // Wait while FIFO is busy
  PWM0_CC_R  = (1 << 8);                   // CC[8]:USEPWMDIV
  PWM0_CC_R &= ~(0x7);                     // CC[2:0]=000 PWMDIV
  PWM0_CC_R |= (0x2);                      // CC[2:0]=0x2 divider = /8
  PWM0_0_CTL_R = 0x0;                      // Disable PWM Generator, and set to count down mode
  PWM0_0_LOAD_R = 50000;                   // Setup the period of the PWM signal
  PWM0_0_CMPA_R = 25000;                   // Set Duty Cycle to 50% (default)
  PWM0_0_GENA_R = 0x8c;                    // Set PWM Mode
  PWM0_0_CTL_R |= 0x1;                     // Enable PWM Generator
  PWM0_ENABLE_R = 0x1;                     // Enable PWM0 after setup
}

void PLL_Init(void) {
    // Do NOT modify this function.
    MOSCCTL &= ~(0x4);                     // Power up MOSC
    MOSCCTL &= ~(0x8);                     // Enable MOSC
    while ((RIS & 0x100) == 0) {};         // Wait for MOSC to be ready
    RSCLKCFG |= (0x3 << 20);               // Select MOSC as system clock source
    RSCLKCFG |= (0x3 << 24);               // Select MOSC as PLL clock source
    PLLFREQ0 |= 0x60;                      // Set MINT field
    PLLFREQ1 |= 0x4;                       // Set N field
    MEMTIM0 &= ~((0xF << 22) | (0xF << 6));// Reset FBCHT and EBCHT
    MEMTIM0 &= ~((0xF << 16) | (0xF << 0));// Reset EWS and FWS
    MEMTIM0 &= ~((0x1 << 21) | (0x1 << 5));// Reset FBCE and EBCE
    RSCLKCFG &= ~(0x1 << 28);              // Temporarilly bypass PLL
    MEMTIM0 |= (0x3 << 22) | (0x3 << 6);   // Set FBCHT and EBCHT
    MEMTIM0 |= (0x2 << 16) | (0x2 << 0);   // Set EWS and FWS
    RSCLKCFG |= 0x7;                       // Set PSYSDIV to use 60 MHZ clock
    RSCLKCFG &= ~0x3F8;                    // Update PSYSDIV field
    RSCLKCFG |= (0x1 << 30);               // Enable new PLL settings
    PLLFREQ0 |= (0x1 << 23);               // Power up PLL
    while ((PLLSTAT & 0x1) == 0) {};       // Wait for PLL to lock and stabilize
    RSCLKCFG |= (0x1u << 31) | (0x1 << 28);// Use PLL and update Memory Timing Register
}

void ADCReadPot_Init(void){
  volatile unsigned short delay = 0;
  RCGCADC |= 0x1;                          // Enable ADC0 clock
  delay++;                                 // Delay 3 more cycles before system clocks
  delay++;
  delay++;
  delay++;
  PLLFREQ0 |= 0x00800000;                  // Power up the PLL (if not already)
  while (PLLSTAT != 0x1);                  // Wait for the PLL to lock
  ADCCC |= 0x1;                            // Clock source set to ALTCLKCFG
  SYSCTL_RCGCGPIO_R |= 0x10;               // using PE3 (AIN0, pin12)
  delay++;                                 // Delay 2 more cycles before system clocks
  delay++;
  GPIOAFSEL |= 0x1;                        // Enable GPIOAFSEL for Pin 3
  GPIODEN_E &= 0x0;                        // Clear GPIODEN
  GPIOAMSEL_E |= 0x1;                      // Enable analog
  ADCACTSS &= ~(0x8);                      // Disable SS3
  ADCEMUX |= 0x5000;                       // Select timer as the trigger for SS3
  ADCSSMUX3 = 0x0;                         // Select analog input for SS3
  ADCSSCTL3 = 0x6;                         // Configure SS3
  ADCIM |= 0x8;                            // Set interrupt mask
  NVIC_EN0 |= (1 << 17);                   // interrupt 17 (Timer0 based interrupt)
  ADCACTSS |= 0x8;                         // Enable SS3
}

void TimerADCTriger_Init(void){
  RCGCTIMER |= 0x1;                        // Enable Timer 0
  GPTMCTL &= 0x0;                          // Disable Timer A on Timer 0
  GPTMCFG = 0x0;                           // Reset & Select 32-bit configuration
  GPTMTAMR |= 0x2;                         // Enable Periodic timer mode
  GPTMTAMR &= ~(0x10);                     // Set to count down mode
  GPTMTAILR = 0xF42400;                    // Load 16 million on to timer (1Hz)
  GPTMADCEV |= 0x1;                        // Enable ADC Event
  GPTMCTL |= 0x21;                         // Enable ADC trigger & timer0A
}

void ADC0SS3_Handler(void){
  ADCISC |= 0x8;                           // Clear the ADC0 interrupt flag
  ADC_value = ADCSSFIFO3;                  // Save ADC_value
  
  float resistance = 
    ADC_value / 4095.0 * 10.0 * 5000.0;    // Finding resistance and adjusting to meet PWM change
  
  PWM0_0_CMPA_R = 50000 - (int)resistance; // Adjusting the PWM duty cycle to change light intensity
}