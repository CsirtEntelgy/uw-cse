/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-30-2021
  
  Header file for Lab4_task2a.
  Contains addresses for the used registers.
*/

#ifndef __LAB4T2A_H__
#define __LAB4T2A_H__

// addresses for ADC0
#define RCGCGPIO (*((volatile uint32_t *)0x400FE608))
#define RCGCADC (*((volatile uint32_t *)0x400FE638))
#define ADCCC (*((volatile uint32_t *)0x40038FC8))
#define GPIOAFSEL (*((volatile uint32_t *)0x4005C420)) // E based
#define GPIODEN_E (*((volatile uint32_t *)0x4005C51C)) // E based
#define GPIOAMSEL_E (*((volatile uint32_t *)0x4005C528)) // E based
#define ADCACTSS (*((volatile uint32_t *)0x40038000))
#define ADCEMUX (*((volatile uint32_t *)0x40038014))
#define ADCSSMUX3 (*((volatile uint32_t *)0x400380A0))
#define ADCSSEMUX3 (*((volatile uint32_t *)0x400380B8))
#define ADCSSCTL3 (*((volatile uint32_t *)0x400380A4))
#define ADCSSTSH3 (*((volatile uint32_t *)0x400380BC))
#define ADCIM (*((volatile uint32_t *)0x40038008))
#define NVIC_EN0 (*((volatile uint32_t *)0xE000E100)) // interrupt 17
#define ADCISC (*((volatile uint32_t *)0x4003800C))
#define ADCSSFIFO3 (*((volatile uint32_t *)0x400380A8))

// addresses for timer0A
#define RCGCTIMER (*((volatile uint32_t *)0x400FE604))
#define GPTMCTL (*((volatile uint32_t *)0x4003000C))
#define GPTMCFG (*((volatile uint32_t *)0x40030000))
#define GPTMTAMR (*((volatile uint32_t *)0x40030004))
#define GPTMTAILR (*((volatile uint32_t *)0x40030028))
#define GPTMADCEV (*((volatile uint32_t *)0x40030070))

// given functions & definitions
enum frequency {PRESET1 = 120, PRESET2 = 60, PRESET3 = 12};
int PLL_Init(enum frequency freq);
void ADCReadPot_Init(void);
void TimerADCTriger_Init(void);

// custom functions
void FSM(); // definition of the FSM
void makeButton(); // creates buttons on the LED display
void readButton(); // reads input from on-screen buttons
void ADC0SS3_Handler(void); // handler for ADC event

#endif //__LAB4T2A_H__