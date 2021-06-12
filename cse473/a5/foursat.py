"""

Algoritm original of DPLL:
https://en.wikipedia.org/wiki/DPLL_algorithm

Translation into the English for the representacion of the clause:
@elper

Input checking, PEP-8 documentation, user guide, other French-to-English:
¯\_(ツ)_/¯

"""
import random


class Problem:
    """ Problem 4-SAT in form CNF.
    Class of problem contains the metadata for n, m, and alpha,
    also the problem of itself.

    Problems are represented as lists of <m> clauses.  Clauses are
    represented as lists of 4 literals.  Literals are represented as
    positive or negative variables, where variables are integers
    ranging from 1 to <n>.  (That is, the variables start with 1, and
    not 0, and appear positively or negatively as literals.)

    Example problem clauses:

      [[ 4, -3, -2,  5 ]
       [ 1, -2,  3, -4 ]
       [-4,  1, -5,  2 ]]

    The above represents a 4-SAT problem with three clauses defined
    over five variables.  The first sublist represents the clause "x4
    or ~x3 or ~x2 or x5," where '~' denotes the 'not' operator.
    """
    def __init__(self, n, m, clauses):
        self.n = n
        self.m = m
        self.alpha = round(float(m) / float(n), 3)
        self.clauses = clauses

    def toFile(self, filename):
        """ Write the problem to a file, in the format DIMACS-modified. """
        with open(filename, 'w') as f:
            f.write(f"p cnf {self.n} {self.m} {self.alpha}\n")
            for clause in self.clauses:
                f.write(' '.join([ str(lit) for lit in clause ]) + '\n')

def generate_problem(n, m):
    """Generate a random 4-SAT problem.
    You tell the function how many variables and how many clauses
    are to be in the problem.


    n: number of variables to use in generating the problem
    m: number of clauses to generate for the problem
    """
    clauses = []
    for _ in range(m):
        clause = []
        while len(clause) < 4:
            variable = random.randrange(1, n+1)

            # Clause must not contain the same variable two times.
            if variable not in clause and -1 * variable not in clause:
                if random.choice([True, False]):
                    literal = variable
                else:
                    literal = -1 * variable
                clause.append(literal)
        clauses.append(clause)
    return Problem(n, m, clauses)

def load_problem(filename):
    """ Reads problem from file in format DIMACS-modified. """
    with open(filename) as f:
        lines = f.readlines()
    lines = [ l.rstrip('\n') for l in lines ]

    header = lines[0].split(' ')
    n = int(header[2])
    m = int(header[3])
    clauses = [ map(int, l.split(' ')) for l in lines[1:] ]

    return Problem(n, m, clauses)

class Solver():
    """
    A 4-SAT solver.

    It is supposed that it will use a backtracking search to determine
    whether the problem is satisfiable or insatisfiable.  But, I think
    it only is doing the algorithm generate-and-test ¯\_(ツ)_/¯.

    It needs to have some improvement to be able to solve the problems
    with the more variables.

    """
    def solve(self, problem):
        """
        Solve the 4-SAT problem.

        Returns False if problem is found insatisfiable, and True if a
        satisfying solution is found.

        The problem of SAT has n variables, so the solver will build a
        list of assignments, length n.  But the index of the variables
        is 1 to n, and yet the list of assignments is of index 0 to
        n-1.  So, the assignment for variable v is stored in
        assignments[v-1].  The assignents[v-1] contains True, then it
        means xi has been set to true, if it contains False then xi is
        false.  But if assignments[i-1] contains None, it means that
        variable xi has been set not yet.
       
        problem: Problem to solve.
        """
        n = problem.n
        m = problem.m
        clauses = problem.clauses

        def solution(clauses, assignment):
            """ True iff assignment satisfies all clauses. Assumes all variables are assigned."""            
            for c in clauses:
                if insat(c, assignment):
                    return False
            return True

        def insat(clause, assignment):
            """ True iff assignment violates the clause explicitly. """
            for lit in clause:
                var = abs(lit)
                
                # If var has no assignment, clause is violated not yet.
                if assignment[var - 1] == None:
                    return False
                
                # If var assignment agrees with literal, clause is satisfied.
                if lit > 0:
                    if assignment[var - 1]:
                        return False
                else:
                    if not assignment[var - 1]:
                        return False
            return True

        depth = 0
        assignment = [None] * n
        while True:

            if depth == -1:
                return False

            if depth == n:
                if solution(clauses, assignment):
                    return True
                else:
                    depth -= 1
                    continue

            if assignment[depth] == None:
                assignment[depth] = True
                depth += 1
            elif assignment[depth] == True:
                assignment[depth] = False
                depth += 1
            elif assignment[depth] == False:
                assignment[depth] = None
                depth -= 1



