# -*- coding: utf-8 -*-
"""
Created on Sat Oct 23 12:53:37 2021

@author: HP
"""
import random, numpy

#Function to treat a list as a stack, pop action over stack
def pop(stack):
    poped = stack[0]
    stack.remove(poped)
    return poped

#Function to treat a list as a stack, push action over stack
def push(stack, value):
    stack.insert(0, value)
    
#Function to treat a list as a stack, peek action over stack
def peek(stack):
    
    return stack[0]

#Function that given a maze represented as a rectangular matrix
#and the indexes of the start and end of itself (optional), decides
#if the maze is solvable.
#values in the maze matrix:
    # 0 if the cell does not bellong to the possible path
    # 1 if the cell is traversable
    # 2 if the cell has been traversed
    # 3 if the cell is on the successful path
def solvable_maze(maze_og, start=(0,1) , end=(None,None)):
    
    start_i, start_j=start
    end_i, end_j=end
    
    #Firstly we copy the maze, not to change it during reference
    maze = []
    for k in maze_og:
        prob = []
        for l in k:
            prob.append(l)
        maze.append(prob)
    
    
    #Set the end values by default, they depend on the size of the maze
    if end_i == None:
        end_i = len(maze)-1
    if end_j == None:
        end_j = len(maze[0])-2
    
    #if one index is not valid, the maze won't be checked
    if (len(maze) <= start_i or start_i < 0 or len(maze[0]) <= start_j or
    start_j < 0 or len(maze) <= end_i or end_i < 0 or len(maze[0]) <= end_j or end_j < 0):
        return -1
    
    #Set the start of the traverse
    i = start_i
    j = start_j
    
    #if the start or the end are not reachable, the maze is not solvable
    if not maze[i][j] or not maze[end_i][end_j]:
        return 0
    
    maze[i][j] = 2
    
    #initialize a stack to find a solution
    stack=[]
    push(stack, (i,j))
    
    #we start the seeking
    while stack:
        
        #we take the new idexes, it's known that we will always
        #pop indexes that belong to a traversable cell
        i,j = peek(stack)
        
        #if the peeked element was on the current path, we just mark it as checked
        #and remove it form the path, else, mark it in the current path
        if maze[i][j] == 3:
            pop(stack)
            maze[i][j] = 2
        else:
            maze[i][j] = 3
        
        #print for the state of the maze, can be easilly commented
        '''for k in maze:
            print(k,"\n")
        print("\n")'''
        
        #check if there is a solution
        if end_i == i and end_j ==j:
            return 1
        
        #now we check for posibilities
        #because the most of the times, we will start in (0,0)
        #and we will want to go to the opposite edge of the matrix
        #the order of the movements will give priority to the ones that
        # make the i or the j bigger
        if j-1 >= 0:
            if maze[i][j-1] == 1:
                push(stack, (i,j-1))
                                
        if i-1 >= 0:
               if maze[i-1][j] == 1:
                   push(stack, (i-1,j))
                   
        if i+1 < len(maze):
            if maze[i+1][j] == 1:
                push(stack, (i+1,j))
                                                        
        if j+1 < len(maze[0]):
           if maze[i][j+1] == 1:
               push(stack, (i,j+1))
            
    #No solution found     
    return 0
        
          
#Creates a random maze respecting some minimals
def create_maze(difficultly = 0, lines = None, cols = None):
    
    #it is impossible to have a negative difficultly
    if difficultly < 9:
        difficultly = 9
    
    #If lines and columns are not specified, we focus on difficultly for the size
    if lines == cols == None:
        size = random.randint(difficultly+5, difficultly+10)
        lines = size + random.randint(-4, 5)
        cols = size + random.randint(-4, 5)
            
    #If columns are specified, we create an square matrix
    elif lines == None:
        lines = cols
        
    #If lines are specified, we create a square matrix
    elif cols == None:
        cols=lines

    
    return random_maze(lines,cols)
    

# Find number of surrounding cells
def surroundingCells(rand_wall,maze):
	s_cells = 0
	if (maze[rand_wall[0]-1][rand_wall[1]] == 'c'):
		s_cells += 1
	if (maze[rand_wall[0]+1][rand_wall[1]] == 'c'):
		s_cells += 1
	if (maze[rand_wall[0]][rand_wall[1]-1] == 'c'):
		s_cells +=1
	if (maze[rand_wall[0]][rand_wall[1]+1] == 'c'):
		s_cells += 1

	return s_cells


## Main code
# Init variables
def random_maze(height, width):
    wall = 0
    cell = 1
    unvisited = -1
    maze = []
    
    
    
    # Denote all cells as unvisited
    for i in range(0, height):
    	line = []
    	for j in range(0, width):
    		line.append(unvisited)
    	maze.append(line)
    
    # Randomize starting point and set it a cell
    starting_height = int(random.random()*height)
    starting_width = int(random.random()*width)
    if (starting_height == 0):
    	starting_height += 1
    if (starting_height == height-1):
    	starting_height -= 1
    if (starting_width == 0):
    	starting_width += 1
    if (starting_width == width-1):
    	starting_width -= 1
    
    # Mark it as cell and add surrounding walls to the list
    maze[starting_height][starting_width] = cell
    walls = []
    walls.append([starting_height - 1, starting_width])
    walls.append([starting_height, starting_width - 1])
    walls.append([starting_height, starting_width + 1])
    walls.append([starting_height + 1, starting_width])
    
    # Denote walls in maze
    maze[starting_height-1][starting_width] = wall
    maze[starting_height][starting_width - 1] = wall
    maze[starting_height][starting_width + 1] = wall
    maze[starting_height + 1][starting_width] = wall
    
    while (walls):
    	# Pick a random wall
    	rand_wall = walls[int(random.random()*len(walls))-1]
    
    	# Check if it is a left wall
    	if (rand_wall[1] != 0):
    		if (maze[rand_wall[0]][rand_wall[1]-1] == unvisited and maze[rand_wall[0]][rand_wall[1]+1] == cell):
    			# Find the number of surrounding cells
    			s_cells = surroundingCells(rand_wall,maze)
    
    			if (s_cells < 2):
    				# Denote the new path
    				maze[rand_wall[0]][rand_wall[1]] = cell
    
    				# Mark the new walls
    				# Upper cell
    				if (rand_wall[0] != 0):
    					if (maze[rand_wall[0]-1][rand_wall[1]] != cell):
    						maze[rand_wall[0]-1][rand_wall[1]] = wall
    					if ([rand_wall[0]-1, rand_wall[1]] not in walls):
    						walls.append([rand_wall[0]-1, rand_wall[1]])
    
    
    				# Bottom cell
    				if (rand_wall[0] != height-1):
    					if (maze[rand_wall[0]+1][rand_wall[1]] != cell):
    						maze[rand_wall[0]+1][rand_wall[1]] = wall
    					if ([rand_wall[0]+1, rand_wall[1]] not in walls):
    						walls.append([rand_wall[0]+1, rand_wall[1]])
    
    				# Leftmost cell
    				if (rand_wall[1] != 0):	
    					if (maze[rand_wall[0]][rand_wall[1]-1] != cell):
    						maze[rand_wall[0]][rand_wall[1]-1] = wall
    					if ([rand_wall[0], rand_wall[1]-1] not in walls):
    						walls.append([rand_wall[0], rand_wall[1]-1])
    			
    
    			# Delete wall
    			for walll in walls:
    				if (walll[0] == rand_wall[0] and walll[1] == rand_wall[1]):
    					walls.remove(walll)
    
    			continue
    
    	# Check if it is an upper wall
    	if (rand_wall[0] != 0):
    		if (maze[rand_wall[0]-1][rand_wall[1]] == unvisited and maze[rand_wall[0]+1][rand_wall[1]] == cell):
    
    			s_cells = surroundingCells(rand_wall,maze)
    			if (s_cells < 2):
    				# Denote the new path
    				maze[rand_wall[0]][rand_wall[1]] =cell
    
    				# Mark the new walls
    				# Upper cell
    				if (rand_wall[0] != 0):
    					if (maze[rand_wall[0]-1][rand_wall[1]] != cell):
    						maze[rand_wall[0]-1][rand_wall[1]] = wall
    					if ([rand_wall[0]-1, rand_wall[1]] not in walls):
    						walls.append([rand_wall[0]-1, rand_wall[1]])
    
    				# Leftmost cell
    				if (rand_wall[1] != 0):
    					if (maze[rand_wall[0]][rand_wall[1]-1] != cell):
    						maze[rand_wall[0]][rand_wall[1]-1] = wall
    					if ([rand_wall[0], rand_wall[1]-1] not in walls):
    						walls.append([rand_wall[0], rand_wall[1]-1])
    
    				# Rightmost cell
    				if (rand_wall[1] != width-1):
    					if (maze[rand_wall[0]][rand_wall[1]+1] != cell):
    						maze[rand_wall[0]][rand_wall[1]+1] = wall
    					if ([rand_wall[0], rand_wall[1]+1] not in walls):
    						walls.append([rand_wall[0], rand_wall[1]+1])
    
    			# Delete wall
    			for walll in walls:
    				if (walll[0] == rand_wall[0] and walll[1] == rand_wall[1]):
    					walls.remove(walll)
    
    			continue
    
    	# Check the bottom wall
    	if (rand_wall[0] != height-1):
    		if (maze[rand_wall[0]+1][rand_wall[1]] == unvisited and maze[rand_wall[0]-1][rand_wall[1]] == cell):
    
    			s_cells = surroundingCells(rand_wall,maze)
    			if (s_cells < 2):
    				# Denote the new path
    				maze[rand_wall[0]][rand_wall[1]] = cell
    
    				# Mark the new walls
    				if (rand_wall[0] != height-1):
    					if (maze[rand_wall[0]+1][rand_wall[1]] != cell):
    						maze[rand_wall[0]+1][rand_wall[1]] = wall
    					if ([rand_wall[0]+1, rand_wall[1]] not in walls):
    						walls.append([rand_wall[0]+1, rand_wall[1]])
    				if (rand_wall[1] != 0):
    					if (maze[rand_wall[0]][rand_wall[1]-1] != cell):
    						maze[rand_wall[0]][rand_wall[1]-1] = wall
    					if ([rand_wall[0], rand_wall[1]-1] not in walls):
    						walls.append([rand_wall[0], rand_wall[1]-1])
    				if (rand_wall[1] != width-1):
    					if (maze[rand_wall[0]][rand_wall[1]+1] != cell):
    						maze[rand_wall[0]][rand_wall[1]+1] = wall
    					if ([rand_wall[0], rand_wall[1]+1] not in walls):
    						walls.append([rand_wall[0], rand_wall[1]+1])
    
    			# Delete wall
    			for walll in walls:
    				if (walll[0] == rand_wall[0] and walll[1] == rand_wall[1]):
    					walls.remove(walll)
    
    
    			continue
    
    	# Check the right wall
    	if (rand_wall[1] != width-1):
    		if (maze[rand_wall[0]][rand_wall[1]+1] == unvisited and maze[rand_wall[0]][rand_wall[1]-1] == cell):
    
    			s_cells = surroundingCells(rand_wall,maze)
    			if (s_cells < 2):
    				# Denote the new path
    				maze[rand_wall[0]][rand_wall[1]] = cell
    
    				# Mark the new walls
    				if (rand_wall[1] != width-1):
    					if (maze[rand_wall[0]][rand_wall[1]+1] != cell):
    						maze[rand_wall[0]][rand_wall[1]+1] = wall
    					if ([rand_wall[0], rand_wall[1]+1] not in walls):
    						walls.append([rand_wall[0], rand_wall[1]+1])
    				if (rand_wall[0] != height-1):
    					if (maze[rand_wall[0]+1][rand_wall[1]] != cell):
    						maze[rand_wall[0]+1][rand_wall[1]] = wall
    					if ([rand_wall[0]+1, rand_wall[1]] not in walls):
    						walls.append([rand_wall[0]+1, rand_wall[1]])
    				if (rand_wall[0] != 0):	
    					if (maze[rand_wall[0]-1][rand_wall[1]] != cell):
    						maze[rand_wall[0]-1][rand_wall[1]] = wall
    					if ([rand_wall[0]-1, rand_wall[1]] not in walls):
    						walls.append([rand_wall[0]-1, rand_wall[1]])
    
    			# Delete wall
    			for walll in walls:
    				if (walll[0] == rand_wall[0] and walll[1] == rand_wall[1]):
    					walls.remove(walll)
    
    			continue
    
    	# Delete the wall from the list anyway
    	for walll in walls:
    		if (walll[0] == rand_wall[0] and walll[1] == rand_wall[1]):
    			walls.remove(walll)
    	
    
    
    # Mark the remaining unvisited cells as walls
    for i in range(0, height):
    	for j in range(0, width):
    		if (maze[i][j] == unvisited):
    			maze[i][j] = wall
    
    # Set entrance and exit
    for i in range(0, width):
    	if (maze[1][i] == cell):
            maze[0][i] = cell
            start=(0,i)
            break
    
    for i in range(width-1, 0, -1):
    	if (maze[height-2][i] == cell):
            maze[height-1][i] = cell
            end=(height-1,i)
            break
    
    return maze,start,end
            


    
    
    
    
    
    
    
    
    
    
    
    
    