/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-22-2021

  This is my file with supporting methods for lab3 task1a.
  Contains initialization for all needed registers.
*/

#include "PLL_Header.h"
#include "lab3_task1a.h"

int PLL_Init(enum frequency freq) {
    // Do NOT modify this function.
    MOSCCTL &= ~(0x4);                      // Power up MOSC
    MOSCCTL &= ~(0x8);                      // Enable MOSC
    while ((RIS & 0x100) == 0) {};          // Wait for MOSC to be ready
    RSCLKCFG |= (0x3 << 20);                // Select MOSC as system clock source
    RSCLKCFG |= (0x3 << 24);                // Select MOSC as PLL clock source

    PLLFREQ0 |= 0x60;                       // Set MINT field
    PLLFREQ1 |= 0x4;                        // Set N field

    MEMTIM0 &= ~((0xF << 22) | (0xF << 6));     // Reset FBCHT and EBCHT
    MEMTIM0 &= ~((0xF << 16) | (0xF << 0));     // Reset EWS and FWS
    MEMTIM0 &= ~((0x1 << 21) | (0x1 << 5));     // Reset FBCE and EBCE

    RSCLKCFG &= ~(0x1 << 28);                   // Temporarilly bypass PLL

    switch (freq) {
        case 120:
            MEMTIM0 |= (0x6 << 22) | (0x6 << 6);    // Set FBCHT and EBCHT
            MEMTIM0 |= (0x5 << 16) | (0x5 << 0);    // Set EWS and FWS
            RSCLKCFG |= 0x3;                        // Set PSYSDIV to use 120 MHZ clock
            RSCLKCFG &= ~0x3FC;                     // Update PSYSDIV field
            break;
        case 60:
            MEMTIM0 |= (0x3 << 22) | (0x3 << 6);    // Set FBCHT and EBCHT
            MEMTIM0 |= (0x2 << 16) | (0x2 << 0);    // Set EWS and FWS
            RSCLKCFG |= 0x7;                        // Set PSYSDIV to use 60 MHZ clock
            RSCLKCFG &= ~0x3F8;                     // Update PSYSDIV field
            break;
        case 12:
            MEMTIM0 |= (0x1 << 21) | (0x1 << 5);    // Set FBCE and EBCE
            RSCLKCFG |= 0x27;                       // Set PSYSDIV to use 12 MHZ clock
            RSCLKCFG &= ~0x3D8;                     // Update PSYSDIV field
            break;
        default:
            return -1;
    }

    RSCLKCFG |= (0x1 << 30);                // Enable new PLL settings
    PLLFREQ0 |= (0x1 << 23);                // Power up PLL
    while ((PLLSTAT & 0x1) == 0) {};        // Wait for PLL to lock and stabilize

    RSCLKCFG |= (0x1u << 31) | (0x1 << 28);  // Use PLL and update Memory Timing Register
    return 1;
}

void LED_Init(void) {
  volatile unsigned short delay = 0;
  RCGCGPIO |= 0x1020; // Enable PortF & PortN GPIO
  delay++; // Delay 2 more cycles before access Timer registers
  delay++; // Refer to Page. 756 of Datasheet for info
  
  GPIODIR_F |= 0x11; // Set PF0 & PF4 to output
  GPIODIR_N |= 0x3; // Set PN0 & PN1 to output
  GPIODEN_F |= 0x11; // Set PF0 & PF4 to digital port
  GPIODEN_N |= 0x3; // Set PN0 & PN1 to digital port
}

void ADCReadPot_Init(void) {
  volatile unsigned short delay = 0;
  // STEP 2: Initialize ADC0 SS3.
  // 2.1: Enable the ADC0 clock
  RCGCADC |= 0x1;
  // 2.2: Delay for RCGCADC (Refer to page 1073)
  delay++; // Delay 3 more cycles before system clocks
  delay++;
  delay++;
  delay++;
  // 2.3: Power up the PLL (if not already)
  PLLFREQ0 |= 0x00800000; // we did this for you
  // 2.4: Wait for the PLL to lock
  while (PLLSTAT != 0x1); // we did this for you
  // 2.5: Configure ADCCC to use the clock source defined by ALTCLKCFG
  ADCCC |= 0x1;
  // 2.6: Enable clock to the appropriate GPIO Modules (Hint: Table 15-1)
  RCGCGPIO |= 0x10; // using PE3 (AIN0, pin12)
  // 2.7: Delay for RCGCGPIO
  delay++;
  delay++;
  // 2.8: Set the GPIOAFSEL bits for the ADC input pins
  GPIOAFSEL |= 0x1;
  // 2.9: Clear the GPIODEN bits for the ADC input pins
  GPIODEN_E &= 0x0;
  // 2.10: Disable the analog isolation circuit for ADC input pins (GPIOAMSEL)
  GPIOAMSEL_E |= 0x1;
  // 2.11: Disable sample sequencer 3 (SS3)
  ADCACTSS &= ~(0x8);
  // 2.12: Select timer as the trigger for SS3
  ADCEMUX |= 0x5000;
  // 2.13: Select the analog input channel for SS3 (Hint: Table 15-1)
  ADCSSMUX3 = 0x0;
  // 2.14: Configure ADCSSCTL3 register
  ADCSSCTL3 = 0x6; // 0b0110 for Task1a | 0b1110 for Task1b
  // 2.15: Set the SS3 interrupt mask
  ADCIM |= 0x8;
  // 2.16: Set the corresponding bit for ADC0 SS3 in NVIC
  NVIC_EN0 |= (1 << 17); // interrupt 17
  // 2.17: Enable ADC0 SS3
  ADCACTSS |= 0x8;
}

void TimerADCTriger_Init(void) {
  RCGCTIMER |= 0x1; // Enable Timer 0
  GPTMCTL &= 0x0; // Disable Timer A on Timer 0
  GPTMCFG = 0x0; // Reset & Select 32-bit configuration
  GPTMTAMR |= 0x2; // Enable Periodic timer mode
  GPTMTAMR &= ~(0x10); // Set to count down mode
  GPTMTAILR = 0xF42400; // Load 16 million on to timer (1Hz)
  GPTMADCEV |= 0x1; // Enable ADC Event
  GPTMCTL |= 0x21; // Enable ADC trigger & timer0A
}