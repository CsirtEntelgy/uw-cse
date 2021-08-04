/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-22-2021
  
  Header file for Lab3_task1a.
  Contains addresses for the used registers.
*/

#ifndef __LAB3T1A_H__
#define __LAB3T1A_H__

// addresses for the on-board LEDs
#define RCGCGPIO (*((volatile uint32_t *)0x400FE608))
#define GPIODIR_F (*((volatile uint32_t *)0x4005D400))
#define GPIODEN_F (*((volatile uint32_t *)0x4005D51C))
#define GPIODATA_F (*((volatile uint32_t *)0x4005D3FC))
#define GPIODIR_N (*((volatile uint32_t *)0x40064400))
#define GPIODEN_N (*((volatile uint32_t *)0x4006451C))
#define GPIODATA_N (*((volatile uint32_t *)0x400643FC))

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
#define GPIOAFSEL (*((volatile uint32_t *)0x4005C420)) // E based
#define GPIODEN_E (*((volatile uint32_t *)0x4005C51C)) // E based
#define GPIOAMSEL_E (*((volatile uint32_t *)0x4005C528)) // E based
#define ADCACTSS (*((volatile uint32_t *)0x40038000))
#define ADCEMUX (*((volatile uint32_t *)0x40038014))
#define ADCSSMUX3 (*((volatile uint32_t *)0x400380A0))
#define ADCSSCTL3 (*((volatile uint32_t *)0x400380A4))
#define ADCIM (*((volatile uint32_t *)0x40038008))
#define NVIC_EN0 (*((volatile uint32_t *)0xE000E100)) // interrupt 17
#define ADCISC (*((volatile uint32_t *)0x4003800C))
#define ADCSSFIFO3 (*((volatile uint32_t *)0x400380A8))

// given functions & enum for frequencies
enum frequency {PRESET1 = 120, PRESET2 = 60, PRESET3 = 12};
int PLL_Init(enum frequency freq);
void LED_Init(void);
void ADCReadPot_Init(void);
void TimerADCTriger_Init(void);

#endif //__LAB3T1A_H__