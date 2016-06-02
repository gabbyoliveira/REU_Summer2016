import csv
import numpy as np
import matplotlib.pyplot as plt


def dataToArray():
    followersCount = np.array([])
    votesCount = np.array([])
    
    with open("users--2016-05-09_16-47-34-UTC.csv", "rb") as postsFile:
        columns = postsFile.readline()
        reader = csv.reader(postsFile, delimiter=';')
        for row in reader:
            followersCount = np.append(followersCount, row[6])
            votesCount = np.append(votesCount, row[8])
 
    return followersCount, votesCount

def dataToGraph():
    t = dataToArray()
    xs = t[0]
    ys = t[1]
    
    plt.plot(xs, ys, 'bo')
    plt.show()
    
    return None

