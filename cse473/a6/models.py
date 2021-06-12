import nn


class PerceptronModel(object):
    def __init__(self, dimensions):
        """
        Initialize a new Perceptron instance.

        A perceptron classifies data points as either belonging to a particular
        class (+1) or not (-1). `dimensions` is the dimensionality of the data.
        For example, dimensions=2 would mean that the perceptron must classify
        2D points.
        """
        self.w = nn.Parameter(1, dimensions)

    def get_weights(self):
        """
        Return a Parameter instance with the current weights of the perceptron.
        """
        return self.w

    def run(self, x):
        """
        Calculates the score assigned by the perceptron to a data point x.

        Inputs:
            x: a node with shape (1 x dimensions)
        Returns: a node containing a single number (the score)
        """
        return nn.dot_product(self.w, x)

    def get_prediction(self, x):
        """
        Calculates the predicted class for a single data point `x`.

        Returns: 1 or -1
        """
        temp = nn.Node.item(self.run(x))
        return 1 if temp >= 0 else -1

    def train(self, dataset):
        """
        Train the perceptron until convergence.
        """
        batch_size = 1
        while True:
            breaker = 0
            for x, y in dataset.iterate_once(batch_size):
                if self.get_prediction(x) != nn.Node.item(y):
                    nn.Parameter.update(self.w, x, nn.Node.item(y))
                    breaker = 1
            if breaker == 0:
                break


class RegressionModel(object):
    """
    A neural network model for approximating a function that maps from real
    numbers to real numbers. The network should be sufficiently large to be able
    to approximate sin(x) on the interval [-2pi, 2pi] to reasonable precision.
    """

    def __init__(self):
        # Initialize your model parameters here
        self.batch_size = 50
        self.learning_rate = -0.01
        self.w0 = nn.Parameter(1, 50)
        self.w1 = nn.Parameter(50, 1)
        self.b0 = nn.Parameter(1, 50)
        self.b1 = nn.Parameter(1, 1)

    def run(self, x):
        """
        Runs the model for a batch of examples.

        Inputs:
            x: a node with shape (batch_size x 1)
        Returns:
            A node with shape (batch_size x 1) containing predicted y-values
        """
        temp = nn.matmul(x, self.w0)
        return nn.add_bias(nn.matmul(nn.relu(nn.add_bias(temp, self.b0)), self.w1), self.b1)

    def get_loss(self, x, y):
        """
        Computes the loss for a batch of examples.

        Inputs:
            x: a node with shape (batch_size x 1)
            y: a node with shape (batch_size x 1), containing the true y-values
                to be used for training
        Returns: a loss node
        """
        return nn.square_loss(self.run(x), y)

    def train(self, dataset):
        """
        Trains the model.
        """
        while True:
            for x, y in dataset.iterate_once(self.batch_size):
                temp = nn.gradients(self.get_loss(x, y), [self.w0, self.w1, self.b0, self.b1])
                self.w0.update(temp[0], self.learning_rate)
                self.w1.update(temp[1], self.learning_rate)
                self.b0.update(temp[2], self.learning_rate)
                self.b1.update(temp[3], self.learning_rate)

            if nn.Node.item(self.get_loss(nn.Constant(dataset.x), nn.Constant(dataset.y))) <= 0.02:
                return


class DigitClassificationModel(object):
    """
    A model for handwritten digit classification using the MNIST dataset.

    Each handwritten digit is a 28x28 pixel grayscale image, which is flattened
    into a 784-dimensional vector for the purposes of this model. Each entry in
    the vector is a floating point number between 0 and 1.

    The goal is to sort each digit into one of 10 classes (number 0 through 9).

    (See RegressionModel for more information about the APIs of different
    methods here. We recommend that you implement the RegressionModel before
    working on this part of the project.)
    """

    def __init__(self):
        self.batch_size = 100
        self.learning_rate = -0.1
        self.w0 = nn.Parameter(784, 100)
        self.w1 = nn.Parameter(100, 10)
        self.b0 = nn.Parameter(1, 100)
        self.b1 = nn.Parameter(1, 10)

    def run(self, x):
        """
        Runs the model for a batch of examples.

        Your model should predict a node with shape (batch_size x 10),
        containing scores. Higher scores correspond to greater probability of
        the image belonging to a particular class.

        Inputs:
            x: a node with shape (batch_size x 784)
        Output:
            A node with shape (batch_size x 10) containing predicted scores
                (also called logits)
        """
        temp = nn.matmul(x, self.w0)
        return nn.add_bias(nn.matmul(nn.relu(nn.add_bias(temp, self.b0)), self.w1), self.b1)

    def get_loss(self, x, y):
        """
        Computes the loss for a batch of examples.

        The correct labels `y` are represented as a node with shape
        (batch_size x 10). Each row is a one-hot vector encoding the correct
        digit class (0-9).

        Inputs:
            x: a node with shape (batch_size x 784)
            y: a node with shape (batch_size x 10)
        Returns: a loss node
        """
        return nn.softmax_loss(self.run(x), y)

    def train(self, dataset):
        """
        Trains the model.
        """
        while True:
            for x, y in dataset.iterate_once(self.batch_size):
                temp = nn.gradients(self.get_loss(x, y), [self.w0, self.w1, self.b0, self.b1])
                self.w0.update(temp[0], self.learning_rate)
                self.w1.update(temp[1], self.learning_rate)
                self.b0.update(temp[2], self.learning_rate)
                self.b1.update(temp[3], self.learning_rate)

            if dataset.get_validation_accuracy() >= 0.977:
                return


class LanguageIDModel(object):
    """
    A model for language identification at a single-word granularity.

    (See RegressionModel for more information about the APIs of different
    methods here. We recommend that you implement the RegressionModel before
    working on this part of the project.)
    """

    def __init__(self):
        # Our dataset contains words from five different languages, and the
        # combined alphabets of the five languages contain a total of 47 unique
        # characters.
        # You can refer to self.num_chars or len(self.languages) in your code
        self.num_chars = 47
        self.languages = ["English", "Spanish", "Finnish", "Dutch", "Polish"]

        # Initialize your model parameters here
        self.batch_size = 100
        self.learning_rate = -0.1

        self.w0 = nn.Parameter(self.num_chars, 400)
        self.b0 = nn.Parameter(1, 400)

        self.w1 = nn.Parameter(self.num_chars, 400)
        self.wh = nn.Parameter(400, 400)
        self.b1 = nn.Parameter(1, 400)

        self.wf = nn.Parameter(400, len(self.languages))
        self.bf = nn.Parameter(1, len(self.languages))


    def run(self, xs):
        """
        Runs the model for a batch of examples.

        Although words have different lengths, our data processing guarantees
        that within a single batch, all words will be of the same length (L).

        Here `xs` will be a list of length L. Each element of `xs` will be a
        node with shape (batch_size x self.num_chars), where every row in the
        array is a one-hot vector encoding of a character. For example, if we
        have a batch of 8 three-letter words where the last word is "cat", then
        xs[1] will be a node that contains a 1 at position (7, 0). Here the
        index 7 reflects the fact that "cat" is the last word in the batch, and
        the index 0 reflects the fact that the letter "a" is the inital (0th)
        letter of our combined alphabet for this task.

        Your model should use a Recurrent Neural Network to summarize the list
        `xs` into a single node of shape (batch_size x hidden_size), for your
        choice of hidden_size. It should then calculate a node of shape
        (batch_size x 5) containing scores, where higher scores correspond to
        greater probability of the word originating from a particular language.

        Inputs:
            xs: a list with L elements (one per character), where each element
                is a node with shape (batch_size x self.num_chars)
        Returns:
            A node with shape (batch_size x 5) containing predicted scores
                (also called logits)
        """
        temp = nn.relu(nn.add_bias(nn.matmul(xs[0], self.w0), self.b0))
        for c in xs[1:]:
            temp = nn.relu(nn.add_bias(nn.add(nn.matmul(c, self.w1), nn.matmul(temp, self.wh)), self.b1))
        return nn.add_bias(nn.matmul(temp, self.wf), self.bf)

    def get_loss(self, xs, y):
        """
        Computes the loss for a batch of examples.

        The correct labels `y` are represented as a node with shape
        (batch_size x 5). Each row is a one-hot vector encoding the correct
        language.

        Inputs:
            xs: a list with L elements (one per character), where each element
                is a node with shape (batch_size x self.num_chars)
            y: a node with shape (batch_size x 5)
        Returns: a loss node
        """
        return nn.softmax_loss(self.run(xs), y)

    def train(self, dataset):
        """
        Trains the model.
        """
        while True:
            for x, y in dataset.iterate_once(self.batch_size):
                temp = nn.gradients(self.get_loss(x, y), [self.w0, self.b0, self.w1, self.wh, self.b1, self.wf, self.bf])
                self.w0.update(temp[0], self.learning_rate)
                self.b0.update(temp[1], self.learning_rate)
                self.w1.update(temp[2], self.learning_rate)
                self.wh.update(temp[3], self.learning_rate)
                self.b1.update(temp[4], self.learning_rate)
                self.wf.update(temp[5], self.learning_rate)
                self.bf.update(temp[6], self.learning_rate)

            if dataset.get_validation_accuracy() >= 0.85:
                return
