/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-09-2021
  
  Header file for Lab2_task1a.
  Contains addresses for the used registers.
*/

#ifndef __HEADER1_H__
#define __HEADER1_H__

#define RCGCGPIO (*((volatile uint32_t *)0x400FE608))

#define GPIODIR_F (*((volatile uint32_t *)0x4005D400))
#define GPIODEN_F (*((volatile uint32_t *)0x4005D51C))
#define GPIODATA_F (*((volatile uint32_t *)0x4005D3FC))

#define GPIODIR_N (*((volatile uint32_t *)0x40064400))
#define GPIODEN_N (*((volatile uint32_t *)0x4006451C))
#define GPIODATA_N (*((volatile uint32_t *)0x400643FC))

#define RCGCTIMER (*((volatile uint32_t *)0x400FE604))
#define GPTMCTL (*((volatile uint32_t *)0x4003000C)) // timer 0 based
#define GPTMCFG (*((volatile uint32_t *)0x40030000)) // timer 0 based
#define GPTMTAMR (*((volatile uint32_t *)0x40030004)) // timer 0 based
#define GPTMTAILR (*((volatile uint32_t *)0x40030028)) // timer 0 based
#define GPTMRIS (*((volatile uint32_t *)0x4003001C)) // timer 0 based
#define GPTMICR (*((volatile uint32_t *)0x40030024)) // timer 0 based

#endif //__HEADER1_H__