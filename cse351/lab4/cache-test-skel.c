/*
CSE 351 Lab 4, Part 1 - Mystery Caches

Name: Young Bin Cho (Josh Cho)
UW_ID: joshua97 (1773497)
*/

#include <stdlib.h>
#include <stdio.h>

#include "support/mystery-cache.h"

/*
 * NOTE: When using access_cache() you do not need to provide a "real" memory
 * addresses. You can use any convenient integer value as a memory address,
 * you should not be able to cause a segmentation fault by providing a memory
 * address out of your programs address space as the argument to access_cache.
 */

/*
   Returns the size (in B) of each block in the cache.
*/
int get_block_size(void) {
  //setting local variables
  int counter = 0;
  //accessing cache with custom value 0
  access_cache(0);
  //loop
  while(access_cache(counter)){
   counter++;
  }
  //return block size
  return counter;
}

/*
   Returns the size (in B) of the cache.
*/
int get_cache_size(int block_size) {
  //setting local variables
  int counter = block_size;
  int cacheSize = 0;
  //flushing and accessing cache with custom value 0
  flush_cache();
  access_cache(0);
  //loop
  while(access_cache(0)){
   //resetting the variabels
   cacheSize = block_size;
   //loop over known area until unknown area is reached
   while(cacheSize <= counter){
      cacheSize += block_size;
      access_cache(cacheSize);
   }
   counter += block_size;
  }
  //return cache size
  return cacheSize;
}

/*
   Returns the associativity of the cache.
*/
int get_cache_assoc(int cache_size) {
  //setting local variables
  int counter = 0;
  int counterCache = 1;
  int assoc = 0;
  //flushing and accessing cache with custom value 0
  flush_cache();
  access_cache(0);
  //loop
  while(access_cache(0)){
   //resetting the variables
   counter = cache_size;
   assoc = 0;
   //loop over known area until unknown area is reached
   while(counter <= counterCache){
      counter += cache_size;
      assoc++;
      access_cache(counter);
   }
   counterCache += cache_size;
  }
  //return associativity
  return assoc;
}

int main(void) {
  int size;
  int assoc;
  int block_size;

  cache_init(0, 0);

  block_size = get_block_size();
  size = get_cache_size(block_size);
  assoc = get_cache_assoc(size);

  printf("Cache block size: %d bytes\n", block_size);
  printf("Cache size: %d bytes\n", size);
  printf("Cache associativity: %d\n", assoc);

  return EXIT_SUCCESS;
}
