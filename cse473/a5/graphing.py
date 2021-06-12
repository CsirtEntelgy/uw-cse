# You can put any python code that you use to make your graph in here.
# Feel free to modify any part of this file :D

import foursat
import random
import matplotlib.pyplot as plt
import numpy as np

solver = foursat.Solver()
temp = {}

for x in range(1, 31):

    total = 0
    num_false = 0

    for _ in range(10):
        a = random.randint(10,16)
        b = foursat.generate_problem(a, a * x)
        c = solver.solve(b)

        total += 1
        if (not c):
            num_false += 1

    temp[x] = num_false / total

xs = []
ys = []

for k in temp.keys():
    xs.append(k)
    ys.append(temp[k])

xpoints = np.array(xs)
ypoints = np.array(ys)

plt.plot(xpoints, ypoints, '-ok')

plt.savefig('foo.png')
plt.savefig('foo2.png', bbox_inches='tight')
