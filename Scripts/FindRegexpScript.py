#Script test to find "regexp" on a javascript file
import os
import os.path

def readExtension(directory):
    listFiles = os.listdir(directory)
    dictFiles = {}
    extension = ""
    
    for elem in listFiles:
        if os.path.isfile(directory + "\\" + elem):
            if(".js" in elem):
                extension = "js"
            elif(".java" in elem):
                extension = "java"
            elif(".rb" in elem):
                extension = "rb"
            elif("py" in elem or "pyc" in elem):
                extenstion = "py"
            else:
                extension = "other"
                
            dictFiles[elem] = openFile(directory + "\\" + elem, extension)

    return dictFiles

def openFile(fileName, extension):
    #print extension
    #print fileName
    with open(fileName, "rb") as inputFile:
        
        if(extension == "js"):
            return findRegExpJS(inputFile)
        elif(extension == "java"):
            return findRegExpJava(inputFile)      
        elif(extension == "rb"):
            return findRegExpRuby(inputFile)
        elif(extension == "py" or extension == "pyc"):
            return findRegExpPython(inputFile)
        elif(extension == "go"):
            return findRegExpGo(inputFile)
        else:
            return findRegExpGeneral(inputFile)
    return None

    
def findRegExpJS(inputFile):
    counter = 1
    regexpLines = set()
    for line in inputFile:
        if(("RegExp" in line) or (".exec(" in line) or (".test(" in line) or ("replace" in line) or (".prototype" in line.lower())):
            regexpLines.add(counter)
        counter += 1
            
    regexpLines = sorted(regexpLines)
        
    return regexpLines
            

def findRegExpJava(inputFile):
    counter = 1
    regexpLines = set()
        
    for line in inputFile:
        if(("Pattern" in line) or ("matcher(" in line) or ("regex" in line.lower())):
            regexpLines.add(counter)
        counter += 1

    regexpLines = sorted(regexpLines)
        
    return regexpLines


def findRegExpRuby(inputFile):
    counter = 1
    regexpLines = set()
        
    for line in inputFile:
        if(("RegExp" in line) or (".match(" in line) or ("regex" in line.lower())):
            regexpLines.add(counter)
        counter += 1

    regexpLines = sorted(regexpLines)
        
    return regexpLines

def findRegExpPython(inputFile):
    counter = 1
    regexpLines = set()
    for line in inputFile:
        if(("re.match(" in line) or ("re.compile(" in line) or ("re." in line) or (".match(" in line)):
            regexpLines.add(counter)
        counter += 1
            
    regexpLines = sorted(regexpLines)
        
    return regexpLines

def findRegExpGo(inputFile):
    counter = 1
    regexpLines = set()
    for line in inputFile:
        if(("regexp" in line.lower()) or
           ("regexp." in line.lower()) or
           (".match" in line.lower()) or
           (".matchreader" in line.lower()) or
           (".matchstring" in line.lower()) or
           (".compile" in line.lower()) or
           (".mustcompile" in line.lower()) or
           (".find" in line.lower())):
            regexpLines.add(counter)
        counter += 1
            
    regexpLines = sorted(regexpLines)
        
    return regexpLines

def findRegExpGeneral(inputFile):
    counter = 1
    regexpLines = set()
    for line in inputFile:
        if("regex" in line.lower() or "regexp" in line.lower()):
            regexpLines.add(counter)
        counter += 1
            
    regexpLines = sorted(regexpLines)
        
    return regexpLines
def printResults(dictionary):
    print
    
    for key in dictionary:
        print (str(key) + ": "),
        for elem in dictionary[key]:
            print(str(elem) + ", "),
        print
        print

if __name__ == '__main__':
    val = raw_input("Enter path of directory that contains the files: ")
    printResults(readExtension(val))
    
