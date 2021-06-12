import math

def is_multiple_of_9(n):
    return n%9 == 0


def next_prime(m):
	if(m == 1):
		return 2
	for i in range(2, m + 1):
		if(((m + 1) % i) == 0):
			return next_prime(m + 1)
	return m + 1


def quadratic_roots(a, b, c):
	discr = b**2 - 4 * a * c
	if(discr < 0):
		return "complex"
	else:
		x = float((-b + math.sqrt(discr)) / (2 * a))
		y = float((-b - math.sqrt(discr)) / (2 * a))
		return (x, y)


def perfect_shuffle(even_list):
	result = []
	middle = len(even_list)//2
	front = even_list[:middle]
	back = even_list[middle:]
	for i in range(0, len(front)):
		result.append(front[i])
		result.append(back[i])
	return result


def triples_list(input_list):
	return [n * 3 for n in input_list]


def double_consonants(text):
	result = []
	vowels = ['A', 'E', 'I', 'O', 'U', 'a', 'e', 'i', 'o', 'u']
	for c in text:
		result.append(c)
		if c not in vowels and c.isalnum():
			result.append(c)
	return ''.join(result)

def count_words(text):
	dic = ['-', '+', '*', '/', '@', '#', '%', "'"]
	result = {}
	words = get_words(dic, text, 0, 0, [])
	for s in words:
		if s not in result:
			result[s] = 0
		result[s] = result[s] + 1
	return result
	
def get_words(dic, text, start, end, result):
	if end == len(text):
		temp = []
		for c in text[start:end]:
			temp.append(c.lower())
		if len(temp) != 0:
			result.append(''.join(temp))
		return result
	if text[end] not in dic and not text[end].isalnum():
		temp = []
		for c in text[start:end]:
			temp.append(c.lower())
		if len(temp) != 0:
			result.append(''.join(temp))
		return get_words(dic, text, end + 1, end + 1, result)
	return get_words(dic, text, start, end + 1, result)
		

def make_cubic_evaluator(a, b, c, d):
    return lambda x : a * x**3 + b * x**2 + c * x + d
	
class Polygon:

	def __init__(self, n_sides, lengths=None, angles=None):
		self.n_sides = n_sides
		self.lengths = lengths
		self.angles = angles
		
	def is_rectangle(self):
		if self.n_sides == 4:
			if self.angles == None:
				return None
			if self.angles == [90, 90, 90, 90]:
				return True
		return False

	def is_rhombus(self):
		if self.n_sides == 4:
			if self.lengths == None and self.angles == None:
				return None
			if self.lengths != None:
				if self.lengths.count(self.lengths[0]) == len(self.lengths):
					return True
			if self.angles != None:
				if self.lengths == None and self.angles[0] == self.angles [2] and self.angles [1] == self.angles [3]:
					return None
			if self.angles != None and self.lengths != None:
				if self.lengths.count(self.lengths[0]) == len(self.lengths) and self.angles[0] == self.angles [2] and self.angles [1] == self.angles [3]:
					return True
		return False

	def is_square(self):
		if self.n_sides == 4:
			if self.lengths == None and self.angles == None:
				return None
			if self.lengths != None:
				if self.angles == None and self.lengths.count(self.lengths[0]) == len(self.lengths):
					return None
			if self.angles != None:
				if self.lengths == None and self.angles == [90, 90, 90, 90]:
					return None
			if self.angles != None and self.lengths != None:
				if self.lengths.count(self.lengths[0]) == len(self.lengths) and self.angles == [90, 90, 90, 90]:
					return True
		return False

	def is_regular_hexagon(self):
		if self.n_sides == 6:
			if self.lengths == None and self.angles == None:
				return None
			if self.lengths != None:
				if self.angles == None and self.lengths.count(self.lengths[0]) == len(self.lengths):
					return None
			if self.angles != None:
				if self.lengths == None and self.angles == [120, 120, 120, 120, 120, 120]:
					return None
			if self.angles != None and self.lengths != None:
				if self.lengths.count(self.lengths[0]) == len(self.lengths) and self.angles == [120, 120, 120, 120, 120, 120]:
					return True
		return False

	def is_isosceles_triangle(self):
		if self.n_sides == 3:
			if self.lengths == None and self.angles == None:
				return None
			if self.lengths != None:
				if self.lengths[0] == self.lengths[1] or self.lengths[0] == self.lengths[2] or self.lengths[1] == self.lengths[2]:
					return True
			if self.angles != None:
				if self.angles[0] == self.angles[1] or self.angles[0] == self.angles[2] or self.angles[1] == self.angles[2] and self.angles[0] + self.angles[1] + self.angles[2] == 180:
					return True
		return False

	def is_equilateral_triangle(self):
		if self.n_sides == 3:
			if self.lengths == None and self.angles == None:
				return None
			if self.lengths != None:
				if self.lengths[0] == self.lengths[1] and self.lengths[1] == self.lengths[2]:
					return True
			if self.angles != None:
				if self.angles[0] == self.angles[1] and self.angles[1] == self.angles[2] and self.angles[0] + self.angles[1] + self.angles[2] == 180:
					return True
		return False

	def is_scalene_triangle(self):
		if self.n_sides == 3:
			if self.lengths == None and self.angles == None:
				return None
			if not self.is_isosceles_triangle() and not self.is_equilateral_triangle():
				return True
		return False