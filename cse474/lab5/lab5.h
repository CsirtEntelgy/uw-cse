/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 08-03-2021
  
  Header file for Lab5.
  Contains addresses for the used registers.
*/

#ifndef __LAB5_H__
#define __LAB5_H__

// addresses for timer0A
#define RCGCTIMER (*((volatile uint32_t *)0x400FE604))
#define GPTMCTL (*((volatile uint32_t *)0x4003000C))
#define GPTMCFG (*((volatile uint32_t *)0x40030000))
#define GPTMTAMR (*((volatile uint32_t *)0x40030004))
#define GPTMTAILR (*((volatile uint32_t *)0x40030028))
#define GPTMADCEV (*((volatile uint32_t *)0x40030070))

// addresses for ADC0
#define RCGCADC (*((volatile uint32_t *)0x400FE638))
#define ADCCC (*((volatile uint32_t *)0x40038FC8))
#define GPIOAFSEL (*((volatile uint32_t *)0x4005C420))    // E based
#define GPIODEN_E (*((volatile uint32_t *)0x4005C51C))    // E based
#define GPIOAMSEL_E (*((volatile uint32_t *)0x4005C528))  // E based
#define ADCACTSS (*((volatile uint32_t *)0x40038000))
#define ADCEMUX (*((volatile uint32_t *)0x40038014))
#define ADCSSMUX3 (*((volatile uint32_t *)0x400380A0))
#define ADCSSCTL3 (*((volatile uint32_t *)0x400380A4))
#define ADCIM (*((volatile uint32_t *)0x40038008))
#define NVIC_EN0 (*((volatile uint32_t *)0xE000E100))     // interrupt 17
#define ADCISC (*((volatile uint32_t *)0x4003800C))
#define ADCSSFIFO3 (*((volatile uint32_t *)0x400380A8))

// functional definitions
void LED_Init(void);                                      // Init the LED4 (PF0)
void PWM_Init(void);                                   // Init the PWM0, module0
void PLL_Init(void);                                   // Init the PLL
void ADCReadPot_Init(void);                               // Init the ADC
void TimerADCTriger_Init(void);                           // Init the timer0
void ADC0SS3_Handler(void);                               // Handler for interrupt

#endif //__LAB5_H__