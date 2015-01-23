/*
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: sandpile.c,v 1.1 2001/03/02 15:34:13 sirna Exp $
 * $Revision: 1.1 $
 * $Date: 2001/03/02 15:34:13 $
 *
 * Simple tool for generating sandpile-avalnce data.
 * 
 * Compile: cc -lm -o sandpile sandpile.c
 */


#include <stdio.h>
#include <stdlib.h>
#include <math.h>

//returns a random number between 0 (inclusive) and R (exclusive)
#define RANDOM(R) ( (int)floor(R*(rand()/(double)RAND_MAX)) )

#define TRUE 1
#define FALSE 0


int *sandpile;
int avalanche_count;
int pile_size;

//drops one grain 
void drop_grain(int x, int y);
//avalanche in a cell
void avalanche(int x, int y);

int main(int argc, char *argv[]){
  int i;
  int runlength;
  int seed; 
  
  if (argc != 4){
    printf("Usage: sandpile <pile_size> <runlength> <random seed>\n");
    return -1;
  }
  
  pile_size = atoi(argv[1]); 
  runlength =  atoi(argv[2]);  
  seed =  atoi(argv[3]);   
  sandpile = malloc(pile_size * pile_size * sizeof(int));

  if (sandpile == NULL){
    printf("Couldn't allocate memory!");
    return -1;
  }
  srand(seed);
  for (i = 0; i < pile_size * pile_size; i ++){
    *(sandpile + i) = 0;

  }

  for (i = 0; i < runlength; i++){
    avalanche_count = 0;
    drop_grain(RANDOM(pile_size), RANDOM(pile_size));
    printf("%d %d \n", i,avalanche_count ); 
  }
  free(sandpile);
  return 0;
}


void drop_grain(int x, int y){
  if (x < 0 || y < 0 || x>= pile_size || y >=pile_size){
    return; //since we are not on the plate
  }
  if (( (*(sandpile + (x * pile_size) + y))++) >= 4){
    avalanche(x,y);
  }
}

void avalanche(int x,int y){

  *(sandpile + (x * pile_size) + y) -= 4;

  avalanche_count++;
  drop_grain(x+1,y);
  drop_grain(x-1,y);
  drop_grain(x,y-1);
  drop_grain(x,y+1);


}
