
#define WALL    0
#define CELL    1
#define UNVISITED   -1

struct coord{
    int i;
    int j;
};
typedef struct coord coord;

struct coord_stack{
    coord coords;
    coord_stack *next;
};
typedef  struct coord_stack coord_stack;

typedef struct maze_pack{
    int *maze;
    int lines;
    int cols;
    coord start;
    coord end;
}maze_pack;