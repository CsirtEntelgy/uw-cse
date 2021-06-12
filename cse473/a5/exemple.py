import foursat
import random


random.seed(42)

p1 = foursat.generate_problem(10,50)
p2 = foursat.generate_problem(10,100)
p3 = foursat.generate_problem(10,150)
p4 = foursat.generate_problem(10,200)

solver = foursat.Solver()
print(solver.solve(p1))
print(solver.solve(p2))
print(solver.solve(p3))
print(solver.solve(p4))

print()

p1.toFile("random-10-50.cnf")
p = foursat.load_problem("random-10-50.cnf")
print(solver.solve(p))

print()

for _ in range(10):
    p = foursat.generate_problem(10, 100)
    print(solver.solve(p))

print()

# oops too slow, Ctrl-C to quit
p = foursat.generate_problem(20,200)
print(solver.solve(p))

