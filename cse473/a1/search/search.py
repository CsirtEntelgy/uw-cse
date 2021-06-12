import util

class SearchProblem:

    def getStartState(self):
        util.raiseNotDefined()

    def isGoalState(self, state):
        util.raiseNotDefined()

    def getSuccessors(self, state):
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        util.raiseNotDefined()


def tinyMazeSearch(problem):
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return  [s, s, w, s, w, w, s, w]

def depthFirstSearch(problem):
    return searchHelper(problem, 0)

def breadthFirstSearch(problem):
    return searchHelper(problem, 1)

def searchHelper(problem, sOrq):
    #determine dfs or bfs (0 for dfs)
    if sOrq == 0:
        temp = util.Stack()
    else:
        temp = util.Queue()
    
    #push start & create list of checked
    temp.push((problem.getStartState(), [], 0))
    checked = []
    
    while(not temp.isEmpty()):
        state, loc, count = temp.pop()
        
        if problem.isGoalState(state):
            return loc #terminate on goal
        
        if state in checked:
            continue #continue on current node exhaustion
            
        for stateNext, locNext, countNext in problem.getSuccessors(state):
            temp.push((stateNext, loc + [locNext], count + countNext)) #add next node to explore
            
        checked.append(state) #add to checked list
    
    return None #goal failed

def uniformCostSearch(problem):
    return aStarSearch(problem, nullHeuristic)

def nullHeuristic(state, problem=None):
    return 0

def aStarSearch(problem, heuristic=nullHeuristic):
    #get prio queue, push start, create list of checked
    start = problem.getStartState()
    temp = util.PriorityQueue()
    temp.push((start, [], 0), heuristic(start, problem))
    checked = []
    
    while(not temp.isEmpty()):
        state, loc, count = temp.pop()
        
        if problem.isGoalState(state):
            return loc #terminate on goal
        
        if state in checked:
            continue #continue on current node exhaustion
            
        for stateNext, locNext, countNext in problem.getSuccessors(state):
            tempCount = count + countNext
            temp.push((stateNext, loc + [locNext], tempCount), tempCount + heuristic(stateNext, problem)) #add next node to explore
            
        checked.append(state) #add to checked list
    
    return None #goal failed

# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch