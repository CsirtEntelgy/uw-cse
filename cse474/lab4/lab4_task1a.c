/*
  Author: Young Bin Cho (Josh Cho) (1773497)
  Date: 07-30-2021

  This is my implementation of Lab4-Task1a.
  Populates the LED screen with a color.
  Performs as a test if all the setup is correct.
*/

#include "SSD2119_Display.h"

int main() {
  LCD_Init();
  LCD_ColorFill(Color4[4]);
  return 0;
}