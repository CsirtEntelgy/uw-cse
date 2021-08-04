/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-05-2021
  
  Header file for Lab1_task1a.
  Contains addresses for the used registers.
*/

#ifndef __HEADER1_H__
#define __HEADER1_H__

#define RCGCGPIO (*((volatile uint32_t *)0x400FE608))

#define GPIOAMSEL_L (*((volatile uint32_t *)0x40062528))
#define GPIOAFSEL_L (*((volatile uint32_t *)0x40062420))
#define GPIODIR_L (*((volatile uint32_t *)0x40062400))
#define GPIODEN_L (*((volatile uint32_t *)0x4006251C))
#define GPIODATA_L (*((volatile uint32_t *)0x400623FC))

#endif //__HEADER1_H__