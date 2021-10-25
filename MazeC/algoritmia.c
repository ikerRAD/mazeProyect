//
// Created by ikerb on 25/10/2021.
//
#include <stdio.h>
#include <stdlib.h>
#include "definitions.h"


/*pop function for coord stack*/
coord pop(coord_stack **stack){
        coord_stack *tmp = *stack;
        coord popped = (*stack)->coords;
        *stack = stack->next;
        free(tmp);
        return popped;
}

/*push function for coord stack*/
void push(coord_stack **stack, coord value){
    coord_stack *new_head = (coord_stack *)malloc(sizeof(coord_stack));
    new_head->coords.i = value.i;
    new_head->coords.j = value.j;
    new_head->next = *stack;
    *stack = new_head;
}

/*peek function for coord stack*/
coord peek(coord_stack *stack){
        return stack->coords;
}

/*Function to check if a maze has solution*/
int solvable_maze(maze_pack mazePack){
    int start_i, start_j, end_i, end_j;

    start_i = mazePack.start.i;
    start_j = mazePack.start.j;
    end_i = mazePack.end.i;
    end_j = mazePack.end.j;

    int *maze = malloc(sizeof(int)*mazePack.cols*mazePack.lines);

    //Copying the maze not to modify the original
    for(int i=0; i<mazePack.lines; i++){
        for(int j=0, j<mazePack.cols; j++){
            maze[mazePack.lines*i+j] = mazePack.maze[mazePack.lines*i+j];
        }
    }

    //If the indexes are wrong, exit and throw an error
    if (mazePack.lines <= start_i || start_i < 0 || mazePack.cols <= start_j || start_j < 0
        || mazePack.lines <= end_i || end_i < 0 || mazePack.cols <= end_j || end_j < 0){
        return -1;
    }

    int i = start_i, j = start_j;
    //if there is no way in the exit or in the start, return that the maze is not solvable
    if (maze[i*mazePack.lines+j] == 0 || maze[end_i*mazePack.lines+j] == 0){
        return 0;
    }
    //initialize the search
    maze[i*mazePack.lines+j] = 2;

    coord_stack *stack = (coord_stack *)malloc(sizeof(coord_stack));
    stack->coords = mazePack.start;
    stack->next = 0;

    //we start the main process
    while (1){

        coord current = peek(stack);

        //if the peeked element was on the current path, we just mark it as checked
        //and remove it form the path, else, mark it in the current path
        if (maze[current.i*mazePack.lines+current.j] == 3){
            pop(&stack);
            maze[current.i*mazePack.lines+current.j] = 2;
            //if we are back to the start, the stack is empty, there is no solution
            if(current.i == start_i && current.j == start_j){
                return 0;
            }
        }else{
            maze[current.i*mazePack.lines+current.j] = 3;
        }

        if(end_i == current.i && end_j == current.j){
            return 1;
        }

        //now we push the possible movements
        if(current.j-1>=0){
            if(maze[current.i*mazePack+current.j-1] == 1){
                coord instert = (coord){.i = current.i, .j = current.j-1};
                push(&stack, instert);
            }
        }
        if(current.i-1>=0){
            if(maze[(current.i-1)*mazePack+current.j] == 1){
                coord instert = (coord){.i = current.i-1, .j = current.j};
                push(&stack, instert);
            }
        }
        if(current.j+1<mazePack.lines){
            if(maze[(current.i+1)*mazePack+current.j] == 1){
                coord instert = (coord){.i = current.i+1, .j = current.j};
                push(&stack, instert);
            }
        }
        if(current.j+1<mazePack.cols){
            if(maze[current.i*mazePack+current.j+1] == 1){
                coord instert = (coord){.i = current.i, .j = current.j+1};
                push(&stack, instert);
            }
        }

    }

}

/*Function to generate a random number between the fields*/
int random_number(int min_num, int max_num)
{
    int result = 0, low_num = 0, hi_num = 0;

    if (min_num < max_num)
    {
        low_num = min_num;
        hi_num = max_num + 1; // include max_num in output
    } else {
        low_num = max_num + 1; // include max_num in output
        hi_num = min_num;
    }

    srand(time(NULL));
    result = (rand() % (hi_num - low_num)) + low_num;
    return result;
}
//Deletes a wall from the list
void delete_wall(coord *walls, coord wall, int *nwalls){
    int deleted = 0;
    for(int i=0; i<*nwalls && deleted == 0; i++){
        if(walls[i].i == wall.i && walls[i].j == wall.j){
            deleted = 1;
            for(int j=i+1;j<*nwalls;j++){
                walls[j-1] = walls[j];
            }
        }
    }
    *nwalls = *nwalls - 1;
}
//returns 1 if the element is NOT in the list
int not_in(coord wall, coord *walls, int nwalls){
    for(int i = 0; i<nwalls; i++){
        if(wall.i == walls[i].i && wall.j == walls[i].j){
            return 0;
        }
    }
    return 1;
}

//returns how many cells surround a wall
int surrounding_cells(coord rand_wall, maze_pack maze){

    int cells = 0;
    if(maze.maze[(rand_wall.i-1)*maze.lines+rand_wall.j] == CELL){
        cells++;
    }
    if(maze.maze[(rand_wall.i+1)*maze.lines+rand_wall.j] == CELL){
        cells++;
    }
    if(maze.maze[rand_wall.i*maze.lines+rand_wall.j-1] == CELL){
        cells++;
    }
    if(maze.maze[rand_wall.i*maze.lines+rand_wall.j+1] == CELL){
        cells++;
    }

    return cells;

}

//Function to create a random maze
maze_pack create_maze(int lines, int cols){

    maze_pack maze;
    maze.lines = lines;
    maze.cols = cols;
    maze.maze = malloc(sizeof(int)*lines*cols);

    int i,j;

    //initializing the maze
    for(i=0;i<lines;i++){
        for(j=0;j<cols;j++){
            maze.maze[i*lines+j] = UNVISITED;
        }
    }

    //Randomize the starting point
    int starting_i = random_number(1,lines-2);
    int starting_j = random_number(1,cols-2);

    //First step
    maze.maze[lines*starting_i+starting_j] = CELL;
    coord walls[lines*cols];
    int nwalls = 4;
    walls[0] = (coord){.i = starting_i-1, .j = starting_j};
    walls[1] = (coord){.i = starting_i, .j = starting_j-1};
    walls[2] = (coord){.i = starting_i, .j = starting_j+1};
    walls[3] = (coord){.i = starting_i+1, .j = starting_j};

    maze.maze[starting_i*lines+starting_j-1] = WALL;
    maze.maze[starting_i*lines+starting_j+1] = WALL;
    maze.maze[(starting_i+1)*lines+starting_j] = WALL;
    maze.maze[(starting_i-1)*lines+starting_j] = WALL;

    while(nwalls!=0){
        //pick a random wall
        coord rand_wall = walls[random_number(0,nwalls-1)];

        int flag = 0;
        if(rand_wall[1] != 0){
            if(maze.maze[rand_wall[0]*lines+rand_wall[1]-1] == UNVISITED && maze.maze[rand_wall[0]*lines+rand_wall[1]+1] == CELL){
                // flag = 1 if we won't check the rightmost cell
                flag = 1;
            }
        }else if(rand_wall[0] != 0){
            if(maze.maze[(rand_wall[0]-1)*lines+rand_wall[1]-1] == UNVISITED && maze.maze[(rand_wall[0]+1)*lines+rand_wall[1]+1] == CELL){
                // flag = 2 if we won't check the bottom cell
                flag = 2;
            }
        }else if(rand_wall[0] != lines-1){
            if(maze.maze[(rand_wall[0]+1)*lines+rand_wall[1]-1] == UNVISITED && maze.maze[(rand_wall[0]-1)*lines+rand_wall[1]+1] == CELL){
                // flag = 3 if we won't check the upper cell
                flag = 3;
            }
        }else if(rand_wall[1] != cols-1){
            if(maze.maze[rand_wall[0]*lines+rand_wall[1]+1] == UNVISITED && maze.maze[rand_wall[0]*lines+rand_wall[1]-1] == CELL){
                // flag = 4 if we won't check the leftmost cell
                flag = 4;
            }
        }

        if(flag != 0){
            int ncells = surrounding_cells(rand_wall, maze);
            if(ncells<2){

                maze.maze[rand_wall.i*lines+rand_wall.j] = CELL;

                if(flag != 1){
                    //rightmost
                    if(rand_wall.j != cols-1){
                        if(maze.maze[rand_wall.i*lines+rand_wall.j+1] != CELL){
                            maze.maze[rand_wall.i*lines+rand_wall.j+1] = WALL;
                        }
                        coord check = (coord){.i = rand_wall.i, .j = rand_wall.j+1};
                        if(1 == not_in(check, walls, nwalls)){
                            walls[nwalls] = check;
                            nwalls++;
                        }
                    }
                }
                if(flag != 2){
                    //bottom
                    if(rand_wall.i != lines-1){
                        if(maze.maze[(rand_wall.i+1)*lines+rand_wall.j] != CELL){
                            maze.maze[(rand_wall.i+1)*lines+rand_wall.j] = WALL;
                        }
                        coord check = (coord){.i = rand_wall.i+1, .j = rand_wall.j};
                        if(1 == not_in(check, walls, nwalls)){
                            walls[nwalls] = check;
                            nwalls++;
                        }
                    }
                }
                if(flag != 3){
                    //upper
                    if(rand_wall.i != 0){
                        if(maze.maze[(rand_wall.i-1)*lines+rand_wall.j] != CELL){
                            maze.maze[(rand_wall.i-1)*lines+rand_wall.j] = WALL;
                        }
                        coord check = (coord){.i = rand_wall.i-1, .j = rand_wall.j};
                        if(1 == not_in(check, walls, nwalls)){
                            walls[nwalls] = check;
                            nwalls++;
                        }
                    }
                }
                if(flag != 4){
                    //leftmost
                    if(rand_wall.j != 0){
                        if(maze.maze[rand_wall.i*lines+rand_wall.j-1] != CELL){
                            maze.maze[rand_wall.i*lines+rand_wall.j-1] = WALL;
                        }
                        coord check = (coord){.i = rand_wall.i, .j = rand_wall.j-1};
                        if(1 == not_in(check, walls, nwalls)){
                            walls[nwalls] = check;
                            nwalls++;
                        }
                    }
                }
            }
        }

        delete_wall(walls, rand_wall, &nwalls);


    }

    //mark all the unvisited cells as walls
    for(i=0;i<lines;i++){
        for(j=0;j<cols;j++){
            if(maze.maze[i*lines+j] == UNVISITED){
                maze.maze[i*lines+j] = WALL;
            }
        }
    }

    coord start = (coord){.i=0,.j=0};
    coord end = (coord){.i=lines-1,.j=0};

    //set entrance and exit
    for(i=0; i<cols; i++){
        if(maze.maze[lines+i] == CELL){
            maze.maze[i] = CELL;
            start.j = i;
            break;
        }
    }

    for(i=cols-1;i>=0;i--){
        if(maze.maze[(lines-2)*lines+i] == CELL){
            maze.maze[(lines-1)*lines+i] = CELL;
            end.j = i;
        }
    }

    maze.start = start;
    maze.end = end;

    return maze;

}
























