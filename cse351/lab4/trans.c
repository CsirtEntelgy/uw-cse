/*
CSE 351 Lab 4, Part 2 - Matrix Transposes
Name: Young Bin Cho (Josh Cho)
UW_ID: joshua97 (1773497)
*/

/* 
 * trans.c - Natrix transpose B = A^T
 *
 * Each transpose function must have a prototype of the form:
 * void trans(int M, int N, int A[M][N], int B[N][M]);
 *
 * A transpose function is evaluated by counting the number of misses
 * on a 1 KiB direct mapped cache with a block size of 32 bytes.
 */ 
#include <stdio.h>
#include "support/cachelab.h"

int is_transpose(int M, int N, int A[M][N], int B[N][M]);

/* 
 * transpose_submit - This is the solution transpose function that you
 *     will be graded on for Part I of the assignment. Do not change
 *     the description string "Transpose submission", as the driver
 *     searches for that string to identify the transpose function to
 *     be graded. 
 */
char transpose_submit_desc[] = "Transpose submission";
void transpose_submit(int M, int N, int A[M][N], int B[N][M]) {
   //Local Variables: 3 counters for xAxis, yAxis, and transpose
   //+ up to 8 temp variables depending on custom block size
   int counter1, counter2, counter3;
   int temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8;
   //Case of 32x32
   if(M == 32){
      //Iterate over x and y axis
      for(counter1 = 0; counter1 < M; counter1 += 8){
         for(counter2 = 0; counter2 < N; counter2 += 8){
            //transpose
            for(counter3 = counter1; counter3 < counter1 + 8; counter3++){
	            //set local variables (8) from A
               temp1 = A[counter3][counter2];
               temp2 = A[counter3][counter2+1];
               temp3 = A[counter3][counter2+2];
               temp4 = A[counter3][counter2+3];
               temp5 = A[counter3][counter2+4];
               temp6 = A[counter3][counter2+5];
               temp7 = A[counter3][counter2+6];
               temp8 = A[counter3][counter2+7];
               //set local variables (8) to B (with transpose)
               B[counter2][counter3] = temp1;
               B[counter2+1][counter3] = temp2;
               B[counter2+2][counter3] = temp3;
               B[counter2+3][counter3] = temp4;
               B[counter2+4][counter3] = temp5;
               B[counter2+5][counter3] = temp6;
               B[counter2+6][counter3] = temp7;
               B[counter2+7][counter3] = temp8;
            }
         }
      }
   //Case of 64x64
   }else if (M == 64){
      //Iterate over x and y axis
      for(counter1 = 0; counter1 < M; counter1 += 4){
         for(counter2 = 0; counter2 < N; counter2 += 4){
            //transpose
            for(counter3 = counter1; counter3 < counter1 + 4; counter3++){
	            //set local variables (4) from A
               temp1 = A[counter3][counter2];
               temp2 = A[counter3][counter2+1];
               temp3 = A[counter3][counter2+2];
               temp4 = A[counter3][counter2+3];
	            //set local variables (8) to B (with transpose)
               B[counter2][counter3] = temp1;
               B[counter2+1][counter3] = temp2;
               B[counter2+2][counter3] = temp3;
               B[counter2+3][counter3] = temp4;
            }
         }
      }
   }
}

/* 
 * You can define additional transpose functions below. We've defined
 * a simple one below to help you get started. 
 */ 

/* 
 * trans - A simple baseline transpose function, not optimized for the cache.
 */
char trans_desc[] = "Simple row-wise scan transpose";
void trans(int M, int N, int A[M][N], int B[N][M]) {
    int i, j, tmp;

    for (i = 0; i < M; i++) {
        for (j = 0; j < N; j++) {
            tmp = A[i][j];
            B[j][i] = tmp;
        }
    }

}

/*
 * registerFunctions - This function registers your transpose
 *     functions with the driver.  At runtime, the driver will
 *     evaluate each of the registered functions and summarize their
 *     performance. This is a handy way to experiment with different
 *     transpose strategies.
 */
void registerFunctions() {
    /* Register your solution function */
    registerTransFunction(transpose_submit, transpose_submit_desc);

    /* Register any additional transpose functions */
    registerTransFunction(trans, trans_desc);

}

/* 
 * is_transpose - This helper function checks if B is the transpose of
 *     A. You can check the correctness of your transpose by calling
 *     it before returning from the transpose function.
 */
int is_transpose(int M, int N, int A[M][N], int B[N][M]) {
    int i, j;

    for (i = 0; i < M; i++) {
        for (j = 0; j < N; ++j) {
            if (A[i][j] != B[j][i]) {
                return 0;
            }
        }
    }
    return 1;
}

