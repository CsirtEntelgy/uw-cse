# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
#
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"

        if action == 'STOP':
            return float('-inf')
        if successorGameState.isWin():
            return float('inf')

        score = 0

        #capsule score
        score += 5/(self.getScoreFor(currentGameState.getCapsules(), newPos, 'min')[0])

        #food score
        old = self.getScoreFor(currentGameState.getFood().asList(), newPos, '')
        score += 1/(old[2])
        score += 2/(old[1])
        score += 4/(old[0])

        score += 5/(self.getScoreFor(newFood.asList(), newPos, 'min')[0])

        #ghost score
        scared = [ghost.getPosition() for ghost in currentGameState.getGhostStates() if ghost.scaredTimer == 0]
        if len(scared) != 0:
            score -= 12**(3 - min(self.getScoreFor(scared, newPos, 'min')[0], 3))

        #buffer for score
        if successorGameState.getNumFood() < currentGameState.getNumFood():
            score += 50
        if successorGameState.getPacmanPosition() in currentGameState.getCapsules():
            score += 60

        return score

    def getScoreFor(self, list, newPos, indice):
        temp = [manhattanDistance(i, newPos) for i in list]
        if len(temp) == 0:
            return [1.0, 1.0, 1.0]
        if indice == 'max':
            return [1.0, max(max(temp), 1.0), 1.0]
        if indice == 'min':
            return [max(min(temp), 1.0), 1.0, 1.0]
        return [max(min(temp), 1.0), max(max(temp), 1.0), sum(temp)/len(temp)]

def scoreEvaluationFunction(currentGameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"
        return self.maxVal(gameState, 0)[1]

    def minVal(self, agentNo, gameState, depth):

        if gameState.getNumAgents() <= agentNo:
            return self.maxVal(gameState, depth + 1)

        if(gameState.isWin() or gameState.isLose() or self.depth == depth):
            return (self.evaluationFunction(gameState), 'None')

        temp = {}
        for action in gameState.getLegalActions(agentNo):
            i = self.minVal(agentNo + 1, gameState.generateSuccessor(agentNo, action), depth)[0]
            temp[i] = action

        return min(temp.keys()), temp[min(temp.keys())]

    def maxVal(self, gameState, depth):

        if(gameState.isWin() or gameState.isLose() or self.depth == depth):
            return (self.evaluationFunction(gameState), 'None')

        temp = {}
        for action in gameState.getLegalActions(0):
            i = self.minVal(1, gameState.generateSuccessor(0, action), depth)[0]
            temp[i] = action

        return max(temp.keys()), temp[max(temp.keys())]

class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"
        return self.maxVal(gameState, 0, float('-inf'), float('inf'))[1]

    def minVal(self, agentNo, gameState, depth, fromPoint, toPoint):

        if gameState.getNumAgents() <= agentNo:
            return self.maxVal(gameState, depth + 1, fromPoint, toPoint)

        if(gameState.isWin() or gameState.isLose() or self.depth == depth):
            return (self.evaluationFunction(gameState), 'None')

        temp = {}
        for action in gameState.getLegalActions(agentNo):
            i = self.minVal(agentNo + 1, gameState.generateSuccessor(agentNo, action), depth, fromPoint, toPoint)[0]
            temp[i] = action
            if min(temp.keys()) < fromPoint:
                return min(temp.keys()), temp[min(temp.keys())]
            toPoint = min(toPoint, min(temp.keys()))

        return min(temp.keys()), temp[min(temp.keys())]

    def maxVal(self, gameState, depth, fromPoint, toPoint):

        if(gameState.isWin() or gameState.isLose() or self.depth == depth):
            return (self.evaluationFunction(gameState), 'None')

        temp = {}
        for action in gameState.getLegalActions(0):
            i = self.minVal(1, gameState.generateSuccessor(0, action), depth, fromPoint, toPoint)[0]
            temp[i] = action
            if max(temp.keys()) > toPoint:
                return max(temp.keys()), temp[max(temp.keys())]
            fromPoint = max(fromPoint, max(temp.keys()))

        return max(temp.keys()), temp[max(temp.keys())]

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        "*** YOUR CODE HERE ***"
        return self.maxVal(gameState,0)[1]

    def expectVal(self, agentNo, gameState, depth):

        if gameState.getNumAgents() <= agentNo:
            return self.maxVal(gameState, depth + 1)[0]

        if(gameState.isWin() or gameState.isLose() or self.depth == depth):
            return self.evaluationFunction(gameState)

        result = 0
        for action in gameState.getLegalActions(agentNo):
            result += self.expectVal(agentNo + 1, gameState.generateSuccessor(agentNo, action), depth) * 1/len(gameState.getLegalActions(agentNo))

        return result

    def maxVal(self, gameState, depth):

        if(gameState.isWin() or gameState.isLose() or self.depth == depth):
            return (self.evaluationFunction(gameState), 'None')

        temp = {}
        for action in gameState.getLegalActions(0):
            i = self.expectVal(1, gameState.generateSuccessor(0, action), depth)
            temp[i] = action

        return max(temp.keys()), temp[max(temp.keys())]

def betterEvaluationFunction(currentGameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION:
    Took the approach of the above evaluation function.

    If not terminal condition,
    evaluate food, capsules, ghosts to get the most accurate evaluation.

    Although this might not be the most efficient, due to the small size of
    the enviornment, I believe some efficiency can be traded off with accuracy.

    Adjusted some weights to fit the currentGameState, not beyond it.

    """
    "*** YOUR CODE HERE ***"

    currPos = currentGameState.getPacmanPosition()
    currFood = currentGameState.getFood().asList()
    currGhosts = currentGameState.getGhostStates()
    currScore = currentGameState.getScore()
    currCapsules = currentGameState.getCapsules()

    result = currScore * 1/25

    #terminal
    if currentGameState.isWin():
        return float('inf')
    if currentGameState.isLose():
        return float('-inf')

    #num of food & capsules evaluation
    result -= 20.0 * len(currFood)
    result -= 40.0 * len(currCapsules)

    #food evaluation
    result += 2.0/getScoreFor(currFood, currPos, 'min')[0]

    #capsule evaluation
    result += 1.0/getScoreFor(currCapsules, currPos, 'min')[0]

    #ghost evaluation
    normGhost = []
    for ghost in currGhosts:
        if ghost.scaredTimer == 0:
            normGhost.append(ghost.getPosition())

    if len(normGhost) != 0:
        result -= 8.0/getScoreFor(normGhost, currPos, 'min')[0]

    #scared ghost evaluation
    scaredGhost = []
    for ghost in currGhosts:
        if ghost.scaredTimer != 0:
            normGhost.append(ghost.getPosition())

    if len(scaredGhost) != 0:
        result += 4.0/getScoreFor(normGhost, currPos, 'min')[0]

    return result

def getScoreFor(list, newPos, indice):
    temp = [manhattanDistance(i, newPos) for i in list]
    if len(temp) == 0:
        return [1.0, 1.0, 1.0]
    if indice == 'max':
        return [1.0, max(max(temp), 1.0), 1.0]
    if indice == 'min':
        return [max(min(temp), 1.0), 1.0, 1.0]
    return [max(min(temp), 1.0), max(max(temp), 1.0), sum(temp)/len(temp)]

# Abbreviation
better = betterEvaluationFunction
