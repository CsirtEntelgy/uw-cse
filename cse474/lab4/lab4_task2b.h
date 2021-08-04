/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-30-2021
  
  Header file for Lab4_task2b.
  Contains addresses for the used registers.
*/

#ifndef __LAB4T2B_H__
#define __LAB4T2B_H__

// given functions & definitions
enum frequency {PRESET1 = 120, PRESET2 = 60, PRESET3 = 12};
int PLL_Init(enum frequency freq);

#endif //__LAB4T2B_H__