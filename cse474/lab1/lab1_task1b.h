/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-05-2021
  
  Header file for Lab1_task1a.
  Contains addresses for the used registers.
*/

#ifndef __HEADER1_H__
#define __HEADER1_H__

#define RCGCGPIO (*((volatile uint32_t *)0x400FE608))

#define GPIODIR_J (*((volatile uint32_t *)0x40060400))
#define GPIODEN_J (*((volatile uint32_t *)0x4006051C))
#define GPIOPUR_J (*((volatile uint32_t *)0x40060510))
#define GPIODATA_J (*((volatile uint32_t *)0x400603FC))

#define GPIODIR_N (*((volatile uint32_t *)0x40064400))
#define GPIODEN_N (*((volatile uint32_t *)0x4006451C))
#define GPIODATA_N (*((volatile uint32_t *)0x400643FC))

#endif //__HEADER1_H__