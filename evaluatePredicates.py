#! /usr/bin/python
import sys
from slimit.visitors import nodevisitor
from slimit import ast
from slimit.parser import Parser

''' At compile time, evaluate the predicates simply statically and then build an AST of these predicates 
    This AST should have sufficeint leaves which tell you what nodes the predicate touches .
    First step : Design an AST for this. Algebraic Data Type in Python :) '''
# There are no ADT's in Python. But I can probably use this or an expression evaluator

class Operator:
    WITHIN=1
    AND=2
    OR=3
    FOR=4
    LEAF=5

class Predicate(object) :
    ''' Class of Predicates. Defines a grammar or AST over them '''
    def __init__(self,value=-1) :
      self.atomic=True             # is it an atomic predicate derived from a getPredicate call 
      self.value=value             # has meaning only for an atomic predicate
      self.connector=Operator.LEAF # By default it's a LEAF node
      self.childPred=[]             # list of children that are relevant to the operator in question.

def PrettyPrintPredicate(predicateObj) :
    if(len(predicateObj.childPred)==0):     
       return predicateObj.value.to_ecma()
    else :
       operator=predicateObj.connector
       if (operator==Operator.AND) : 
            return "("+" AND "+PrettyPrintPredicate((predicateObj.childPred[0]))+ " "+PrettyPrintPredicate((predicateObj.childPred[1])) + ")"
       elif (operator==Operator.OR) :
            return "("+" OR "+PrettyPrintPredicate(predicateObj.childPred[0])+ " " + PrettyPrintPredicate(predicateObj.childPred[1]) + ")"
       elif (operator==Operator.WITHIN) : # cause for is a JS keyword
            return "("+ " WITHIN "+ PrettyPrintPredicate(predicateObj.childPred[0])+" " + PrettyPrintPredicate(predicateObj.childPred[1]) + " " + str(astListToecma(predicateObj.params)) + ")" 
       elif (operator==Operator.FOR) :
            return "("+" FOR "+PrettyPrintPredicate(predicateObj.childPred[0])+"  " + str(astListToecma(predicateObj.params)) + ")"
def astListToecma(astNodeList) : 
    strFn=lambda x : x.to_ecma() 
    return map(strFn,astNodeList)

def PredicatePass(tree) :
     # scan only global statements, nothing within functions. 
     '''   One pass over the tree to get the list of all Predicates in the multi script program 
           Parses an expression node in the AST , specifically an expression of the type assignment.
           It returns the string corresponding to the variable name of the predicate if the assignment
           is indeed a predicate ... . Checks for reassignment of predicates to other variables , 
           which is a type error  '''
     predicateList=dict()
     for node in nodevisitor.visit(tree):
        # Store all mobile nodes in a list ######
        if(isinstance(node,ast.ExprStatement)):
            exprNode=node.expr
            if(isinstance(exprNode,ast.Assign)):  # check if this is an assignment to a Device object
                 if( (isinstance(exprNode.children()[0],ast.Identifier)) and (isinstance(exprNode.children()[1],ast.FunctionCall))) : # check if the LHS is an identifier and the RHS is a fnCall
                      fnCallNode=node.expr.children()[1] # get fnCall
                      functionName=fnCallNode.children()[0].to_ecma() # getString repr.
                      if (functionName.startswith("getPredicate")): # maybe add more calls in the future, TODO: Need some analysis to approx. the call results at compile time or defer to runtime
                          predicateVariable=exprNode.children()[0]
                          predicateList[predicateVariable.to_ecma()]=Predicate(fnCallNode)
                      elif (isinstance(fnCallNode.children()[0],ast.DotAccessor)) :
                          dotNotation=fnCallNode.children()[0]
                          argument=fnCallNode.children()[1]
                          print "Object method call "
                          predVar=dotNotation.children()[0]
                          operator=dotNotation.children()[1] # binops such as AND,OR and NOT for now
                          opNum=GetOperator(operator)
                          if (opNum==Operator.FOR) : 
                              tempPred=Predicate(-1)
                              currentPredAtVar=predicateList[predVar.to_ecma()]
                              tempPred.atomic=False # aggregated operator
                              tempPred.connector=opNum # get operator 
                              tempPred.childPred=[currentPredAtVar] # works only for binary operators for now
                              if(len(fnCallNode.children()) >= 2) :
                                 tempPred.params=fnCallNode.children()[1:] # arguments from 2 onwards are other parameters. Nothing for AND
                              predicateList[predVar.to_ecma()]=tempPred 
                          elif (opNum==Operator.AND) or (opNum==Operator.OR) or (opNum==Operator.WITHIN) : 
                              if (predVar.to_ecma() not in predicateList) :
                                  raise Exception ("Calling predicate operator on non-predicate",predVar.to_ecma())
                              if (argument.to_ecma() not in predicateList) : # TODO: For now, only previously declared variables are allowed as arguments, not getPredicate.. itself
                                  raise Exception("Only previously declared predicates are allowed as arguments and not",argument.to_ecma())
                              tempPred=Predicate(-1)
                              currentPredAtVar=predicateList[predVar.to_ecma()]
                              tempPred.atomic=False # aggregated operator
                              tempPred.connector=GetOperator(operator) # get operator 
                              tempPred.childPred=[currentPredAtVar,predicateList[argument.to_ecma()]] # works only for binary operators for now
                              if(len(fnCallNode.children()) >= 3) :
                                 tempPred.params=fnCallNode.children()[2:] # arguments from 2 onwards are other parameters. Nothing for AND
                              predicateList[predVar.to_ecma()]=tempPred 

     return predicateList
def GetOperator(operator) :
    assert(isinstance(operator,ast.Identifier))
    if (operator.to_ecma()=="and") : 
           return Operator.AND
    elif (operator.to_ecma()=="or") :
           return Operator.OR
    elif (operator.to_ecma()=="fortime") : # cause for is a JS keyword
           return Operator.FOR
    elif (operator.to_ecma()=="within") :
           return Operator.WITHIN

if __name__ == "__main__" : 
     parser=Parser() 
     if(len(sys.argv) < 2) : 
       print "Usage : python evaluatePredicates.py fileName \n"
       sys.exit(1)
     sourceCode=open(sys.argv[1]).read()
     print "Source code originally is ............ \n", sourceCode
     tree=parser.parse(sourceCode);
     predicateList=PredicatePass(tree)
     for predicateVar in predicateList : 
            print "Name of variable :",predicateVar
            print "Predicate evaluation",PrettyPrintPredicate(predicateList[predicateVar])
